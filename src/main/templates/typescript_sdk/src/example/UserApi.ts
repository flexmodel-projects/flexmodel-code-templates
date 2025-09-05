import { ApiClient } from '../client/ApiClient.js';
import { PageResult } from '../api/PageResult.js';
import { User } from './User.js';
import { PaginationOptions, QueryOptions } from '../types/index.js';

/**
 * User API - 直接访问User实体（示例）
 * 示例用法：
 * const userApi = new UserApi(apiClient, "myDatasource", "user");
 * const users = await userApi.listUsers();
 * const user = await userApi.getUser("123");
 */
export class UserApi {
  private apiClient: ApiClient;
  private datasourceName: string;
  private modelName: string;

  constructor(apiClient: ApiClient, datasourceName: string, modelName: string) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  /**
   * 获取所有User记录（分页）
   */
  async listUsers(options: PaginationOptions = {}): Promise<PageResult<User>> {
    const {
      current = null,
      pageSize = null,
      filter = null,
      nestedQuery = null,
      sort = null
    } = options;

    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records`;
    const params: Record<string, any> = {};
    
    if (current !== null) params.current = current;
    if (pageSize !== null) params.pageSize = pageSize;
    if (filter !== null) params.filter = filter;
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;
    if (sort !== null) params.sort = sort;

    const data = await this.apiClient.get<{ total: number; list: Record<string, any>[] }>(path, params);
    const users = data.list.map(item => User.fromJSON(item));
    return new PageResult(data.total, users);
  }

  /**
   * 获取所有User记录（直接返回列表）
   * 这是您想要的方法：const users = await userApi.listUsers();
   */
  async listUsersAsList(options: QueryOptions = {}): Promise<User[]> {
    const pageResult = await this.listUsers(options);
    return pageResult.getList();
  }

  /**
   * 获取所有User记录（最简单的调用）
   */
  async listUsersSimple(): Promise<User[]> {
    return await this.listUsersAsList();
  }

  /**
   * 根据ID获取User记录
   */
  async getUser(id: string, options: { nestedQuery?: boolean } = {}): Promise<User> {
    const { nestedQuery = null } = options;
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const params: Record<string, any> = {};
    
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;

    const data = await this.apiClient.get<Record<string, any>>(path, params);
    return User.fromJSON(data);
  }

  /**
   * 创建新的User记录
   */
  async createUser(user: User): Promise<User> {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records`;
    const data = await this.apiClient.post<Record<string, any>>(path, user.toJSON());
    return User.fromJSON(data);
  }

  /**
   * 更新User记录
   */
  async updateUser(id: string, user: User): Promise<User> {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.put<Record<string, any>>(path, user.toJSON());
    return User.fromJSON(data);
  }

  /**
   * 部分更新User记录
   */
  async patchUser(id: string, user: User): Promise<User> {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.patch<Record<string, any>>(path, user.toJSON());
    return User.fromJSON(data);
  }

  /**
   * 删除User记录
   */
  async deleteUser(id: string): Promise<void> {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    await this.apiClient.delete(path);
  }

  /**
   * URL编码
   */
  private encode(str: string): string {
    return str.replace(/\//g, '%2F');
  }
}
