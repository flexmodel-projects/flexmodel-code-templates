package tech.wetech.flexmodel.codegen;

import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.wetech.flexmodel.JsonUtils;
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

  private final GroovyClassLoader loader = new GroovyClassLoader();

  private final SessionFactory sessionFactory;

  private final Map<String, TemplateInfo> templateInfoMap = new HashMap<>();

  private final Logger log = LoggerFactory.getLogger(CodeGenerationService.class);

  public CodeGenerationService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
    initializeFileSystem();
    loadTemplates();
    registerShutdownHook();
    log.info("Jar package template has been mounted successfully");
  }

  private void initializeFileSystem() {
    URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT);
    if (resUrl == null) {
      throw new IllegalStateException("Resource not found: " + TEMPLATE_ROOT);
    }

    try {
      String protocol = resUrl.getProtocol();
      if ("file".equalsIgnoreCase(protocol)) {
        // When debugging in IDE, resources are in local file system, use default file system
        fs = FileSystems.getDefault();
      } else if ("jar".equalsIgnoreCase(protocol)) {
        // In JAR package, need to create new file system
        JarURLConnection jarCon = (JarURLConnection) resUrl.openConnection();
        Path jarPath = Paths.get(jarCon.getJarFileURL().toURI());
        fs = FileSystems.newFileSystem(jarPath, Map.of("create", "false"));
      } else {
        throw new IllegalStateException("Unknown resource type: " + protocol);
      }
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException("Failed to get template path", e);
    }
  }

  private void loadTemplates() {
    try {
      Path templatePath;
      if (fs == FileSystems.getDefault()) {
        // Local file system, use relative path
        URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT);
        templatePath = Paths.get(resUrl.toURI());
      } else {
        // JAR file system, use absolute path
        templatePath = fs.getPath("/" + TEMPLATE_ROOT);
      }

      try (Stream<Path> stream = Files.list(templatePath)) {
        List<String> templateNames = stream.map(p -> p.getFileName().toString()).toList();
        preloadTemplates(templateNames);
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Failed to load template names", e);
    }
  }

  private void preloadTemplates(List<String> templateNames) {
    log.debug("Preloading default variables for all templates...");
    for (String templateName : templateNames) {
      try {
        loadTemplate(templateName);
        log.debug("Preloaded default variables for template: {}", templateName);
      } catch (Exception e) {
        log.debug("No default variables found for template: {}", templateName);
      }
    }
    log.debug("Preloading completed. Cached {} template variables.", templateInfoMap.size());
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
      // Local file system, use relative path
      try {
        URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT + "/" + templateName);
        if (resUrl == null) {
          throw new IllegalStateException("Template not found: " + templateName);
        }
        return Paths.get(resUrl.toURI());
      } catch (URISyntaxException e) {
        throw new RuntimeException("Failed to get template path: " + templateName, e);
      }
    } else {
      // JAR file system, use absolute path
      return fs.getPath("/" + TEMPLATE_ROOT + "/" + templateName);
    }
  }


  public List<TemplateInfo> getTemplates() {
    return templateInfoMap.values().stream()
      .toList();
  }

  /**
   * Generate code to temporary directory based on datasource and modelName, return root path.
   */
  public Path generateCode(String datasourceName, String templateName, Map<String, Object> variables) {
    long startTime = System.currentTimeMillis();
    log.debug("Starting code generation - datasource: {}, template: {}", datasourceName, templateName);

    List<File> outputFiles = new ArrayList<>();
    try {
      // Merge with default variables from .flexmodel/variables.json
      Map<String, Object> mergedVariables = mergeWithDefaultVariables(templateName, variables);

      GenerationContext ctx = buildContext(datasourceName, mergedVariables);
      java.nio.file.Path targetPath = Paths.get(System.getProperty("java.io.tmpdir"), "codegen", "" + System.currentTimeMillis());
      Path templateDir = getTemplatePath(templateName);
      outputFiles(loader, ctx, templateDir,
        templateDir.toString(), targetPath.toString(), outputFiles);

      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      log.debug("Code generation completed - duration: {}ms, files generated: {}, output path: {}",
        duration, outputFiles.size(), targetPath);

      return targetPath;
    } catch (Exception e) {
      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      log.error("Code generation failed - duration: {}ms, datasource: {}, template: {}",
        duration, datasourceName, templateName, e);
      throw new RuntimeException(e);
    }
  }

  private void outputFiles(GroovyClassLoader classLoader, GenerationContext context, Path dir, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    try (Stream<Path> paths = Files.walk(dir)) {
      paths.forEach(path -> {
        try {
          outFile(classLoader, context, sourceDirectory, targetDirectory, outputFiles, path);
        } catch (Exception e) {
          throw new RuntimeException("Failed to process file: " + path, e);
        }
      });
    }
  }

  private void outFile(GroovyClassLoader classLoader, GenerationContext context, String sourceDirectory, String targetDirectory, List<File> outputFiles, Path path) throws Exception {
    // Skip .flexmodel directory and its contents
    if (isFlexmodelPath(path)) {
      return;
    }

    String normalizedSource = sourceDirectory.replace("\\", "/");
    String normalizedTarget = targetDirectory.replace("\\", "/");

    if (Files.isDirectory(path)) {
      createDirectory(path, context, normalizedSource, normalizedTarget, outputFiles);
    } else if (path.toString().endsWith(".groovy")) {
      processGroovyFile(classLoader, context, path, normalizedSource, normalizedTarget, outputFiles);
    } else {
      copyFile(path, context, normalizedSource, normalizedTarget, outputFiles);
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

  private void copyFile(Path path, GenerationContext context, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    String targetPath = resolveTargetPath(path.toString(), context, sourceDirectory, targetDirectory);

    // Read source file content and apply template variable replacement
    String content = Files.readString(path);
    String processedContent = simpleRenderTemplate(content, context.getVariables());

    // Ensure target directory exists and write processed content
    Path targetFilePath = Paths.get(targetPath);
    Files.createDirectories(targetFilePath.getParent());
    Files.writeString(targetFilePath, processedContent);

    outputFiles.add(new File(targetPath));
  }

  private String resolveTargetPath(String sourcePath, GenerationContext context, String sourceDirectory, String targetDirectory) {
    String filePath = simpleRenderTemplate(sourcePath, context.getVariables()).replace("\\", "/");
    return filePath.replace(sourceDirectory, targetDirectory);
  }

  /**
   * Check if the path is within .flexmodel directory
   */
  private boolean isFlexmodelPath(Path path) {
    String pathString = path.toString().replace("\\", "/");
    return pathString.contains("/.flexmodel/") || pathString.endsWith("/.flexmodel");
  }


  /**
   * Merge user variables with preloaded default variables
   */
  private Map<String, Object> mergeWithDefaultVariables(String templateName, Map<String, Object> userVariables) {
    // Get preloaded default variables from cache
    TemplateInfo templateInfo = templateInfoMap.get(templateName);


    // User variables provided, need to merge
    if (templateInfo != null) {
      Map<String, Object> mergedVariables = new HashMap<>(templateInfo.variables());
      mergedVariables.putAll(userVariables);
      return mergedVariables;
    }

    // No default variables, return user variables
    return new HashMap<>(userVariables);
  }

  /**
   * Load default variables from .flexmodel/variables.json and cache them
   */
  private Map<String, Object> loadTemplate(String templateName) throws Exception {
    Path variablesPath;

    if (fs == FileSystems.getDefault()) {
      // Local file system
      URL resUrl = this.getClass().getClassLoader().getResource(TEMPLATE_ROOT + "/" + templateName + "/.flexmodel/variables.json");
      if (resUrl == null) {
        return null; // No default variables file
      }
      variablesPath = Paths.get(resUrl.toURI());
    } else {
      // JAR file system
      variablesPath = fs.getPath("/" + TEMPLATE_ROOT + "/" + templateName + "/.flexmodel/variables.json");
      if (!Files.exists(variablesPath)) {
        return null; // No default variables file
      }
    }

    String jsonContent = Files.readString(variablesPath);
    Map<String, Object> variables = JsonUtils.parseToMap(jsonContent);
    // Cache the result
    templateInfoMap.put(templateName, new TemplateInfo(templateName, variables));

    return variables;
  }

  private GenerationContext buildContext(String datasource, Map<String, Object> variables) {
    String packageName = variables.getOrDefault("packageName", "com.example").toString();

    // Create a copy of variables to avoid modifying the original
    Map<String, Object> contextVariables = new HashMap<>(variables);

    GenerationContext ctx = new GenerationContext();
    ctx.setSchemaName(datasource);
    ctx.setPackageName(packageName);
    ctx.setVariables(contextVariables);

    // Load all models
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
