import { ApiClient } from '../client/ApiClient.js';
import { PageResult } from './PageResult.js';
import { PaginationOptions } from '../types/index.js';

/**
 * 通用Records API
 */
export class RecordsApi {
  private apiClient: ApiClient;

  constructor(apiClient: ApiClient) {
    this.apiClient = apiClient;
  }

  /**
   * 获取记录列表（分页）
   */
  async listRecords(
    datasourceName: string, 
    modelName: string, 
    options: PaginationOptions = {}
  ): Promise<PageResult<Record<string, any>>> {
    const {
      current = null,
      pageSize = null,
      filter = null,
      nestedQuery = null,
      sort = null
    } = options;

    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records`;
    const params: Record<string, any> = {};
    
    if (current !== null) params.current = current;
    if (pageSize !== null) params.pageSize = pageSize;
    if (filter !== null) params.filter = filter;
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;
    if (sort !== null) params.sort = sort;

    const data = await this.apiClient.get<{ total: number; list: Record<string, any>[] }>(path, params);
    return new PageResult(data.total, data.list);
  }

  /**
   * 获取记录列表（直接返回列表）
   */
  async listRecordsAsList(
    datasourceName: string, 
    modelName: string, 
    options: PaginationOptions = {}
  ): Promise<Record<string, any>[]> {
    const pageResult = await this.listRecords(datasourceName, modelName, options);
    return pageResult.getList();
  }

  /**
   * 创建记录
   */
  async createRecord(
    datasourceName: string, 
    modelName: string, 
    body: Record<string, any>
  ): Promise<Record<string, any>> {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records`;
    return await this.apiClient.post<Record<string, any>>(path, body);
  }

  /**
   * 获取单个记录
   */
  async getRecord(
    datasourceName: string, 
    modelName: string, 
    id: string, 
    options: { nestedQuery?: boolean } = {}
  ): Promise<Record<string, any>> {
    const { nestedQuery = null } = options;
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    const params: Record<string, any> = {};
    
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;

    return await this.apiClient.get<Record<string, any>>(path, params);
  }

  /**
   * 更新记录
   */
  async updateRecord(
    datasourceName: string, 
    modelName: string, 
    id: string, 
    body: Record<string, any>
  ): Promise<Record<string, any>> {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    return await this.apiClient.put<Record<string, any>>(path, body);
  }

  /**
   * 部分更新记录
   */
  async patchRecord(
    datasourceName: string, 
    modelName: string, 
    id: string, 
    body: Record<string, any>
  ): Promise<Record<string, any>> {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    return await this.apiClient.patch<Record<string, any>>(path, body);
  }

  /**
   * 删除记录
   */
  async deleteRecord(datasourceName: string, modelName: string, id: string): Promise<void> {
    const path = `/api/f/datasources/${this.encode(datasourceName)}/models/${this.encode(modelName)}/records/${this.encode(id)}`;
    await this.apiClient.delete(path);
  }

  /**
   * URL编码
   */
  private encode(str: string): string {
    return str.replace(/\//g, '%2F');
  }
}
