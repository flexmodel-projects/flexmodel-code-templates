import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ApiClientOptions, ApiExceptionData } from '../types/index.js';

/**
 * 基础API客户端
 */
export class ApiClient {
  private baseURL: string;
  private apiKey?: string;
  private username?: string;
  private password?: string;
  private timeout: number;
  private client: AxiosInstance;

  constructor(baseURL: string, options: ApiClientOptions = {}) {
    this.baseURL = baseURL;
    this.apiKey = options.apiKey;
    this.username = options.username;
    this.password = options.password;
    this.timeout = options.timeout || 30000;
    
    // 创建axios实例
    this.client = axios.create({
      baseURL: this.baseURL,
      timeout: this.timeout,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      }
    });

    // 设置认证
    if (this.apiKey) {
      this.client.defaults.headers.common['Authorization'] = `Bearer ${this.apiKey}`;
    } else if (this.username && this.password) {
      this.client.defaults.auth = {
        username: this.username,
        password: this.password
      };
    }

    // 请求拦截器
    this.client.interceptors.request.use(
      (config: AxiosRequestConfig) => {
        return config;
      },
      (error: any) => {
        return Promise.reject(error);
      }
    );

    // 响应拦截器
    this.client.interceptors.response.use(
      (response: AxiosResponse) => {
        return response.data;
      },
      (error: any) => {
        if (error.response) {
          throw new ApiException(
            error.response.data?.message || error.message,
            error.response.status,
            error.response.data
          );
        } else if (error.request) {
          throw new ApiException('网络请求失败', 0, null);
        } else {
          throw new ApiException(error.message, 0, null);
        }
      }
    );
  }

  /**
   * GET请求
   */
  async get<T = any>(path: string, params: Record<string, any> = {}): Promise<T> {
    return await this.client.get(path, { params });
  }

  /**
   * POST请求
   */
  async post<T = any>(path: string, data: any = {}): Promise<T> {
    return await this.client.post(path, data);
  }

  /**
   * PUT请求
   */
  async put<T = any>(path: string, data: any = {}): Promise<T> {
    return await this.client.put(path, data);
  }

  /**
   * PATCH请求
   */
  async patch<T = any>(path: string, data: any = {}): Promise<T> {
    return await this.client.patch(path, data);
  }

  /**
   * DELETE请求
   */
  async delete(path: string): Promise<void> {
    await this.client.delete(path);
  }

  /**
   * 设置API Key
   */
  setApiKey(apiKey: string): void {
    this.apiKey = apiKey;
    this.client.defaults.headers.common['Authorization'] = `Bearer ${apiKey}`;
  }

  /**
   * 设置用户名密码
   */
  setCredentials(username: string, password: string): void {
    this.username = username;
    this.password = password;
    this.client.defaults.auth = { username, password };
  }
}

/**
 * API异常类
 */
export class ApiException extends Error {
  public readonly status: number;
  public readonly data: ApiExceptionData | null;

  constructor(message: string, status: number, data: ApiExceptionData | null) {
    super(message);
    this.name = 'ApiException';
    this.status = status;
    this.data = data;
  }
}
