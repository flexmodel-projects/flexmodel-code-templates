import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class NodeEntityGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + '.ts').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def imports = [] as Set<String>
    def relationFields = []
    def regularFields = []

    // 收集导入和分类字段
    modelClass.allFields.each { field ->
      if (field.isIdentity()) {
        imports.add("PrimaryGeneratedColumn")
        regularFields << field
      } else if (field.isRelationField()) {
        // 处理关系字段
        def relationType = getRelationType(field)
        def targetEntity = getTargetEntity(field)

        switch(relationType) {
          case "OneToOne":
            imports.add("OneToOne")
            imports.add("JoinColumn")
            relationFields << [field: field, type: "OneToOne", target: targetEntity]
            break
          case "OneToMany":
            imports.add("OneToMany")
            imports.add("JoinColumn")
            relationFields << [field: field, type: "OneToMany", target: targetEntity]
            break
          case "ManyToOne":
            imports.add("ManyToOne")
            imports.add("JoinColumn")
            relationFields << [field: field, type: "ManyToOne", target: targetEntity]
            break
          case "ManyToMany":
            imports.add("ManyToMany")
            imports.add("JoinTable")
            relationFields << [field: field, type: "ManyToMany", target: targetEntity]
            break
        }
      } else {
        imports.add("Column")
        regularFields << field
      }
    }

    // 输出导入语句
    out.println "import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm'"
    if (imports.contains("OneToOne")) out.println "import { OneToOne, JoinColumn } from 'typeorm'"
    if (imports.contains("OneToMany")) out.println "import { OneToMany, JoinColumn } from 'typeorm'"
    if (imports.contains("ManyToOne")) out.println "import { ManyToOne, JoinColumn } from 'typeorm'"
    if (imports.contains("ManyToMany")) out.println "import { ManyToMany, JoinTable } from 'typeorm'"
    out.println ""

    // 输出实体类
    out.println "@Entity()"
    out.println "export class ${modelClass.shortClassName} {"

    // 输出普通字段
    regularFields.each { field ->
      if (field.isIdentity()) {
        if (field.shortTypeName == 'String') {
          out.println "  @PrimaryGeneratedColumn('uuid')"
          out.println "  ${field.variableName}: string;"
        } else {
          out.println "  @PrimaryGeneratedColumn()"
          out.println "  ${field.variableName}: ${mapTsType(field.shortTypeName)};"
        }
      } else {
        out.println "  @Column()"
        out.println "  ${field.variableName}: ${mapTsType(field.shortTypeName)};"
      }
    }

    // 输出关系字段
    relationFields.each { relation ->
      def field = relation.field
      def type = relation.type
      def target = relation.target
      def localField = getLocalField(field)
      def foreignField = getForeignField(field)
      def cascadeDelete = isCascadeDelete(field)
      
      switch(type) {
        case "OneToOne":
          def cascadeOptions = cascadeDelete ? "{ cascade: true }" : "{}"
          out.println "  @OneToOne(() => ${target}, ${cascadeOptions})"
          out.println "  @JoinColumn({ name: '${localField}_id' })"
          out.println "  ${field.variableName}: ${target};"
          break
        case "OneToMany":
          out.println "  @OneToMany(() => ${target}, ${target.toLowerCase()} => ${target.toLowerCase()}.${localField})"
          out.println "  ${field.variableName}: ${target}[];"
          break
        case "ManyToOne":
          out.println "  @ManyToOne(() => ${target}, ${target.toLowerCase()} => ${target.toLowerCase()}.${localField})"
          out.println "  @JoinColumn({ name: '${localField}_id' })"
          out.println "  ${field.variableName}: ${target};"
          break
        case "ManyToMany":
          out.println "  @ManyToMany(() => ${target})"
          out.println "  @JoinTable()"
          out.println "  ${field.variableName}: ${target}[];"
          break
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

  /**
   * 获取关系类型
   */
  private static String getRelationType(field) {
    def relationField = field.original
    if (relationField?.multiple) {
      return "OneToMany"
    } else {
      return "ManyToOne"
    }
  }

  /**
   * 获取目标实体名称
   */
  private static String getTargetEntity(field) {
    def relationField = field.original
    return relationField?.from ?: field.shortTypeName
  }

  /**
   * 获取本地字段名称
   */
  private static String getLocalField(field) {
    def relationField = field.original
    return relationField?.localField ?: field.variableName
  }

  /**
   * 获取外键字段名称
   */
  private static String getForeignField(field) {
    def relationField = field.original
    return relationField?.foreignField ?: "id"
  }

  /**
   * 是否级联删除
   */
  private static boolean isCascadeDelete(field) {
    def relationField = field.original
    return relationField?.cascadeDelete ?: false
  }
}
