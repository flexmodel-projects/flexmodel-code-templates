# Flexmodel Java SDK 模板

该模板提供访问 Flexmodel 后端的简易 Java SDK，支持直接访问实体类型，无需通用封装。

## 特性

- **直接实体访问**：`List<User> listUsers()` 而不是 `ApiResponse<PageResult<Map<String, Object>>>`
- **类型安全**：强类型实体类，编译时类型检查
- **便捷API**：为每个实体生成专门的API类
- **灵活查询**：支持分页、过滤、排序等查询参数
- **简单易用**：工厂模式创建客户端，一行代码开始使用

## 快速开始

### 1. 创建客户端

```java
// 使用工厂方法创建客户端
FlexmodelClient client = FlexmodelClientFactory.createClient(
    "http://localhost:8080", 
    "myDatasource"
);

// 或者手动创建
ApiClient apiClient = new ApiClient("http://localhost:8080");
FlexmodelClient client = new FlexmodelClient(apiClient, "myDatasource");
```

### 2. 直接访问实体

```java
// 获取用户API（示例）
UserApi userApi = client.users();

// 获取所有用户 - 直接返回List<User>
List<User> users = userApi.listUsers();

// 带条件查询
List<User> activeUsers = userApi.listUsers("status='active'", null, "name asc");

// 分页查询
PageResult<User> userPage = userApi.listUsers(1, 10, null, null, null);

// 获取单个用户
User user = userApi.getUser("123");

// 创建用户
User newUser = new User();
newUser.setName("张三");
newUser.setEmail("zhangsan@example.com");
User createdUser = userApi.createUser(newUser);

// 更新用户
user.setAge(26);
User updatedUser = userApi.updateUser(user.getId(), user);

// 删除用户
userApi.deleteUser(user.getId());
```

### 3. 访问多个实体

```java
// 用户相关操作（示例）
UserApi userApi = client.users();
List<User> users = userApi.listUsers();

// 产品相关操作（示例）
ProductApi productApi = client.products();
List<Product> products = productApi.listProducts();

// 订单相关操作（示例）
OrderApi orderApi = client.orders();
List<Order> orders = orderApi.listOrders();
```

## API 方法说明

每个实体API都提供以下方法：

- `list{Entity}s()` - 获取所有记录（直接返回列表）
- `list{Entity}s(filter, nestedQuery, sort)` - 带条件查询
- `list{Entity}s(current, pageSize, filter, nestedQuery, sort)` - 分页查询
- `get{Entity}(id)` - 根据ID获取记录
- `create{Entity}(entity)` - 创建记录
- `update{Entity}(id, entity)` - 更新记录
- `patch{Entity}(id, entity)` - 部分更新记录
- `delete{Entity}(id)` - 删除记录

## 通用API

如果需要使用通用API（返回Map类型），可以使用：

```java
RecordsApi recordsApi = client.getRecordsApi();
PageResult<Map<String, Object>> records = recordsApi.listRecords(
    "datasource", "model", 1, 10, null, null, null
);
```

## 配置

### 认证

```java
// API Key认证
FlexmodelClient client = FlexmodelClientFactory.createClient(
    "http://localhost:8080", 
    "myDatasource", 
    "your-api-key"
);

// 用户名密码认证
FlexmodelClient client = FlexmodelClientFactory.createClient(
    "http://localhost:8080", 
    "myDatasource", 
    "username", 
    "password"
);
```

### 开发环境

```java
// 使用默认配置（localhost:8080）
FlexmodelClient client = FlexmodelClientFactory.createDefaultClient("myDatasource");
```

## 实体生成

模板包含实体生成器，会根据模型定义自动生成：
- 实体类（如User.java）
- API类（如UserApi.java）

## 示例代码

所有示例代码都位于 `example` 包中：
- `example.User.java` - 用户实体示例
- `example.Product.java` - 产品实体示例  
- `example.Order.java` - 订单实体示例
- `example.UserApi.java` - 用户API示例
- `example.ProductApi.java` - 产品API示例
- `example.OrderApi.java` - 订单API示例
- `example.FlexmodelExample.java` - 完整使用示例

## 依赖

确保在pom.xml中设置正确的groupId、artifactId和version占位变量。


