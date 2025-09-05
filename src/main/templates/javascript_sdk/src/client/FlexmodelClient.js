import { RecordsApi } from '../api/RecordsApi.js';

/**
 * Flexmodel客户端 - 提供对各个实体API的访问
 */
export class FlexmodelClient {
  constructor(apiClient, datasourceName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.entityApis = new Map();
  }

  /**
   * 获取指定模型的API实例
   * @param {string} modelName 模型名称
   * @param {Function} ApiClass API类构造函数
   * @returns {Object} API实例
   */
  getApi(modelName, ApiClass) {
    const key = `${modelName}_${ApiClass.name}`;
    
    if (!this.entityApis.has(key)) {
      const apiInstance = new ApiClass(this.apiClient, this.datasourceName, modelName);
      this.entityApis.set(key, apiInstance);
    }
    
    return this.entityApis.get(key);
  }

  /**
   * 获取通用Records API
   */
  getRecordsApi() {
    return new RecordsApi(this.apiClient);
  }

  /**
   * 便捷方法：获取User API（示例）
   * 示例用法：const users = await client.users().listUsers();
   * 注意：这些是示例方法，实际使用时需要根据您的实体类型进行调整
   */
  users() {
    // 这里需要导入UserApi类
    // import { UserApi } from '../example/UserApi.js';
    // return this.getApi('user', UserApi);
    throw new Error('UserApi需要从example包中导入并实现');
  }

  /**
   * 便捷方法：获取Product API（示例）
   * 示例用法：const products = await client.products().listProducts();
   */
  products() {
    // 这里需要导入ProductApi类
    // import { ProductApi } from '../example/ProductApi.js';
    // return this.getApi('product', ProductApi);
    throw new Error('ProductApi需要从example包中导入并实现');
  }

  /**
   * 便捷方法：获取Order API（示例）
   * 示例用法：const orders = await client.orders().listOrders();
   */
  orders() {
    // 这里需要导入OrderApi类
    // import { OrderApi } from '../example/OrderApi.js';
    // return this.getApi('order', OrderApi);
    throw new Error('OrderApi需要从example包中导入并实现');
  }

  // 可以根据需要添加更多便捷方法...
}
