"""
Product API - 直接访问Product实体（示例）
"""

from typing import List, Optional
from ..client.api_client import ApiClient
from ..api.page_result import PageResult
from .product import Product


class ProductApi:
    """Product API - 直接访问Product实体（示例）"""
    
    def __init__(self, api_client: ApiClient, datasource_name: str, model_name: str):
        """
        初始化Product API
        
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
    
    def list_products(
        self,
        current: Optional[int] = None,
        page_size: Optional[int] = None,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> PageResult[Product]:
        """获取所有Product记录（分页）"""
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
        products = [Product.from_dict(item) for item in data['list']]
        return PageResult(data['total'], products)
    
    def list_products_as_list(
        self,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> List[Product]:
        """获取所有Product记录（直接返回列表）"""
        page_result = self.list_products(
            filter_expr=filter_expr,
            nested_query=nested_query,
            sort=sort
        )
        return page_result.get_items()
    
    def list_products_simple(self) -> List[Product]:
        """获取所有Product记录（最简单的调用）"""
        return self.list_products_as_list()
    
    def get_product(
        self,
        product_id: str,
        nested_query: Optional[bool] = None
    ) -> Product:
        """根据ID获取Product记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(product_id)}"
        
        params = {}
        if nested_query is not None:
            params['nestedQuery'] = nested_query
        
        data = self.api_client.get(path, params)
        return Product.from_dict(data)
    
    def create_product(self, product: Product) -> Product:
        """创建新的Product记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records"
        data = self.api_client.post(path, product.to_dict())
        return Product.from_dict(data)
    
    def update_product(self, product_id: str, product: Product) -> Product:
        """更新Product记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(product_id)}"
        data = self.api_client.put(path, product.to_dict())
        return Product.from_dict(data)
    
    def patch_product(self, product_id: str, product: Product) -> Product:
        """部分更新Product记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(product_id)}"
        data = self.api_client.patch(path, product.to_dict())
        return Product.from_dict(data)
    
    def delete_product(self, product_id: str) -> None:
        """删除Product记录"""
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(product_id)}"
        self.api_client.delete(path)
