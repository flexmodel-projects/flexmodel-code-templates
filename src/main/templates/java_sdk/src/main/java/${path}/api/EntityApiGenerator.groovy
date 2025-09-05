import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class EntityApiGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName + 'Api.java').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def className = modelClass.shortClassName
    def variableName = className.toLowerCase()
    
    out.println "package ${modelClass.packageName};"
    out.println ""
    out.println "import com.fasterxml.jackson.core.type.TypeReference;"
    out.println "import java.util.HashMap;"
    out.println "import java.util.List;"
    out.println "import java.util.Map;"
    out.println "import ${modelClass.packageName}.client.ApiClient;"
    out.println ""
    out.println "/**"
    out.println " * ${className} API - 直接访问${className}实体"
    out.println " */"
    out.println "public class ${className}Api {"
    out.println "  private final ApiClient apiClient;"
    out.println "  private final String datasourceName;"
    out.println "  private final String modelName;"
    out.println ""
    out.println "  public ${className}Api(ApiClient apiClient, String datasourceName, String modelName) {"
    out.println "    this.apiClient = apiClient;"
    out.println "    this.datasourceName = datasourceName;"
    out.println "    this.modelName = modelName;"
    out.println "  }"
    out.println ""
    
    // 列出所有记录的方法
    out.println "  /**"
    out.println "   * 获取所有${className}记录（分页）"
    out.println "   */"
    out.println "  public PageResult<${className}> list${className}s(Integer current, Integer pageSize, String filter, Boolean nestedQuery, String sort) {"
    out.println "    String path = String.format(\"/api/f/datasources/%s/models/%s/records\", encode(datasourceName), encode(modelName));"
    out.println "    Map<String, Object> q = new HashMap<>();"
    out.println "    if (current != null) q.put(\"current\", current);"
    out.println "    if (pageSize != null) q.put(\"pageSize\", pageSize);"
    out.println "    if (filter != null) q.put(\"filter\", filter);"
    out.println "    if (nestedQuery != null) q.put(\"nestedQuery\", nestedQuery);"
    out.println "    if (sort != null) q.put(\"sort\", sort);"
    out.println ""
    out.println "    return apiClient.get(path, q, new TypeReference<PageResult<${className}>>() {});"
    out.println "  }"
    out.println ""
    
    // 直接返回列表的方法
    out.println "  /**"
    out.println "   * 获取所有${className}记录（直接返回列表）"
    out.println "   */"
    out.println "  public List<${className}> list${className}s(String filter, Boolean nestedQuery, String sort) {"
    out.println "    PageResult<${className}> pageResult = list${className}s(null, null, filter, nestedQuery, sort);"
    out.println "    return pageResult.getList();"
    out.println "  }"
    out.println ""
    
    // 获取单个记录
    out.println "  /**"
    out.println "   * 根据ID获取${className}记录"
    out.println "   */"
    out.println "  public ${className} get${className}(String id, Boolean nestedQuery) {"
    out.println "    String path = String.format(\"/api/f/datasources/%s/models/%s/records/%s\", encode(datasourceName), encode(modelName), encode(id));"
    out.println "    Map<String, Object> q = new HashMap<>();"
    out.println "    if (nestedQuery != null) q.put(\"nestedQuery\", nestedQuery);"
    out.println ""
    out.println "    return apiClient.get(path, q, new TypeReference<${className}>() {});"
    out.println "  }"
    out.println ""
    
    // 创建记录
    out.println "  /**"
    out.println "   * 创建新的${className}记录"
    out.println "   */"
    out.println "  public ${className} create${className}(${className} ${variableName}) {"
    out.println "    String path = String.format(\"/api/f/datasources/%s/models/%s/records\", encode(datasourceName), encode(modelName));"
    out.println "    return apiClient.post(path, ${variableName}, new TypeReference<${className}>() {});"
    out.println "  }"
    out.println ""
    
    // 更新记录
    out.println "  /**"
    out.println "   * 更新${className}记录"
    out.println "   */"
    out.println "  public ${className} update${className}(String id, ${className} ${variableName}) {"
    out.println "    String path = String.format(\"/api/f/datasources/%s/models/%s/records/%s\", encode(datasourceName), encode(modelName), encode(id));"
    out.println "    return apiClient.put(path, ${variableName}, new TypeReference<${className}>() {});"
    out.println "  }"
    out.println ""
    
    // 部分更新记录
    out.println "  /**"
    out.println "   * 部分更新${className}记录"
    out.println "   */"
    out.println "  public ${className} patch${className}(String id, ${className} ${variableName}) {"
    out.println "    String path = String.format(\"/api/f/datasources/%s/models/%s/records/%s\", encode(datasourceName), encode(modelName), encode(id));"
    out.println "    return apiClient.patch(path, ${variableName}, new TypeReference<${className}>() {});"
    out.println "  }"
    out.println ""
    
    // 删除记录
    out.println "  /**"
    out.println "   * 删除${className}记录"
    out.println "   */"
    out.println "  public void delete${className}(String id) {"
    out.println "    String path = String.format(\"/api/f/datasources/%s/models/%s/records/%s\", encode(datasourceName), encode(modelName), encode(id));"
    out.println "    apiClient.delete(path);"
    out.println "  }"
    out.println ""
    
    // 辅助方法
    out.println "  private String encode(String s) {"
    out.println "    return s.replace(\"/\", \"%2F\");"
    out.println "  }"
    out.println "}"
  }
}
