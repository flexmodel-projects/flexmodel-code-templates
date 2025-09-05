# Flexmodel Python SDK 模板

该模板提供访问 Flexmodel 后端的简易 Python SDK，支持直接访问实体类型，无需通用封装。

## 特性

- **直接实体访问**：`users = user_api.list_users()` 而不是 `ApiResponse[PageResult[Dict[str, Any]]]`
- **类型安全**：强类型实体类，运行时类型检查
- **便捷API**：为每个实体生成专门的API类
- **灵活查询**：支持分页、过滤、排序等查询参数
- **简单易用**：工厂模式创建客户端，一行代码开始使用
- **Pydantic支持**：使用Pydantic进行数据验证和序列化

## 快速开始

### 1. 安装依赖

```bash
pip install -e .
```

### 2. 创建客户端

```python
from src.client.flexmodel_client_factory import FlexmodelClientFactory

# 使用工厂方法创建客户端
client = FlexmodelClientFactory.create_client(
    'http://localhost:8080', 
    'myDatasource'
)

# 或者手动创建
from src.client.api_client import ApiClient
from src.client.flexmodel_client import FlexmodelClient

api_client = ApiClient('http://localhost:8080')
client = FlexmodelClient(api_client, 'myDatasource')
```

### 3. 直接访问实体

```python
from src.example.user_api import UserApi
from src.example.user import User

# 获取用户API（示例）
user_api = UserApi(client.api_client, 'myDatasource', 'user')

# 获取所有用户 - 直接返回User对象列表
users = user_api.list_users_simple()

# 带条件查询
active_users = user_api.list_users_as_list(
    filter_expr="status='active'",
    sort='name asc'
)

# 分页查询
user_page = user_api.list_users(current=1, page_size=10)

# 获取单个用户
user = user_api.get_user('123')

# 创建用户
new_user = User(
    name='张三',
    email='zhangsan@example.com',
    age=25
)
created_user = user_api.create_user(new_user)

# 更新用户
user.set_age(26)
updated_user = user_api.update_user(user.get_id(), user)

# 删除用户
user_api.delete_user(user.get_id())
```

### 4. 访问多个实体

```python
from src.example.product_api import ProductApi
from src.example.order_api import OrderApi

# 用户相关操作（示例）
user_api = UserApi(client.api_client, 'myDatasource', 'user')
users = user_api.list_users_simple()

# 产品相关操作（示例）
product_api = ProductApi(client.api_client, 'myDatasource', 'product')
products = product_api.list_products_simple()

# 订单相关操作（示例）
order_api = OrderApi(client.api_client, 'myDatasource', 'order')
orders = order_api.list_orders_simple()
```

## API 方法说明

每个实体API都提供以下方法：

- `list_{entity}s_simple()` - 获取所有记录（直接返回列表）
- `list_{entity}s_as_list(options)` - 带条件查询
- `list_{entity}s(options)` - 分页查询
- `get_{entity}(id)` - 根据ID获取记录
- `create_{entity}(entity)` - 创建记录
- `update_{entity}(id, entity)` - 更新记录
- `patch_{entity}(id, entity)` - 部分更新记录
- `delete_{entity}(id)` - 删除记录

## 通用API

如果需要使用通用API（返回Dict类型），可以使用：

```python
records_api = client.get_records_api()
records = records_api.list_records(
    'datasource', 'model', 
    current=1, page_size=10
)
```

## 配置

### 认证

```python
# API Key认证
client = FlexmodelClientFactory.create_client_with_api_key(
    'http://localhost:8080', 
    'myDatasource', 
    'your-api-key'
)

# 用户名密码认证
client = FlexmodelClientFactory.create_client_with_credentials(
    'http://localhost:8080', 
    'myDatasource', 
    'username', 
    'password'
)
```

### 开发环境

```python
# 使用默认配置（localhost:8080）
client = FlexmodelClientFactory.create_default_client('myDatasource')
```

## 实体生成

模板包含实体生成器，会根据模型定义自动生成：
- 实体类（如User.py）
- API类（如UserApi.py）

## 示例代码

所有示例代码都位于 `example` 包中：
- `example/user.py` - 用户实体示例
- `example/product.py` - 产品实体示例  
- `example/order.py` - 订单实体示例
- `example/user_api.py` - 用户API示例
- `example/product_api.py` - 产品API示例
- `example/order_api.py` - 订单API示例
- `example/flexmodel_example.py` - 完整使用示例

## 构建和开发

```bash
# 安装开发依赖
pip install -e ".[dev]"

# 代码格式化
black src/

# 代码检查
flake8 src/

# 类型检查
mypy src/

# 运行测试
pytest
```

## 依赖

- requests: HTTP客户端
- pydantic: 数据验证和序列化
- Python 3.8+

## 注意事项

- 示例代码位于 `example` 包中，实际使用时需要根据您的实体类型进行调整
- `FlexmodelClient` 中的便捷方法（如 `users()`, `products()` 等）是示例方法，需要根据实际生成的实体类型进行修改
- 需要Python 3.8+环境
- 使用Pydantic进行数据验证，确保数据类型的正确性
