"""
Flexmodel Python SDK

直接访问实体类型，无需通用封装
"""

__version__ = "${version}"
__author__ = "${author}"

# 核心客户端
from .client.api_client import ApiClient, ApiException
from .client.flexmodel_client import FlexmodelClient
from .client.flexmodel_client_factory import FlexmodelClientFactory

# API类
from .api.records_api import RecordsApi
from .api.page_result import PageResult

# 示例类（可选导入）
from .example.user import User
from .example.product import Product
from .example.order import Order
from .example.user_api import UserApi
from .example.product_api import ProductApi
from .example.order_api import OrderApi

__all__ = [
    # 版本信息
    "__version__",
    "__author__",
    
    # 核心客户端
    "ApiClient",
    "ApiException",
    "FlexmodelClient",
    "FlexmodelClientFactory",
    
    # API类
    "RecordsApi",
    "PageResult",
    
    # 示例类
    "User",
    "Product", 
    "Order",
    "UserApi",
    "ProductApi",
    "OrderApi",
]
