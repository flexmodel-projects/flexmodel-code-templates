"""
Flexmodel SDK 使用示例
展示如何直接访问实体而不是通用封装
"""

from typing import List
from ..client.flexmodel_client_factory import FlexmodelClientFactory
from ..client.flexmodel_client import FlexmodelClient
from .user_api import UserApi
from .product_api import ProductApi
from .order_api import OrderApi
from .user import User
from .product import Product
from .order import Order


class FlexmodelExample:
    """Flexmodel SDK 使用示例"""
    
    @staticmethod
    async def main():
        """主示例方法"""
        try:
            # 1. 创建API客户端
            client = FlexmodelClientFactory.create_client(
                'http://localhost:8080', 
                'myDatasource'
            )

            # 2. 直接访问User实体 - 这就是您想要的方式！
            user_api = UserApi(client.api_client, 'myDatasource', 'user')
            
            # 获取所有用户 - 直接返回User对象列表
            all_users: List[User] = user_api.list_users_simple()
            print(f'所有用户数量: {len(all_users)}')
            
            # 带条件查询用户
            active_users: List[User] = user_api.list_users_as_list(
                filter_expr="status='active'",
                sort='name asc'
            )
            print(f'活跃用户数量: {len(active_users)}')
            
            # 分页获取用户
            user_page = user_api.list_users(current=1, page_size=10)
            print(f'总用户数: {user_page.get_total()}')
            print(f'当前页用户: {user_page.get_size()}')
            
            # 3. 获取单个用户
            user: User = user_api.get_user('123')
            print(f'用户姓名: {user.get_name()}')
            
            # 4. 创建新用户
            new_user = User(
                name='张三',
                email='zhangsan@example.com',
                age=25
            )
            
            created_user: User = user_api.create_user(new_user)
            print(f'创建的用户ID: {created_user.get_id()}')
            
            # 5. 更新用户
            created_user.set_age(26)
            updated_user: User = user_api.update_user(created_user.get_id(), created_user)
            print(f'更新后的年龄: {updated_user.get_age()}')
            
            # 6. 删除用户
            if created_user.get_id():
                user_api.delete_user(created_user.get_id())
                print('用户已删除')
            
            # 7. 访问其他实体 - 同样的模式
            product_api = ProductApi(client.api_client, 'myDatasource', 'product')
            products: List[Product] = product_api.list_products_simple()
            print(f'产品数量: {len(products)}')
            
            order_api = OrderApi(client.api_client, 'myDatasource', 'order')
            orders: List[Order] = order_api.list_orders_simple()
            print(f'订单数量: {len(orders)}')
            
            # 8. 使用通用Records API（如果需要）
            records_api = client.get_records_api()
            generic_records = records_api.list_records(
                'myDatasource', 'user', 
                current=1, page_size=10
            )
            print(f'通用API获取的记录数: {generic_records.get_size()}')
            
        except Exception as error:
            print(f'示例执行失败: {str(error)}')
    
    @staticmethod
    async def business_example():
        """业务示例方法"""
        try:
            client = FlexmodelClientFactory.create_client(
                'http://localhost:8080', 
                'ecommerce'
            )
            
            # 业务场景：获取用户的所有订单
            user_api = UserApi(client.api_client, 'ecommerce', 'user')
            order_api = OrderApi(client.api_client, 'ecommerce', 'order')
            
            # 获取用户
            user: User = user_api.get_user('user123')
            
            # 获取该用户的订单 - 直接返回Order对象列表
            user_orders: List[Order] = order_api.list_orders_as_list(
                filter_expr=f"userId='{user.get_id()}'",
                sort='create_time desc'
            )
            
            # 处理订单
            for order in user_orders:
                print(f'订单ID: {order.get_id()}, 金额: {order.get_amount()}')
                
        except Exception as error:
            print(f'业务示例执行失败: {str(error)}')


# 如果直接运行此文件，执行示例
if __name__ == '__main__':
    import asyncio
    asyncio.run(FlexmodelExample.main())
