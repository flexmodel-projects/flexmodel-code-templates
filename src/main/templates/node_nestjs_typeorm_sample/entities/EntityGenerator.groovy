import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class EntityGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of('src', 'entities', modelClass.shortClassName + '.ts').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    out.println "import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm'"
    out.println ""
    out.println "@Entity()"
    out.println "export class ${modelClass.shortClassName} {"
    modelClass.allFields.each { field ->
      if (field.isIdentity()) {
        out.println "  @PrimaryGeneratedColumn()"
        out.println "  ${field.variableName}: number"
      } else {
        out.println "  @Column()"
        out.println "  ${field.variableName}: ${mapTsType(field.shortTypeName)}"
      }
    }
    out.println "}"
  }

  static String mapTsType(String javaShortType) {
    switch (javaShortType) {
      case 'Long':
      case 'Integer':
      case 'Short':
      case 'Byte':
      case 'Double':
      case 'Float':
        return 'number'
      case 'Boolean':
        return 'boolean'
      case 'String':
      default:
        return 'string'
    }
  }
}
