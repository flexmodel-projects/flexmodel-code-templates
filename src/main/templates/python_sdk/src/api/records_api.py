"""
通用Records API
"""

from typing import Dict, Any, Optional, List
from .page_result import PageResult
from ..client.api_client import ApiClient


class RecordsApi:
    """通用Records API"""
    
    def __init__(self, api_client: ApiClient):
        """
        初始化Records API
        
        Args:
            api_client: API客户端实例
        """
        self.api_client = api_client
    
    def _encode(self, text: str) -> str:
        """URL编码"""
        return text.replace('/', '%2F')
    
    def list_records(
        self,
        datasource_name: str,
        model_name: str,
        current: Optional[int] = None,
        page_size: Optional[int] = None,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> PageResult[Dict[str, Any]]:
        """
        获取记录列表（分页）
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            current: 当前页
            page_size: 页大小
            filter_expr: 过滤条件
            nested_query: 是否嵌套查询
            sort: 排序
            
        Returns:
            分页结果
        """
        path = f"/api/f/datasources/{self._encode(datasource_name)}/models/{self._encode(model_name)}/records"
        
        params = {}
        if current is not None:
            params['current'] = current
        if page_size is not None:
            params['pageSize'] = page_size
        if filter_expr is not None:
            params['filter'] = filter_expr
        if nested_query is not None:
            params['nestedQuery'] = nested_query
        if sort is not None:
            params['sort'] = sort
        
        data = self.api_client.get(path, params)
        return PageResult(data['total'], data['list'])
    
    def list_records_as_list(
        self,
        datasource_name: str,
        model_name: str,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> List[Dict[str, Any]]:
        """
        获取记录列表（直接返回列表）
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            filter_expr: 过滤条件
            nested_query: 是否嵌套查询
            sort: 排序
            
        Returns:
            记录列表
        """
        page_result = self.list_records(
            datasource_name, model_name, 
            filter_expr=filter_expr, 
            nested_query=nested_query, 
            sort=sort
        )
        return page_result.get_items()
    
    def create_record(
        self,
        datasource_name: str,
        model_name: str,
        data: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        创建记录
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            data: 记录数据
            
        Returns:
            创建的记录
        """
        path = f"/api/f/datasources/{self._encode(datasource_name)}/models/{self._encode(model_name)}/records"
        return self.api_client.post(path, data)
    
    def get_record(
        self,
        datasource_name: str,
        model_name: str,
        record_id: str,
        nested_query: Optional[bool] = None
    ) -> Dict[str, Any]:
        """
        获取单个记录
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            record_id: 记录ID
            nested_query: 是否嵌套查询
            
        Returns:
            记录数据
        """
        path = f"/api/f/datasources/{self._encode(datasource_name)}/models/{self._encode(model_name)}/records/{self._encode(record_id)}"
        
        params = {}
        if nested_query is not None:
            params['nestedQuery'] = nested_query
        
        return self.api_client.get(path, params)
    
    def update_record(
        self,
        datasource_name: str,
        model_name: str,
        record_id: str,
        data: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        更新记录
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            record_id: 记录ID
            data: 更新数据
            
        Returns:
            更新后的记录
        """
        path = f"/api/f/datasources/{self._encode(datasource_name)}/models/{self._encode(model_name)}/records/{self._encode(record_id)}"
        return self.api_client.put(path, data)
    
    def patch_record(
        self,
        datasource_name: str,
        model_name: str,
        record_id: str,
        data: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        部分更新记录
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            record_id: 记录ID
            data: 更新数据
            
        Returns:
            更新后的记录
        """
        path = f"/api/f/datasources/{self._encode(datasource_name)}/models/{self._encode(model_name)}/records/{self._encode(record_id)}"
        return self.api_client.patch(path, data)
    
    def delete_record(
        self,
        datasource_name: str,
        model_name: str,
        record_id: str
    ) -> None:
        """
        删除记录
        
        Args:
            datasource_name: 数据源名称
            model_name: 模型名称
            record_id: 记录ID
        """
        path = f"/api/f/datasources/{self._encode(datasource_name)}/models/{self._encode(model_name)}/records/{self._encode(record_id)}"
        self.api_client.delete(path)
