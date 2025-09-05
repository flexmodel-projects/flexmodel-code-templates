import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class JavaScriptSdkEntityApiGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + 'Api.js').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def className = modelClass.shortClassName
    def variableName = className.toLowerCase()
    
    // 导入语句
    out.println "import { ${className} } from './${className}.js';"
    out.println "import { PageResult } from '../api/PageResult.js';"
    out.println ""
    
    // 文档注释
    out.println "/**"
    out.println " * ${className} API - 直接访问${className}实体"
    out.println " */"
    out.println ""
    
    // 类定义
    out.println "export class ${className}Api {"
    out.println "  constructor(apiClient, datasourceName, modelName) {"
    out.println "    this.apiClient = apiClient;"
    out.println "    this.datasourceName = datasourceName;"
    out.println "    this.modelName = modelName;"
    out.println "  }"
    out.println ""
    
    // 辅助方法
    out.println "  encode(str) {"
    out.println "    return str.replace(/\\//g, '%2F');"
    out.println "  }"
    out.println ""
    
    // 分页查询方法
    out.println "  /**"
    out.println "   * 获取所有${className}记录（分页）"
    out.println "   */"
    out.println "  async list${className}s(options = {}) {"
    out.println "    const {"
    out.println "      current = null,"
    out.println "      pageSize = null,"
    out.println "      filter = null,"
    out.println "      nestedQuery = null,"
    out.println "      sort = null"
    out.println "    } = options;"
    out.println ""
    out.println "    const path = `/api/f/datasources/\${this.encode(this.datasourceName)}/models/\${this.encode(this.modelName)}/records`;"
    out.println "    const params = {};"
    out.println "    "
    out.println "    if (current !== null) params.current = current;"
    out.println "    if (pageSize !== null) params.pageSize = pageSize;"
    out.println "    if (filter !== null) params.filter = filter;"
    out.println "    if (nestedQuery !== null) params.nestedQuery = nestedQuery;"
    out.println "    if (sort !== null) params.sort = sort;"
    out.println ""
    out.println "    const data = await this.apiClient.get(path, params);"
    out.println "    const ${variableName}s = data.list.map(item => ${className}.fromJSON(item));"
    out.println "    return new PageResult(data.total, ${variableName}s);"
    out.println "  }"
    out.println ""
    
    // 直接返回列表方法
    out.println "  /**"
    out.println "   * 获取所有${className}记录（直接返回列表）"
    out.println "   * 这是您想要的方法：const ${variableName}s = await ${variableName}Api.list${className}sAsList();"
    out.println "   */"
    out.println "  async list${className}sAsList(options = {}) {"
    out.println "    const pageResult = await this.list${className}s(options);"
    out.println "    return pageResult.getList();"
    out.println "  }"
    out.println ""
    
    // 简单查询方法
    out.println "  /**"
    out.println "   * 获取所有${className}记录（最简单的调用）"
    out.println "   */"
    out.println "  async list${className}sSimple() {"
    out.println "    return await this.list${className}sAsList();"
    out.println "  }"
    out.println ""
    
    // 获取单个记录方法
    out.println "  /**"
    out.println "   * 根据ID获取${className}记录"
    out.println "   */"
    out.println "  async get${className}(id, options = {}) {"
    out.println "    const { nestedQuery = null } = options;"
    out.println "    const path = `/api/f/datasources/\${this.encode(this.datasourceName)}/models/\${this.encode(this.modelName)}/records/\${this.encode(id)}`;"
    out.println "    const params = {};"
    out.println "    "
    out.println "    if (nestedQuery !== null) params.nestedQuery = nestedQuery;"
    out.println ""
    out.println "    const data = await this.apiClient.get(path, params);"
    out.println "    return ${className}.fromJSON(data);"
    out.println "  }"
    out.println ""
    
    // 创建记录方法
    out.println "  /**"
    out.println "   * 创建新的${className}记录"
    out.println "   */"
    out.println "  async create${className}(${variableName}) {"
    out.println "    const path = `/api/f/datasources/\${this.encode(this.datasourceName)}/models/\${this.encode(this.modelName)}/records`;"
    out.println "    const data = await this.apiClient.post(path, ${variableName}.toJSON());"
    out.println "    return ${className}.fromJSON(data);"
    out.println "  }"
    out.println ""
    
    // 更新记录方法
    out.println "  /**"
    out.println "   * 更新${className}记录"
    out.println "   */"
    out.println "  async update${className}(id, ${variableName}) {"
    out.println "    const path = `/api/f/datasources/\${this.encode(this.datasourceName)}/models/\${this.encode(this.modelName)}/records/\${this.encode(id)}`;"
    out.println "    const data = await this.apiClient.put(path, ${variableName}.toJSON());"
    out.println "    return ${className}.fromJSON(data);"
    out.println "  }"
    out.println ""
    
    // 部分更新记录方法
    out.println "  /**"
    out.println "   * 部分更新${className}记录"
    out.println "   */"
    out.println "  async patch${className}(id, ${variableName}) {"
    out.println "    const path = `/api/f/datasources/\${this.encode(this.datasourceName)}/models/\${this.encode(this.modelName)}/records/\${this.encode(id)}`;"
    out.println "    const data = await this.apiClient.patch(path, ${variableName}.toJSON());"
    out.println "    return ${className}.fromJSON(data);"
    out.println "  }"
    out.println ""
    
    // 删除记录方法
    out.println "  /**"
    out.println "   * 删除${className}记录"
    out.println "   */"
    out.println "  async delete${className}(id) {"
    out.println "    const path = `/api/f/datasources/\${this.encode(this.datasourceName)}/models/\${this.encode(this.modelName)}/records/\${this.encode(id)}`;"
    out.println "    await this.apiClient.delete(path);"
    out.println "  }"
    out.println "}"
  }
}
