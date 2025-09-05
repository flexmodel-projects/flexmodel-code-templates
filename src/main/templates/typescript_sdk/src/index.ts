// 核心客户端
export { ApiClient, ApiException } from './client/ApiClient.js';
export { FlexmodelClient } from './client/FlexmodelClient.js';
export { FlexmodelClientFactory } from './client/FlexmodelClientFactory.js';

// API类
export { RecordsApi } from './api/RecordsApi.js';
export { PageResult } from './api/PageResult.js';

// 类型定义
export * from './types/index.js';

// 示例类（可选导入）
export { User } from './example/User.js';
export { UserApi } from './example/UserApi.js';
export { FlexmodelExample } from './example/FlexmodelExample.js';
