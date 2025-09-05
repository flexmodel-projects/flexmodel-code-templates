package tech.wetech.flexmodel.codegen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

/**
 * @author cjbi
 */
class CodeGenerationServiceTest extends AbstractIntegrationTest {

  @Test
  void testGetTemplatePath() {
    Path templatePath = codeGenerationService.getTemplatePath("java_sdk");
    log.info("testGetTemplatePath, templatePath: {}", templatePath);
  }

  @Test
  void testGetTemplates() {
    List<String> templates = codeGenerationService.getTemplates();
    log.info("testGetTemplates, templates: {}", templates);
    Assertions.assertFalse(templates.isEmpty());
  }

  @Test
  void testGenerateCode() {
    List<String> templates = codeGenerationService.getTemplates();
    templates.forEach(template -> {
      Path path = codeGenerationService.generateCode(SCHEMA_NAME, template, new HashMap<>());
      log.info("testGenerateCode, template: {}, path: {}", template, path);
      Assertions.assertTrue(path.toFile().exists());
      Assertions.assertTrue(path.toFile().isDirectory());
    });


  }
}
