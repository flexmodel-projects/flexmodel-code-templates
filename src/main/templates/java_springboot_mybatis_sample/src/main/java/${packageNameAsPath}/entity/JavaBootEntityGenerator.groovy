import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class JavaBootEntityGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory,  modelClass.shortClassName + '.java').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    out.println "package ${modelClass.packageName}.entity;"
    out.println ""
    out.println "public class ${modelClass.shortClassName} {"
    modelClass.allFields.each { field ->
      out.println "  private ${field.shortTypeName} ${field.variableName};"
    }
    modelClass.allFields.each { field ->
      out.println ""
      out.println "  public ${field.shortTypeName} get${field.variableName.capitalize()}() {"
      out.println "    return ${field.variableName};"
      out.println "  }"
      out.println ""
      out.println "  public void set${field.variableName.capitalize()}(${field.shortTypeName} ${field.variableName}) {"
      out.println "    this.${field.variableName} = ${field.variableName};"
      out.println "  }"
    }
    out.println "}"
  }
}


