"""
Flexmodel客户端 - 提供对各个实体API的访问
"""

from typing import Type, TypeVar, Dict, Any
from .api_client import ApiClient
from ..api.records_api import RecordsApi

T = TypeVar('T')


class FlexmodelClient:
    """Flexmodel客户端 - 提供对各个实体API的访问"""
    
    def __init__(self, api_client: ApiClient, datasource_name: str):
        """
        初始化Flexmodel客户端
        
        Args:
            api_client: API客户端实例
            datasource_name: 数据源名称
        """
        self.api_client = api_client
        self.datasource_name = datasource_name
        self._entity_apis: Dict[str, Any] = {}
    
    def get_api(self, model_name: str, api_class: Type[T]) -> T:
        """
        获取指定模型的API实例
        
        Args:
            model_name: 模型名称
            api_class: API类
            
        Returns:
            API实例
        """
        key = f"{model_name}_{api_class.__name__}"
        
        if key not in self._entity_apis:
            api_instance = api_class(self.api_client, self.datasource_name, model_name)
            self._entity_apis[key] = api_instance
        
        return self._entity_apis[key]
    
    def get_records_api(self) -> RecordsApi:
        """获取通用Records API"""
        return RecordsApi(self.api_client)
    
    def users(self):
        """
        便捷方法：获取User API（示例）
        示例用法：users = client.users().list_users()
        注意：这些是示例方法，实际使用时需要根据您的实体类型进行调整
        """
        # 这里需要导入UserApi类
        # from ..example.user_api import UserApi
        # return self.get_api('user', UserApi)
        raise NotImplementedError('UserApi需要从example包中导入并实现')
    
    def products(self):
        """
        便捷方法：获取Product API（示例）
        示例用法：products = client.products().list_products()
        """
        # 这里需要导入ProductApi类
        # from ..example.product_api import ProductApi
        # return self.get_api('product', ProductApi)
        raise NotImplementedError('ProductApi需要从example包中导入并实现')
    
    def orders(self):
        """
        便捷方法：获取Order API（示例）
        示例用法：orders = client.orders().list_orders()
        """
        # 这里需要导入OrderApi类
        # from ..example.order_api import OrderApi
        # return self.get_api('order', OrderApi)
        raise NotImplementedError('OrderApi需要从example包中导入并实现')
