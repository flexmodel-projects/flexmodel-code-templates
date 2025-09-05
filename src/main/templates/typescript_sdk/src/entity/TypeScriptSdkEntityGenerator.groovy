import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class TypeScriptSdkEntityGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + '.ts').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def className = modelClass.shortClassName
    
    // 文档注释
    out.println "/**"
    out.println " * ${className}实体类"
    out.println " */"
    out.println ""
    
    // 导入语句
    out.println "import { BaseModel } from 'pydantic';"
    out.println "import { Field } from 'pydantic';"
    out.println ""
    
    // 接口定义（可选，用于类型检查）
    out.println "export interface I${className} {"
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def fieldType = getTypeScriptType(field.shortTypeName)
      out.println "  ${fieldName}?: ${fieldType};"
    }
    out.println "}"
    out.println ""
    
    // 类定义
    out.println "export class ${className} implements I${className} {"
    
    // 字段定义
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def fieldType = getTypeScriptType(field.shortTypeName)
      out.println "  private ${fieldName}: ${fieldType} | null;"
    }
    out.println ""
    
    // 构造函数
    out.println "  constructor(data: Partial<I${className}> = {}) {"
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      out.println "    this.${fieldName} = data.${fieldName} || null;"
    }
    out.println "  }"
    out.println ""
    
    // Getter方法
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def fieldType = getTypeScriptType(field.shortTypeName)
      def capitalizedName = fieldName.capitalize()
      out.println "  get${capitalizedName}(): ${fieldType} | null {"
      out.println "    return this.${fieldName};"
      out.println "  }"
      out.println ""
    }
    
    // Setter方法
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def fieldType = getTypeScriptType(field.shortTypeName)
      def capitalizedName = fieldName.capitalize()
      out.println "  set${capitalizedName}(${fieldName}: ${fieldType} | null): void {"
      out.println "    this.${fieldName} = ${fieldName};"
      out.println "  }"
      out.println ""
    }
    
    // 转换为JSON方法
    out.println "  /**"
    out.println "   * 转换为JSON对象"
    out.println "   */"
    out.println "  toJSON(): Record<string, any> {"
    out.println "    return {"
    modelClass.allFields.eachWithIndex { field, index ->
      def fieldName = field.variableName
      if (index < modelClass.allFields.size() - 1) {
        out.println "      ${fieldName}: this.${fieldName},"
      } else {
        out.println "      ${fieldName}: this.${fieldName}"
      }
    }
    out.println "    };"
    out.println "  }"
    out.println ""
    
    // 从JSON创建实例的静态方法
    out.println "  /**"
    out.println "   * 从JSON对象创建${className}实例"
    out.println "   */"
    out.println "  static fromJSON(data: Record<string, any>): ${className} {"
    out.println "    return new ${className}(data);"
    out.println "  }"
    out.println "}"
  }
  
  /**
   * 将Java类型转换为TypeScript类型
   */
  private String getTypeScriptType(String javaType) {
    switch (javaType) {
      case 'String':
        return 'string'
      case 'Integer':
      case 'int':
        return 'number'
      case 'Long':
      case 'long':
        return 'number'
      case 'Double':
      case 'double':
        return 'number'
      case 'Float':
      case 'float':
        return 'number'
      case 'Boolean':
      case 'boolean':
        return 'boolean'
      case 'BigDecimal':
        return 'number'
      case 'LocalDateTime':
      case 'Date':
        return 'Date'
      default:
        return 'string'  // 默认为字符串类型
    }
  }
}
