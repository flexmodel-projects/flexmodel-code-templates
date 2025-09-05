import { ApiClient } from './ApiClient.js';
import { FlexmodelClient } from './FlexmodelClient.js';

/**
 * Flexmodel客户端工厂
 * 提供便捷的客户端创建方法
 */
export class FlexmodelClientFactory {
  /**
   * 创建Flexmodel客户端
   * @param {string} baseURL 基础URL
   * @param {string} datasourceName 数据源名称
   * @param {Object} options 选项
   * @returns {FlexmodelClient} Flexmodel客户端
   */
  static createClient(baseURL, datasourceName, options = {}) {
    const apiClient = new ApiClient(baseURL, options);
    return new FlexmodelClient(apiClient, datasourceName);
  }

  /**
   * 创建Flexmodel客户端（带API Key认证）
   * @param {string} baseURL 基础URL
   * @param {string} datasourceName 数据源名称
   * @param {string} apiKey API密钥
   * @param {Object} options 其他选项
   * @returns {FlexmodelClient} Flexmodel客户端
   */
  static createClientWithApiKey(baseURL, datasourceName, apiKey, options = {}) {
    return this.createClient(baseURL, datasourceName, {
      ...options,
      apiKey
    });
  }

  /**
   * 创建Flexmodel客户端（带用户名密码认证）
   * @param {string} baseURL 基础URL
   * @param {string} datasourceName 数据源名称
   * @param {string} username 用户名
   * @param {string} password 密码
   * @param {Object} options 其他选项
   * @returns {FlexmodelClient} Flexmodel客户端
   */
  static createClientWithCredentials(baseURL, datasourceName, username, password, options = {}) {
    return this.createClient(baseURL, datasourceName, {
      ...options,
      username,
      password
    });
  }

  /**
   * 创建默认配置的客户端（用于开发环境）
   * @param {string} datasourceName 数据源名称
   * @param {Object} options 其他选项
   * @returns {FlexmodelClient} Flexmodel客户端
   */
  static createDefaultClient(datasourceName, options = {}) {
    return this.createClient('http://localhost:8080', datasourceName, options);
  }
}
