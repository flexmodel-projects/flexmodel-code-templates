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
import com.fasterxml.jackson.databind.ObjectMapper;

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

  private void loadTemplateNames() {
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
        templateNames = stream.map(p -> p.getFileName().toString()).toList();
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Failed to load template names", e);
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


  public List<String> getTemplates() {
    return templateNames;
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
      copyFile(path, context, sourceDirectory, targetDirectory, outputFiles);
    }
  }

  private void copyFile(Path path, GenerationContext context, String sourceDirectory, String targetDirectory, List<File> outputFiles) throws Exception {
    String targetPath = resolveTargetPath(path.toString(), context, sourceDirectory, targetDirectory);
    Files.copy(path, Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
    outputFiles.add(new File(targetPath));
  }

  private String resolveTargetPath(String sourcePath, GenerationContext context, String sourceDirectory, String targetDirectory) {
    String filePath = simpleRenderTemplate(sourcePath, context.getVariables()).replace("\\", "/");
    return filePath.replace(sourceDirectory, targetDirectory);
  }

  /**
   * Merge user variables with default variables from .flexmodel/variables.json
   */
  private Map<String, Object> mergeWithDefaultVariables(String templateName, Map<String, Object> userVariables) {
    Map<String, Object> mergedVariables = new HashMap<>();
    
    try {
      // Load default variables from .flexmodel/variables.json
      Map<String, Object> defaultVariables = loadDefaultVariables(templateName);
      if (defaultVariables != null) {
        mergedVariables.putAll(defaultVariables);
        log.debug("Loaded default variables for template {}: {}", templateName, defaultVariables.keySet());
      }
    } catch (Exception e) {
      log.debug("No default variables found for template {}: {}", templateName, e.getMessage());
    }
    
    // User variables override default variables
    if (userVariables != null) {
      mergedVariables.putAll(userVariables);
      log.debug("Merged user variables: {}", userVariables.keySet());
    }
    
    return mergedVariables;
  }

  /**
   * Load default variables from .flexmodel/variables.json
   */
  private Map<String, Object> loadDefaultVariables(String templateName) throws Exception {
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
    
    ObjectMapper mapper = new ObjectMapper();
    String jsonContent = Files.readString(variablesPath);
    @SuppressWarnings("unchecked")
    Map<String, Object> variables = mapper.readValue(jsonContent, Map.class);
    
    return variables;
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
