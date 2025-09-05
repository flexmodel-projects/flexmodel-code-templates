# Java SDK 模板使用说明

## 模板结构

```
src/main/templates/java_sdk/
├── src/main/java/${path}/
│   ├── api/
│   │   ├── RecordsApi.java              # 通用API（已修改为直接返回数据）
│   │   └── EntityApiGenerator.groovy    # 实体API生成器
│   ├── client/
│   │   ├── ApiClient.java               # 基础API客户端
│   │   ├── ApiResponse.java             # API响应包装类
│   │   ├── FlexmodelClient.java         # 主客户端类
│   │   └── FlexmodelClientFactory.java  # 客户端工厂
│   ├── entity/
│   │   └── JavaSdkEntityGenerator.groovy # 实体生成器
│   └── example/                         # 示例代码包
│       ├── User.java                    # 用户实体示例
│       ├── Product.java                 # 产品实体示例
│       ├── Order.java                   # 订单实体示例
│       ├── UserApi.java                 # 用户API示例
│       ├── ProductApi.java              # 产品API示例
│       ├── OrderApi.java                # 订单API示例
│       └── FlexmodelExample.java        # 完整使用示例
├── pom.xml                              # Maven配置
├── README.md                            # 使用文档
└── TEMPLATE_USAGE.md                    # 模板使用说明
```

## 主要改进

### 1. 直接实体访问
**之前**：
```java
ApiResponse<PageResult<Map<String, Object>>> response = recordsApi.listRecords(...);
List<Map<String, Object>> records = response.getData().getList();
```

**现在**：
```java
List<User> users = userApi.listUsers();
```

### 2. 类型安全
- 强类型实体类，编译时类型检查
- 自动补全和重构支持
- 减少运行时错误

### 3. 便捷API
- 每个实体都有专门的API类
- 统一的方法命名规范
- 支持多种查询方式

## 生成器使用

### 实体生成器 (JavaSdkEntityGenerator.groovy)
自动生成实体类，包含：
- 私有字段
- Getter/Setter方法
- 标准JavaBean结构

### API生成器 (EntityApiGenerator.groovy)
自动生成API类，包含：
- CRUD操作方法
- 分页查询支持
- 条件查询支持
- 直接返回实体类型

## 使用流程

1. **配置模板变量**：
   - `${packageName}` - 包名
   - `${path}` - 路径结构

2. **生成实体类**：
   - 使用 `JavaSdkEntityGenerator` 为每个模型生成实体类

3. **生成API类**：
   - 使用 `EntityApiGenerator` 为每个实体生成API类

4. **使用客户端**：
   ```java
   FlexmodelClient client = FlexmodelClientFactory.createClient(baseUrl, datasource);
   // 注意：以下示例方法需要根据实际生成的实体类型进行调整
   UserApi userApi = client.users();
   List<User> users = userApi.listUsers();
   ```

## 扩展指南

### 添加新的实体类型
1. 创建实体类（或使用生成器）
2. 创建对应的API类（或使用生成器）
3. 在 `FlexmodelClient` 中添加便捷方法（参考example包中的示例）

### 自定义API方法
在生成的API类中添加业务特定的方法：
```java
public List<User> findActiveUsers() {
    return listUsers("status='active'", null, "name asc");
}
```

### 添加认证支持
在 `ApiClient` 中添加认证逻辑，或在 `FlexmodelClientFactory` 中配置认证参数。

## 最佳实践

1. **使用工厂方法**：优先使用 `FlexmodelClientFactory` 创建客户端
2. **类型安全**：尽量使用强类型API而不是通用API
3. **错误处理**：适当处理API调用异常
4. **资源管理**：合理管理客户端生命周期
5. **缓存策略**：考虑对频繁访问的数据进行缓存

## 注意事项

- 确保所有实体类都有无参构造函数
- API类需要与实体类在同一个包中
- 生成器需要正确的模型定义才能工作
- 客户端工厂方法提供了多种认证方式
- 示例代码位于 `example` 包中，实际使用时需要根据您的实体类型进行调整
- `FlexmodelClient` 中的便捷方法（如 `users()`, `products()` 等）是示例方法，需要根据实际生成的实体类型进行修改
