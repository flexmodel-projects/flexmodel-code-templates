import { PageResult } from './PageResult.js';

/**
 * 通用Records API
 */
export class RecordsApi {
  constructor(apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * 获取记录列表（分页）
   */
  async listRecords(datasourceName, modelName, options = {}) {
    const {
      current = null,
      pageSize = null,
      filter = null,
      nestedQuery = null,
      sort = null
    } = options;

    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records`;
    const params = {};
    
    if (current !== null) params.current = current;
    if (pageSize !== null) params.pageSize = pageSize;
    if (filter !== null) params.filter = filter;
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;
    if (sort !== null) params.sort = sort;

    const data = await this.apiClient.get(path, params);
    return new PageResult(data.total, data.list);
  }

  /**
   * 获取记录列表（直接返回列表）
   */
  async listRecordsAsList(datasourceName, modelName, options = {}) {
    const pageResult = await this.listRecords(datasourceName, modelName, options);
    return pageResult.getList();
  }

  /**
   * 创建记录
   */
  async createRecord(datasourceName, modelName, body) {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records`;
    return await this.apiClient.post(path, body);
  }

  /**
   * 获取单个记录
   */
  async getRecord(datasourceName, modelName, id, options = {}) {
    const { nestedQuery = null } = options;
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    const params = {};
    
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;

    return await this.apiClient.get(path, params);
  }

  /**
   * 更新记录
   */
  async updateRecord(datasourceName, modelName, id, body) {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    return await this.apiClient.put(path, body);
  }

  /**
   * 部分更新记录
   */
  async patchRecord(datasourceName, modelName, id, body) {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    return await this.apiClient.patch(path, body);
  }

  /**
   * 删除记录
   */
  async deleteRecord(datasourceName, modelName, id) {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    await this.apiClient.delete(path);
  }

  /**
   * URL编码
   */
  encode(str) {
    return str.replace(/\//g, '%2F');
  }
}
