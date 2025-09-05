import { Product } from './Product.js';
import { PageResult } from '../api/PageResult.js';

/**
 * Product API - 直接访问Product实体（示例）
 */
export class ProductApi {
  constructor(apiClient, datasourceName, modelName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  async listProducts(options = {}) {
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
    const products = data.list.map(item => Product.fromJSON(item));
    return new PageResult(data.total, products);
  }

  async listProductsAsList(options = {}) {
    const pageResult = await this.listProducts(options);
    return pageResult.getList();
  }

  async listProductsSimple() {
    return await this.listProductsAsList();
  }

  async getProduct(id, options = {}) {
    const { nestedQuery = null } = options;
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const params = {};
    
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;

    const data = await this.apiClient.get(path, params);
    return Product.fromJSON(data);
  }

  async createProduct(product) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records`;
    const data = await this.apiClient.post(path, product.toJSON());
    return Product.fromJSON(data);
  }

  async updateProduct(id, product) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.put(path, product.toJSON());
    return Product.fromJSON(data);
  }

  async patchProduct(id, product) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.patch(path, product.toJSON());
    return Product.fromJSON(data);
  }

  async deleteProduct(id) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    await this.apiClient.delete(path);
  }

  encode(str) {
    return str.replace(/\//g, '%2F');
  }
}
