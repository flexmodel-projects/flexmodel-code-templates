import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class JavaScriptSdkEntityGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + '.js').toString()
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
    
    // 类定义
    out.println "export class ${className} {"
    out.println "  constructor(data = {}) {"
    
    // 字段初始化
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      out.println "    this.${fieldName} = data.${fieldName} || null;"
    }
    out.println "  }"
    out.println ""
    
    // Getter方法
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def capitalizedName = fieldName.capitalize()
      out.println "  get${capitalizedName}() {"
      out.println "    return this.${fieldName};"
      out.println "  }"
      out.println ""
    }
    
    // Setter方法
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def capitalizedName = fieldName.capitalize()
      out.println "  set${capitalizedName}(${fieldName}) {"
      out.println "    this.${fieldName} = ${fieldName};"
      out.println "  }"
      out.println ""
    }
    
    // 转换为JSON方法
    out.println "  /**"
    out.println "   * 转换为JSON对象"
    out.println "   */"
    out.println "  toJSON() {"
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
    out.println "  static fromJSON(data) {"
    out.println "    return new ${className}(data);"
    out.println "  }"
    out.println "}"
  }
}
