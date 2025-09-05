import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class PythonSdkEntityGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName.toLowerCase() + '.py').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def className = modelClass.shortClassName
    
    // 导入语句
    out.println '"""'
    out.println "${className}实体类"
    out.println '"""'
    out.println ""
    out.println "from typing import Optional"
    out.println "from pydantic import BaseModel, Field"
    out.println ""
    
    // 类定义
    out.println "class ${className}(BaseModel):"
    out.println "    \"\"\"${className}实体类\"\"\""
    out.println ""
    
    // 字段定义
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def fieldType = getPythonType(field.shortTypeName)
      def fieldDescription = field.comment ?: "${fieldName}字段"
      out.println "    ${fieldName}: Optional[${fieldType}] = Field(None, description=\"${fieldDescription}\")"
    }
    out.println ""
    
    // Pydantic配置
    out.println "    class Config:"
    out.println "        \"\"\"Pydantic配置\"\"\""
    out.println "        json_encoders = {"
    out.println "            # 可以在这里添加自定义编码器"
    out.println "        }"
    out.println ""
    
    // Getter和Setter方法
    modelClass.allFields.each { field ->
      def fieldName = field.variableName
      def fieldType = getPythonType(field.shortTypeName)
      def capitalizedName = fieldName.capitalize()
      
      // Getter方法
      out.println "    def get_${fieldName}(self) -> Optional[${fieldType}]:"
      out.println "        \"\"\"获取${fieldName}\"\"\""
      out.println "        return self.${fieldName}"
      out.println ""
      
      // Setter方法
      out.println "    def set_${fieldName}(self, ${fieldName}: Optional[${fieldType}]) -> None:"
      out.println "        \"\"\"设置${fieldName}\"\"\""
      out.println "        self.${fieldName} = ${fieldName}"
      out.println ""
    }
    
    // 类方法
    out.println "    @classmethod"
    out.println "    def from_dict(cls, data: dict) -> '${className}':"
    out.println "        \"\"\"从字典创建${className}实例\"\"\""
    out.println "        return cls(**data)"
    out.println ""
    out.println "    def to_dict(self) -> dict:"
    out.println "        \"\"\"转换为字典\"\"\""
    out.println "        return self.dict()"
  }
  
  /**
   * 将Java类型转换为Python类型
   */
  private String getPythonType(String javaType) {
    switch (javaType) {
      case 'String':
        return 'str'
      case 'Integer':
      case 'int':
        return 'int'
      case 'Long':
      case 'long':
        return 'int'
      case 'Double':
      case 'double':
        return 'float'
      case 'Float':
      case 'float':
        return 'float'
      case 'Boolean':
      case 'boolean':
        return 'bool'
      case 'BigDecimal':
        return 'Decimal'
      case 'LocalDateTime':
      case 'Date':
        return 'datetime'
      default:
        return 'str'  // 默认为字符串类型
    }
  }
}
