package tech.wetech.flexmodel.codegen;

import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.wetech.flexmodel.model.EntityDefinition;
import tech.wetech.flexmodel.model.EnumDefinition;
import tech.wetech.flexmodel.session.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static tech.wetech.flexmodel.sql.StringHelper.simpleRenderTemplate;

/**
 * @author cjbi
 */
public class CodeGenerationService {

  private static final String TEMPLATE_ROOT = "templates";

  private static FileSystem fs = null;

  GroovyClassLoader loader = new GroovyClassLoader();

  private static List<String> templateNames;
  private final SessionFactory sessionFactory;

  private final Logger log = LoggerFactory.getLogger(CodeGenerationService.class);

  public CodeGenerationService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
    initializeFileSystem();
    loadTemplateNames();
    registerShutdownHook();
    log.info("Jar package template has been mounted successfully");
  }

  private void initializeFileSystem() {
    URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT);
    if (resUrl == null) {
      throw new IllegalStateException("资源未找到: " + TEMPLATE_ROOT);
    }

    try {
      String protocol = resUrl.getProtocol();
      if ("file".equalsIgnoreCase(protocol)) {
        // IDE 调试时，资源位于本地文件系统，直接使用默认文件系统
        fs = FileSystems.getDefault();
      } else if ("jar".equalsIgnoreCase(protocol)) {
        // JAR 包中，需要创建新的文件系统
        JarURLConnection jarCon = (JarURLConnection) resUrl.openConnection();
        Path jarPath = Paths.get(jarCon.getJarFileURL().toURI());
        fs = FileSystems.newFileSystem(jarPath, Map.of("create", "false"));
      } else {
        throw new IllegalStateException("未知资源类型: " + protocol);
      }
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException("获取模板路径失败", e);
    }
  }

  private void loadTemplateNames() {
    try {
      Path templatePath;
      if (fs == FileSystems.getDefault()) {
        // 本地文件系统，使用相对路径
        URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT);
        templatePath = Paths.get(resUrl.toURI());
      } else {
        // JAR 文件系统，使用绝对路径
        templatePath = fs.getPath("/" + TEMPLATE_ROOT);
      }

      try (Stream<Path> stream = Files.list(templatePath)) {
        templateNames = stream.map(p -> p.getFileName().toString()).toList();
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("加载模板名称失败", e);
    }
  }

  private void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        if (fs != null) {
          fs.close();
        }
      } catch (IOException ignored) {
      }
    }));
  }

  public Path getTemplatePath(String templateName) {
    if (fs == FileSystems.getDefault()) {
      // 本地文件系统，使用相对路径
      try {
        URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT + "/" + templateName);
        if (resUrl == null) {
          throw new IllegalStateException("模板未找到: " + templateName);
        }
        return Paths.get(resUrl.toURI());
      } catch (URISyntaxException e) {
        throw new RuntimeException("获取模板路径失败: " + templateName, e);
      }
    } else {
      // JAR 文件系统，使用绝对路径
      return fs.getPath("/" + TEMPLATE_ROOT + "/" + templateName);
    }
  }


  public List<String> getTemplates() {
    return templateNames;
  }

  /**
   * 根据 datasource 和 modelName，生成代码到临时目录并返回根路径。
   */
  public Path generateCode(String datasourceName, String templateName, Map<String, Object> variables) {
    List<File> outputFiles = new ArrayList<>();
    try {
      GenerationContext ctx = buildContext(datasourceName, new HashMap<>(variables));
      java.nio.file.Path targetPath = Paths.get(System.getProperty("java.io.tmpdir"), "codegen", "" + System.currentTimeMillis());
      Path templateDir = getTemplatePath(templateName);
      outputFiles(loader, ctx, templateDir,
        templateDir.toString(), targetPath.toString(), outputFiles);
      return targetPath;
    } catch (Exception e) {
      log.error("Generate code error", e);
      throw new RuntimeException(e);
    }
  }

  private void outputFiles(GroovyClassLoader classLoader, GenerationContext context, Path dir, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    try (Stream<Path> paths = Files.walk(dir)) {
      paths.forEach(path -> {
        try {
          outFile(classLoader, context, sourceDirectory, targetDirectory, outputFiles, path);
        } catch (Exception e) {
          throw new RuntimeException("处理文件失败: " + path, e);
        }
      });
    }
  }

  private void outFile(GroovyClassLoader classLoader, GenerationContext context, String sourceDirectory, String targetDirectory, List<File> outputFiles, Path path) throws Exception {
    String normalizedSource = sourceDirectory.replace("\\", "/");
    String normalizedTarget = targetDirectory.replace("\\", "/");

    if (Files.isDirectory(path)) {
      createDirectory(path, context, normalizedSource, normalizedTarget, outputFiles);
    } else if (path.toString().endsWith(".groovy")) {
      processGroovyFile(classLoader, context, path, normalizedSource, normalizedTarget, outputFiles);
    } else {
      processFile(path, context, sourceDirectory, targetDirectory, outputFiles);
    }
  }

  private void createDirectory(Path path, GenerationContext context, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    String targetPath = resolveTargetPath(path.toString(), context, sourceDirectory, targetDirectory);
    File targetDir = new File(targetPath);
    targetDir.mkdirs();
    outputFiles.add(targetDir);
  }

  private void processGroovyFile(GroovyClassLoader classLoader, GenerationContext context, Path path, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    try {
      Class<?> scriptClass = classLoader.parseClass(Files.readString(path));
      Object groovyObject = scriptClass.getDeclaredConstructor().newInstance();
      String targetPath = resolveTargetPath(path.getParent().toString(), context, sourceDirectory, targetDirectory);

      @SuppressWarnings("unchecked")
      List<File> result = (List<File>) scriptClass.getMethod("generate", GenerationContext.class, String.class)
        .invoke(groovyObject, context, targetPath);
      outputFiles.addAll(result);
    } catch (Exception e) {
      log.error("Generate file error, file: {}", path, e);
      String targetPath = resolveTargetPath(path.toString(), context, sourceDirectory, targetDirectory);
      Files.copy(path, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
      outputFiles.add(new File(targetPath));
    }
  }

  private void processFile(Path path, GenerationContext context, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    String targetPath = resolveTargetPath(path.toString(), context, sourceDirectory, targetDirectory);

    // 读取源文件内容并进行变量替换
    String content = Files.readString(path);
    String processedContent = simpleRenderTemplate(content, context.getVariables());

    // 写入处理后的内容到目标文件
    Files.write(Paths.get(targetPath), processedContent.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    outputFiles.add(new File(targetPath));
  }

  private String resolveTargetPath(String sourcePath, GenerationContext context, String sourceDirectory, String targetDirectory) {
    String filePath = simpleRenderTemplate(sourcePath, context.getVariables()).replace("\\", "/");
    return filePath.replace(sourceDirectory, targetDirectory);
  }

  private GenerationContext buildContext(String datasource, Map<String, Object> variables) {
    String packageName = variables.getOrDefault("packageName", "com.example").toString();
    if (!variables.containsKey("packageName")) {
      String packageNameOfPath = packageName.replaceAll("\\.", "/");
      variables.put("packageName", "com.example");
      variables.put("path", packageNameOfPath);
    }
    GenerationContext ctx = new GenerationContext();
    ctx.setSchemaName(datasource);
    ctx.setPackageName(packageName);
    ctx.setVariables(variables);

    // 加载所有模型
    sessionFactory.getModels(datasource).forEach(model -> {
      if (model instanceof EntityDefinition entity) {
        ctx.getModelClassList().add(ModelClass.buildModelClass("^fs_", ctx.getPackageName(), datasource, entity));
      } else if (model instanceof EnumDefinition enumDef) {
        ctx.getEnumClassList().add(EnumClass.buildEnumClass(ctx.getPackageName(), datasource, enumDef));
      }
    });

    return ctx;
  }

}
