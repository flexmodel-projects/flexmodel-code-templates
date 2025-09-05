import { User } from './User.js';
import { PageResult } from '../api/PageResult.js';

/**
 * User API - 直接访问User实体（示例）
 * 示例用法：
 * const userApi = new UserApi(apiClient, "myDatasource", "user");
 * const users = await userApi.listUsers();
 * const user = await userApi.getUser("123");
 */
export class UserApi {
  constructor(apiClient, datasourceName, modelName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  /**
   * 获取所有User记录（分页）
   */
  async listUsers(options = {}) {
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
    const users = data.list.map(item => User.fromJSON(item));
    return new PageResult(data.total, users);
  }

  /**
   * 获取所有User记录（直接返回列表）
   * 这是您想要的方法：const users = await userApi.listUsers();
   */
  async listUsersAsList(options = {}) {
    const pageResult = await this.listUsers(options);
    return pageResult.getList();
  }

  /**
   * 获取所有User记录（最简单的调用）
   */
  async listUsersSimple() {
    return await this.listUsersAsList();
  }

  /**
   * 根据ID获取User记录
   */
  async getUser(id, options = {}) {
    const { nestedQuery = null } = options;
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const params = {};
    
    if (nestedQuery !== null) params.nestedQuery = nestedQuery;

    const data = await this.apiClient.get(path, params);
    return User.fromJSON(data);
  }

  /**
   * 创建新的User记录
   */
  async createUser(user) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records`;
    const data = await this.apiClient.post(path, user.toJSON());
    return User.fromJSON(data);
  }

  /**
   * 更新User记录
   */
  async updateUser(id, user) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.put(path, user.toJSON());
    return User.fromJSON(data);
  }

  /**
   * 部分更新User记录
   */
  async patchUser(id, user) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    const data = await this.apiClient.patch(path, user.toJSON());
    return User.fromJSON(data);
  }

  /**
   * 删除User记录
   */
  async deleteUser(id) {
    const path = `/api/f/datasources/${this.encode(this.datasourceName)}/models/${this.encode(this.modelName)}/records/${this.encode(id)}`;
    await this.apiClient.delete(path);
  }

  /**
   * URL编码
   */
  encode(str) {
    return str.replace(/\//g, '%2F');
  }
}
