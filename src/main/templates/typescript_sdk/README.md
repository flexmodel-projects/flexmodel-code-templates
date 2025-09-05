# Flexmodel TypeScript SDK 模板

该模板提供访问 Flexmodel 后端的简易 TypeScript SDK，支持直接访问实体类型，无需通用封装。

## 特性

- **直接实体访问**：`const users: User[] = await userApi.listUsers()` 而不是 `ApiResponse<PageResult<Map<String, Object>>>`
- **类型安全**：强类型实体类，编译时类型检查
- **便捷API**：为每个实体生成专门的API类
- **灵活查询**：支持分页、过滤、排序等查询参数
- **简单易用**：工厂模式创建客户端，一行代码开始使用
- **完整类型定义**：提供完整的TypeScript类型定义

## 快速开始

### 1. 安装依赖

```bash
npm install
```

### 2. 构建项目

```bash
npm run build
```

### 3. 创建客户端

```typescript
import { FlexmodelClientFactory } from './src/client/FlexmodelClientFactory.js';

// 使用工厂方法创建客户端
const client = FlexmodelClientFactory.createClient(
  'http://localhost:8080', 
  'myDatasource'
);

// 或者手动创建
import { ApiClient } from './src/client/ApiClient.js';
import { FlexmodelClient } from './src/client/FlexmodelClient.js';

const apiClient = new ApiClient('http://localhost:8080');
const client = new FlexmodelClient(apiClient, 'myDatasource');
```

### 4. 直接访问实体

```typescript
import { UserApi } from './src/example/UserApi.js';
import { User } from './src/example/User.js';

// 获取用户API（示例）
const userApi = new UserApi(client['apiClient'], 'myDatasource', 'user');

// 获取所有用户 - 直接返回User对象数组
const users: User[] = await userApi.listUsersSimple();

// 带条件查询
const activeUsers: User[] = await userApi.listUsersAsList({
  filter: "status='active'",
  sort: 'name asc'
});

// 分页查询
const userPage = await userApi.listUsers({
  current: 1,
  pageSize: 10
});

// 获取单个用户
const user: User = await userApi.getUser('123');

// 创建用户
const newUser = new User({
  name: '张三',
  email: 'zhangsan@example.com',
  age: 25
});
const createdUser: User = await userApi.createUser(newUser);

// 更新用户
user.setAge(26);
const updatedUser: User = await userApi.updateUser(user.getId()!, user);

// 删除用户
await userApi.deleteUser(user.getId()!);
```

### 5. 访问多个实体

```typescript
import { ProductApi } from './src/example/ProductApi.js';
import { OrderApi } from './src/example/OrderApi.js';

// 用户相关操作（示例）
const userApi = new UserApi(client['apiClient'], 'myDatasource', 'user');
const users: User[] = await userApi.listUsersSimple();

// 产品相关操作（示例）
const productApi = new ProductApi(client['apiClient'], 'myDatasource', 'product');
const products = await productApi.listProductsSimple();

// 订单相关操作（示例）
const orderApi = new OrderApi(client['apiClient'], 'myDatasource', 'order');
const orders = await orderApi.listOrdersSimple();
```

## API 方法说明

每个实体API都提供以下方法：

- `list{Entity}sSimple(): Promise<Entity[]>` - 获取所有记录（直接返回列表）
- `list{Entity}sAsList(options): Promise<Entity[]>` - 带条件查询
- `list{Entity}s(options): Promise<PageResult<Entity>>` - 分页查询
- `get{Entity}(id): Promise<Entity>` - 根据ID获取记录
- `create{Entity}(entity): Promise<Entity>` - 创建记录
- `update{Entity}(id, entity): Promise<Entity>` - 更新记录
- `patch{Entity}(id, entity): Promise<Entity>` - 部分更新记录
- `delete{Entity}(id): Promise<void>` - 删除记录

## 类型定义

```typescript
// 分页选项
interface PaginationOptions {
  current?: number;
  pageSize?: number;
  filter?: string;
  nestedQuery?: boolean;
  sort?: string;
}

// 查询选项
interface QueryOptions {
  filter?: string;
  nestedQuery?: boolean;
  sort?: string;
}

// API客户端选项
interface ApiClientOptions {
  apiKey?: string;
  username?: string;
  password?: string;
  timeout?: number;
  headers?: Record<string, string>;
}
```

## 通用API

如果需要使用通用API（返回Map类型），可以使用：

```typescript
const recordsApi = client.getRecordsApi();
const records = await recordsApi.listRecords(
  'datasource', 'model', {
    current: 1,
    pageSize: 10
  }
);
```

## 配置

### 认证

```typescript
// API Key认证
const client = FlexmodelClientFactory.createClientWithApiKey(
  'http://localhost:8080', 
  'myDatasource', 
  'your-api-key'
);

// 用户名密码认证
const client = FlexmodelClientFactory.createClientWithCredentials(
  'http://localhost:8080', 
  'myDatasource', 
  'username', 
  'password'
);
```

### 开发环境

```typescript
// 使用默认配置（localhost:8080）
const client = FlexmodelClientFactory.createDefaultClient('myDatasource');
```

## 实体生成

模板包含实体生成器，会根据模型定义自动生成：
- 实体类（如User.ts）
- API类（如UserApi.ts）
- 完整的TypeScript类型定义

## 示例代码

所有示例代码都位于 `example` 包中：
- `example/User.ts` - 用户实体示例
- `example/Product.ts` - 产品实体示例  
- `example/Order.ts` - 订单实体示例
- `example/UserApi.ts` - 用户API示例
- `example/ProductApi.ts` - 产品API示例
- `example/OrderApi.ts` - 订单API示例
- `example/FlexmodelExample.ts` - 完整使用示例

## 构建和开发

```bash
# 安装依赖
npm install

# 开发模式（监听文件变化）
npm run dev

# 构建
npm run build

# 清理构建文件
npm run clean

# 代码检查
npm run lint

# 测试
npm test
```

## 依赖

- axios: HTTP客户端
- TypeScript: 类型系统
- Rollup: 模块打包器

## 注意事项

- 示例代码位于 `example` 包中，实际使用时需要根据您的实体类型进行调整
- `FlexmodelClient` 中的便捷方法（如 `users()`, `products()` 等）是示例方法，需要根据实际生成的实体类型进行修改
- 需要TypeScript 5.0+和Node.js 16+环境
- 构建后的文件位于 `dist` 目录
