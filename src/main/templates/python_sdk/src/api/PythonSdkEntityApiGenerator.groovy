import tech.wetech.flexmodel.codegen.AbstractGenerator
import tech.wetech.flexmodel.codegen.GenerationContext
import tech.wetech.flexmodel.codegen.ModelClass

import java.nio.file.Path

class PythonSdkEntityApiGenerator extends AbstractGenerator {

  @Override
  String getTargetFile(GenerationContext context, String targetDirectory) {
    ModelClass modelClass = context.modelClass
    return Path.of(targetDirectory, modelClass.shortClassName.toLowerCase() + '_api.py').toString()
  }

  @Override
  void writeModel(PrintWriter out, GenerationContext context) {
    def modelClass = context.modelClass
    def className = modelClass.shortClassName
    def variableName = className.toLowerCase()
    
    // 导入语句
    out.println '"""'
    out.println "${className} API - 直接访问${className}实体"
    out.println '"""'
    out.println ""
    out.println "from typing import List, Optional"
    out.println "from ..client.api_client import ApiClient"
    out.println "from ..api.page_result import PageResult"
    out.println "from .${variableName} import ${className}"
    out.println ""
    
    // 类定义
    out.println "class ${className}Api:"
    out.println "    \"\"\"${className} API - 直接访问${className}实体\"\"\""
    out.println ""
    
    // 构造函数
    out.println "    def __init__(self, api_client: ApiClient, datasource_name: str, model_name: str):"
    out.println "        \"\"\""
    out.println "        初始化${className} API"
    out.println "        \"\"\""
    out.println "        self.api_client = api_client"
    out.println "        self.datasource_name = datasource_name"
    out.println "        self.model_name = model_name"
    out.println ""
    
    // 辅助方法
    out.println "    def _encode(self, text: str) -> str:"
    out.println "        \"\"\"URL编码\"\"\""
    out.println "        return text.replace('/', '%2F')"
    out.println ""
    
    // 分页查询方法
    out.println "    def list_${variableName}s("
    out.println "        self,"
    out.println "        current: Optional[int] = None,"
    out.println "        page_size: Optional[int] = None,"
    out.println "        filter_expr: Optional[str] = None,"
    out.println "        nested_query: Optional[bool] = None,"
    out.println "        sort: Optional[str] = None"
    out.println "    ) -> PageResult[${className}]:"
    out.println "        \"\"\""
    out.println "        获取所有${className}记录（分页）"
    out.println "        \"\"\""
    out.println "        path = f\"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records\""
    out.println "        "
    out.println "        params = {}"
    out.println "        if current is not None:"
    out.println "            params['current'] = current"
    out.println "        if page_size is not None:"
    out.println "            params['pageSize'] = page_size"
    out.println "        if filter_expr is not None:"
    out.println "            params['filter'] = filter_expr"
    out.println "        if nested_query is not None:"
    out.println "            params['nestedQuery'] = nested_query"
    out.println "        if sort is not None:"
    out.println "            params['sort'] = sort"
    out.println "        "
    out.println "        data = self.api_client.get(path, params)"
    out.println "        ${variableName}s = [${className}.from_dict(item) for item in data['list']]"
    out.println "        return PageResult(data['total'], ${variableName}s)"
    out.println ""
    
    // 直接返回列表方法
    out.println "    def list_${variableName}s_as_list("
    out.println "        self,"
    out.println "        filter_expr: Optional[str] = None,"
    out.println "        nested_query: Optional[bool] = None,"
    out.println "        sort: Optional[str] = None"
    out.println "    ) -> List[${className}]:"
    out.println "        \"\"\""
    out.println "        获取所有${className}记录（直接返回列表）"
    out.println "        这是您想要的方法：${variableName}s = ${variableName}_api.list_${variableName}s_as_list()"
    out.println "        \"\"\""
    out.println "        page_result = self.list_${variableName}s("
    out.println "            filter_expr=filter_expr,"
    out.println "            nested_query=nested_query,"
    out.println "            sort=sort"
    out.println "        )"
    out.println "        return page_result.get_items()"
    out.println ""
    
    // 简单查询方法
    out.println "    def list_${variableName}s_simple(self) -> List[${className}]:"
    out.println "        \"\"\""
    out.println "        获取所有${className}记录（最简单的调用）"
    out.println "        \"\"\""
    out.println "        return self.list_${variableName}s_as_list()"
    out.println ""
    
    // 获取单个记录方法
    out.println "    def get_${variableName}("
    out.println "        self,"
    out.println "        ${variableName}_id: str,"
    out.println "        nested_query: Optional[bool] = None"
    out.println "    ) -> ${className}:"
    out.println "        \"\"\""
    out.println "        根据ID获取${className}记录"
    out.println "        \"\"\""
    out.println "        path = f\"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(${variableName}_id)}\""
    out.println "        "
    out.println "        params = {}"
    out.println "        if nested_query is not None:"
    out.println "            params['nestedQuery'] = nested_query"
    out.println "        "
    out.println "        data = self.api_client.get(path, params)"
    out.println "        return ${className}.from_dict(data)"
    out.println ""
    
    // 创建记录方法
    out.println "    def create_${variableName}(self, ${variableName}: ${className}) -> ${className}:"
    out.println "        \"\"\""
    out.println "        创建新的${className}记录"
    out.println "        \"\"\""
    out.println "        path = f\"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records\""
    out.println "        data = self.api_client.post(path, ${variableName}.to_dict())"
    out.println "        return ${className}.from_dict(data)"
    out.println ""
    
    // 更新记录方法
    out.println "    def update_${variableName}(self, ${variableName}_id: str, ${variableName}: ${className}) -> ${className}:"
    out.println "        \"\"\""
    out.println "        更新${className}记录"
    out.println "        \"\"\""
    out.println "        path = f\"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(${variableName}_id)}\""
    out.println "        data = self.api_client.put(path, ${variableName}.to_dict())"
    out.println "        return ${className}.from_dict(data)"
    out.println ""
    
    // 部分更新记录方法
    out.println "    def patch_${variableName}(self, ${variableName}_id: str, ${variableName}: ${className}) -> ${className}:"
    out.println "        \"\"\""
    out.println "        部分更新${className}记录"
    out.println "        \"\"\""
    out.println "        path = f\"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(${variableName}_id)}\""
    out.println "        data = self.api_client.patch(path, ${variableName}.to_dict())"
    out.println "        return ${className}.from_dict(data)"
    out.println ""
    
    // 删除记录方法
    out.println "    def delete_${variableName}(self, ${variableName}_id: str) -> None:"
    out.println "        \"\"\""
    out.println "        删除${className}记录"
    out.println "        \"\"\""
    out.println "        path = f\"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(${variableName}_id)}\""
    out.println "        self.api_client.delete(path)"
  }
}
