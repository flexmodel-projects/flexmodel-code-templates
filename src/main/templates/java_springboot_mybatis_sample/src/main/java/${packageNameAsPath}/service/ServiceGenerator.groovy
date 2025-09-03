import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class ServiceGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + 'Service.java').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    out.println "package ${modelClass.packageName}.service;"
    out.println ""
    out.println "import java.util.List;"
    out.println "import org.springframework.stereotype.Service;"
    out.println "import org.springframework.transaction.annotation.Transactional;"
    out.println "import ${modelClass.packageName}.entity.${modelClass.shortClassName};"
    out.println "import ${modelClass.packageName}.mapper.${modelClass.shortClassName}Mapper;"
    out.println ""
    out.println "@Service"
    out.println "@Transactional(readOnly = true)"
    out.println "public class ${modelClass.shortClassName}Service {"
    out.println "  private final ${modelClass.shortClassName}Mapper mapper;"
    out.println ""
    out.println "  public ${modelClass.shortClassName}Service(${modelClass.shortClassName}Mapper mapper) {"
    out.println "    this.mapper = mapper;"
    out.println "  }"
    out.println ""
    out.println "  public List<${modelClass.shortClassName}> list() {"
    out.println "    return mapper.findAll();"
    out.println "  }"
    out.println ""
    out.println "  public List<${modelClass.shortClassName}> page(int offset, int limit) {"
    out.println "    return mapper.findPage(offset, limit);"
    out.println "  }"
    out.println ""
    out.println "  public ${modelClass.shortClassName} get(Long id) {"
    out.println "    return mapper.findById(id);"
    out.println "  }"
    out.println ""
    out.println "  @Transactional"
    out.println "  public Long create(${modelClass.shortClassName} entity) {"
    out.println "    mapper.insert(entity);"
    out.println "    return entity.getId();"
    out.println "  }"
    out.println ""
    out.println "  @Transactional"
    out.println "  public int createBatch(List<${modelClass.shortClassName}> entities) {"
    out.println "    return mapper.insertBatch(entities);"
    out.println "  }"
    out.println ""
    out.println "  @Transactional"
    out.println "  public void update(${modelClass.shortClassName} entity) {"
    out.println "    mapper.update(entity);"
    out.println "  }"
    out.println ""
    out.println "  @Transactional"
    out.println "  public void delete(Long id) {"
    out.println "    mapper.deleteById(id);"
    out.println "  }"
    out.println ""
    out.println "  @Transactional"
    out.println "  public void deleteByIds(List<Long> ids) {"
    out.println "    mapper.deleteByIds(ids);"
    out.println "  }"
    out.println ""
    out.println "  public List<${modelClass.shortClassName}> findByIds(List<Long> ids) {"
    out.println "    return mapper.findByIds(ids);"
    out.println "  }"
    out.println ""
    out.println "  public long count() {"
    out.println "    return mapper.count();"
    out.println "  }"
    out.println ""
    out.println "  public boolean existsById(Long id) {"
    out.println "    return mapper.existsById(id) > 0;"
    out.println "  }"
    out.println "}"
  }
}


