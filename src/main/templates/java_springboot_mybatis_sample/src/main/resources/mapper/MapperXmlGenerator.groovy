import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class MapperXmlGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    return Path.of(targetDirectory, 'resources', 'mapper', context.modelClass.shortClassName + 'Mapper.xml').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def baseColumns = modelClass.allFields.collect { it.original.name }.join(', ')
    def tableName = modelClass.shortClassName.toLowerCase()
    out.println "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
    out.println "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">"
    out.println "<mapper namespace=\"${modelClass.packageName}.mapper.${modelClass.shortClassName}Mapper\">"
    out.println "  <resultMap id=\"ResultMap\" type=\"${modelClass.packageName}.${modelClass.shortClassName}\">"
    modelClass.allFields.each { field ->
      if (field.isIdentity()) {
        out.println "    <id column=\"${field.original.name}\" property=\"${field.variableName}\" />"
      } else {
        out.println "    <result column=\"${field.original.name}\" property=\"${field.variableName}\" />"
      }
    }
    out.println "  </resultMap>"
    out.println "  <sql id=\"Base_Column_List\">"
    out.println "    ${baseColumns}"
    out.println "  </sql>"
    out.println ""
    out.println "  <select id=\"findAll\" resultMap=\"ResultMap\">"
    out.println "    SELECT"
    out.println "      <include refid=\"Base_Column_List\"/>"
    out.println "    FROM ${tableName}"
    out.println "    ORDER BY id"
    out.println "  </select>"
    out.println ""
    out.println "  <select id=\"findById\" parameterType=\"long\" resultMap=\"ResultMap\">"
    out.println "    SELECT"
    out.println "      <include refid=\"Base_Column_List\"/>"
    out.println "    FROM ${tableName}"
    out.println "    WHERE id = #{id}"
    out.println "  </select>"
    out.println ""
    def nonIdFields = modelClass.allFields.findAll{ !it.isIdentity() }
    def insertColumns = nonIdFields.collect{ it.original.name }.join(', ')
    def insertValues = nonIdFields.collect{ '#{' + it.variableName + '}' }.join(', ')
    def updateSets = nonIdFields.collect{ it.original.name + ' = #{' + it.variableName + '}' }.join(', ')
    out.println "  <insert id=\"insert\" parameterType=\"${modelClass.packageName}.entity.${modelClass.shortClassName}\" useGeneratedKeys=\"true\" keyProperty=\"id\">"
    out.println "    INSERT INTO ${tableName} ("
    out.println "      ${insertColumns}"
    out.println "    ) VALUES ("
    out.println "      ${insertValues}"
    out.println "    )"
    out.println "  </insert>"
    out.println ""
    out.println "  <update id=\"update\" parameterType=\"${modelClass.packageName}.entity.${modelClass.shortClassName}\">"
    out.println "    UPDATE ${tableName}"
    out.println "    SET ${updateSets}"
    out.println "    WHERE id = #{id}"
    out.println "  </update>"
    out.println ""
    out.println "  <delete id=\"deleteById\" parameterType=\"long\">"
    out.println "    DELETE FROM ${tableName} WHERE id = #{id}"
    out.println "  </delete>"
    out.println ""
    out.println "  <delete id=\"deleteByIds\" parameterType=\"list\">"
    out.println "    DELETE FROM ${tableName} WHERE id IN"
    out.println "    <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">"
    out.println "      #{id}"
    out.println "    </foreach>"
    out.println "  </delete>"
    out.println ""
    out.println "  <insert id=\"insertBatch\" parameterType=\"list\" useGeneratedKeys=\"true\" keyProperty=\"id\">"
    out.println "    INSERT INTO ${tableName} ("
    out.println "      ${insertColumns}"
    out.println "    ) VALUES"
    out.println "    <foreach collection=\"list\" item=\"item\" separator=\",\">"
    out.println "      ("
    out.println nonIdFields.collect{ '        #{item.' + it.variableName + '}' }.join(',\n')
    out.println "      )"
    out.println "    </foreach>"
    out.println "  </insert>"
    out.println ""
    out.println "  <select id=\"findByIds\" resultMap=\"ResultMap\">"
    out.println "    SELECT"
    out.println "      <include refid=\"Base_Column_List\"/>"
    out.println "    FROM ${tableName}"
    out.println "    WHERE id IN"
    out.println "    <foreach collection=\"ids\" item=\"id\" open=\"(\" separator=\",\" close=\")\">"
    out.println "      #{id}"
    out.println "    </foreach>"
    out.println "    ORDER BY id"
    out.println "  </select>"
    out.println ""
    out.println "  <select id=\"findPage\" resultMap=\"ResultMap\">"
    out.println "    SELECT"
    out.println "      <include refid=\"Base_Column_List\"/>"
    out.println "    FROM ${tableName}"
    out.println "    ORDER BY id"
    out.println "    LIMIT #{limit} OFFSET #{offset}"
    out.println "  </select>"
    out.println ""
    out.println "  <select id=\"count\" resultType=\"long\">"
    out.println "    SELECT COUNT(1) FROM ${tableName}"
    out.println "  </select>"
    out.println ""
    out.println "  <select id=\"existsById\" parameterType=\"long\" resultType=\"int\">"
    out.println "    SELECT COUNT(1) FROM ${tableName} WHERE id = #{id}"
    out.println "  </select>"
    out.println "</mapper>"
  }
}


