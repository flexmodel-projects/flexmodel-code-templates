"""
Order API - 直接访问Order实体（示例）
"""

from typing import List, Optional
from ..client.api_client import ApiClient
from ..api.page_result import PageResult
from .order import Order


class OrderApi:
    """Order API - 直接访问Order实体（示例）"""
    
    def __init__(self, api_client: ApiClient, datasource_name: str, model_name: str):
        """
        初始化Order API
        
        Args:
            api_client: API客户端实例
            datasource_name: 数据源名称
            model_name: 模型名称
        """
        self.api_client = api_client
        self.datasource_name = datasource_name
        self.model_name = model_name
    
    def _encode(self, text: str) -> str:
        """URL编码"""
        return text.replace('/', '%2F')
    
    def list_orders(
        self,
        current: Optional[int] = None,
        page_size: Optional[int] = None,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> PageResult[Order]:
        """获取所有Order记录（分页）"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records"
        
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
        orders = [Order.from_dict(item) for item in data['list']]
        return PageResult(data['total'], orders)
    
    def list_orders_as_list(
        self,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> List[Order]:
        """获取所有Order记录（直接返回列表）"""
        page_result = self.list_orders(
            filter_expr=filter_expr,
            nested_query=nested_query,
            sort=sort
        )
        return page_result.get_items()
    
    def list_orders_simple(self) -> List[Order]:
        """获取所有Order记录（最简单的调用）"""
        return self.list_orders_as_list()
    
    def get_order(
        self,
        order_id: str,
        nested_query: Optional[bool] = None
    ) -> Order:
        """根据ID获取Order记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(order_id)}"
        
        params = {}
        if nested_query is not None:
            params['nestedQuery'] = nested_query
        
        data = self.api_client.get(path, params)
        return Order.from_dict(data)
    
    def create_order(self, order: Order) -> Order:
        """创建新的Order记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records"
        data = self.api_client.post(path, order.to_dict())
        return Order.from_dict(data)
    
    def update_order(self, order_id: str, order: Order) -> Order:
        """更新Order记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(order_id)}"
        data = self.api_client.put(path, order.to_dict())
        return Order.from_dict(data)
    
    def patch_order(self, order_id: str, order: Order) -> Order:
        """部分更新Order记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(order_id)}"
        data = self.api_client.patch(path, order.to_dict())
        return Order.from_dict(data)
    
    def delete_order(self, order_id: str) -> None:
        """删除Order记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(order_id)}"
        self.api_client.delete(path)
