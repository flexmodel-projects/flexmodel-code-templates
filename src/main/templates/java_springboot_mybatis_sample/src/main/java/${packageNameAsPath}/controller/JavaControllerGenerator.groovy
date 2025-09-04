import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class JavaControllerGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + 'Controller.java').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def basePath = '/' + modelClass.shortClassName.toLowerCase() + 's'
    out.println "package ${modelClass.packageName}.controller;"
    out.println ""
    out.println "import java.util.List;"
    out.println "import org.springframework.http.HttpStatus;"
    out.println "import org.springframework.web.bind.annotation.*;"
    out.println "import ${modelClass.packageName}.entity.${modelClass.shortClassName};"
    out.println "import ${modelClass.packageName}.service.${modelClass.shortClassName}Service;"
    out.println ""
    out.println "@RestController"
    out.println "@RequestMapping(\"${basePath}\")"
    out.println "public class ${modelClass.shortClassName}Controller {"
    out.println "  private final ${modelClass.shortClassName}Service service;"
    out.println ""
    out.println "  public ${modelClass.shortClassName}Controller(${modelClass.shortClassName}Service service) {"
    out.println "    this.service = service;"
    out.println "  }"
    out.println ""
    out.println "  @GetMapping"
    out.println "  public List<${modelClass.shortClassName}> list() {"
    out.println "    return service.list();"
    out.println "  }"
    out.println ""
    out.println "  @GetMapping(\"/page\")"
    out.println "  public List<${modelClass.shortClassName}> page(@RequestParam int offset, @RequestParam int limit) {"
    out.println "    return service.page(offset, limit);"
    out.println "  }"
    out.println ""
    out.println "  @GetMapping(\"/{id}\")"
    out.println "  public ${modelClass.shortClassName} get(@PathVariable Long id) {"
    out.println "    return service.get(id);"
    out.println "  }"
    out.println ""
    out.println "  @PostMapping"
    out.println "  @ResponseStatus(HttpStatus.CREATED)"
    out.println "  public Long create(@RequestBody ${modelClass.shortClassName} entity) {"
    out.println "    return service.create(entity);"
    out.println "  }"
    out.println ""
    out.println "  @PostMapping(\"/batch\")"
    out.println "  @ResponseStatus(HttpStatus.CREATED)"
    out.println "  public int createBatch(@RequestBody List<${modelClass.shortClassName}> entities) {"
    out.println "    return service.createBatch(entities);"
    out.println "  }"
    out.println ""
    out.println "  @PutMapping(\"/{id}\")"
    out.println "  public void update(@PathVariable Long id, @RequestBody ${modelClass.shortClassName} entity) {"
    out.println "    entity.setId(id);"
    out.println "    service.update(entity);"
    out.println "  }"
    out.println ""
    out.println "  @DeleteMapping(\"/{id}\")"
    out.println "  @ResponseStatus(HttpStatus.NO_CONTENT)"
    out.println "  public void delete(@PathVariable Long id) {"
    out.println "    service.delete(id);"
    out.println "  }"
    out.println ""
    out.println "  @DeleteMapping"
    out.println "  @ResponseStatus(HttpStatus.NO_CONTENT)"
    out.println "  public void deleteByIds(@RequestParam List<Long> ids) {"
    out.println "    service.deleteByIds(ids);"
    out.println "  }"
    out.println ""
    out.println "  @GetMapping(\"/ids\")"
    out.println "  public List<${modelClass.shortClassName}> findByIds(@RequestParam List<Long> ids) {"
    out.println "    return service.findByIds(ids);"
    out.println "  }"
    out.println ""
    out.println "  @GetMapping(\"/count\")"
    out.println "  public long count() {"
    out.println "    return service.count();"
    out.println "  }"
    out.println ""
    out.println "  @GetMapping(\"/exists/{id}\")"
    out.println "  public boolean existsById(@PathVariable Long id) {"
    out.println "    return service.existsById(id);"
    out.println "  }"
    out.println "}"
  }
}


