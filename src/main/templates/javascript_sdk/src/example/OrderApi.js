import { Order } from './Order.js';
import { PageResult } from '../api/PageResult.js';

/**
 * Order API - 直接访问Order实体（示例）
 */
export class OrderApi {
  constructor(apiClient, datasourceName, modelName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  async listOrders(options = {}) {
    const {
      current = null,
      pageSize = null,
      filter = null,
      nestedQuery = null,
      sort = null
    } = options;

    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records`;
    const params = {};
    
    if (current !== null) params.current = current;
    if (pageSize !== null) params.pageSize = pageSize;
    if (filter !== null) params.filter = filter;
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;
    if (sort !== null) params.sort = sort;

    const data = await this.apiClient.get(path, params);
    const orders = data.list.map(item => Order.fromJSON(item));
    return new PageResult(data.total, orders);
  }

  async listOrdersAsList(options = {}) {
    const pageResult = await this.listOrders(options);
    return pageResult.getList();
  }

  async listOrdersSimple() {
    return await this.listOrdersAsList();
  }

  async getOrder(id, options = {}) {
    const { nestedQuery = null } = options;
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const params = {};
    
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;

    const data = await this.apiClient.get(path, params);
    return Order.fromJSON(data);
  }

  async createOrder(order) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records`;
    const data = await this.apiClient.post(path, order.toJSON());
    return Order.fromJSON(data);
  }

  async updateOrder(id, order) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.put(path, order.toJSON());
    return Order.fromJSON(data);
  }

  async patchOrder(id, order) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.patch(path, order.toJSON());
    return Order.fromJSON(data);
  }

  async deleteOrder(id) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    await this.apiClient.delete(path);
  }

  encode(str) {
    return str.replace(/\//g, '%2F');
  }
}
