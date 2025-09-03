import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class MapperInterfaceGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + 'Mapper.java').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    out.println "package ${modelClass.packageName}.mapper;"
    out.println ""
    out.println "import java.util.List;"
    out.println "import java.util.Collection;"
    out.println "import org.apache.ibatis.annotations.Mapper;"
    out.println "import org.apache.ibatis.annotations.Param;"
    out.println "import ${modelClass.packageName}.entity.${modelClass.shortClassName};"
    out.println ""
    out.println "@Mapper"
    out.println "public interface ${modelClass.shortClassName}Mapper {"
    out.println "  List<${modelClass.shortClassName}> findAll();"
    out.println ""
    out.println "  ${modelClass.shortClassName} findById(@Param(\"id\") Long id);"
    out.println ""
    out.println "  int insert(${modelClass.shortClassName} entity);"
    out.println ""
    out.println "  int insertBatch(@Param(\"list\") List<${modelClass.shortClassName}> entities);"
    out.println ""
    out.println "  int update(${modelClass.shortClassName} entity);"
    out.println ""
    out.println "  int deleteById(@Param(\"id\") Long id);"
    out.println ""
    out.println "  int deleteByIds(@Param(\"ids\") List<Long> ids);"
    out.println ""
    out.println "  List<${modelClass.shortClassName}> findByIds(@Param(\"ids\") List<Long> ids);"
    out.println ""
    out.println "  List<${modelClass.shortClassName}> findPage(@Param(\"offset\") int offset, @Param(\"limit\") int limit);"
    out.println ""
    out.println "  long count();"
    out.println ""
    out.println "  int existsById(@Param(\"id\") Long id);"
    out.println "}"
  }
}


