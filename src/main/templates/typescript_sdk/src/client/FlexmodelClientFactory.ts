import { ApiClient, ApiClientOptions } from './ApiClient.js';
import { FlexmodelClient } from './FlexmodelClient.js';

/**
 * Flexmodel客户端工厂
 * 提供便捷的客户端创建方法
 */
export class FlexmodelClientFactory {
  /**
   * 创建Flexmodel客户端
   * @param baseURL 基础URL
   * @param datasourceName 数据源名称
   * @param options 选项
   * @returns Flexmodel客户端
   */
  static createClient(baseURL: string, datasourceName: string, options: ApiClientOptions = {}): FlexmodelClient {
    const apiClient = new ApiClient(baseURL, options);
    return new FlexmodelClient(apiClient, datasourceName);
  }

  /**
   * 创建Flexmodel客户端（带API Key认证）
   * @param baseURL 基础URL
   * @param datasourceName 数据源名称
   * @param apiKey API密钥
   * @param options 其他选项
   * @returns Flexmodel客户端
   */
  static createClientWithApiKey(
    baseURL: string, 
    datasourceName: string, 
    apiKey: string, 
    options: ApiClientOptions = {}
  ): FlexmodelClient {
    return this.createClient(baseURL, datasourceName, {
      ...options,
      apiKey
    });
  }

  /**
   * 创建Flexmodel客户端（带用户名密码认证）
   * @param baseURL 基础URL
   * @param datasourceName 数据源名称
   * @param username 用户名
   * @param password 密码
   * @param options 其他选项
   * @returns Flexmodel客户端
   */
  static createClientWithCredentials(
    baseURL: string, 
    datasourceName: string, 
    username: string, 
    password: string, 
    options: ApiClientOptions = {}
  ): FlexmodelClient {
    return this.createClient(baseURL, datasourceName, {
      ...options,
      username,
      password
    });
  }

  /**
   * 创建默认配置的客户端（用于开发环境）
   * @param datasourceName 数据源名称
   * @param options 其他选项
   * @returns Flexmodel客户端
   */
  static createDefaultClient(datasourceName: string, options: ApiClientOptions = {}): FlexmodelClient {
    return this.createClient('http://localhost:8080', datasourceName, options);
  }
}
