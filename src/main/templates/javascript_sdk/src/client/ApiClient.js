import axios from 'axios';

/**
 * 基础API客户端
 */
export class ApiClient {
  constructor(baseURL, options = {}) {
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
      (config) => {
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // 响应拦截器
    this.client.interceptors.response.use(
      (response) => {
        return response.data;
      },
      (error) => {
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
  async get(path, params = {}) {
    return await this.client.get(path, { params });
  }

  /**
   * POST请求
   */
  async post(path, data = {}) {
    return await this.client.post(path, data);
  }

  /**
   * PUT请求
   */
  async put(path, data = {}) {
    return await this.client.put(path, data);
  }

  /**
   * PATCH请求
   */
  async patch(path, data = {}) {
    return await this.client.patch(path, data);
  }

  /**
   * DELETE请求
   */
  async delete(path) {
    return await this.client.delete(path);
  }

  /**
   * 设置API Key
   */
  setApiKey(apiKey) {
    this.apiKey = apiKey;
    this.client.defaults.headers.common['Authorization'] = `Bearer ${apiKey}`;
  }

  /**
   * 设置用户名密码
   */
  setCredentials(username, password) {
    this.username = username;
    this.password = password;
    this.client.defaults.auth = { username, password };
  }
}

/**
 * API异常类
 */
export class ApiException extends Error {
  constructor(message, status, data) {
    super(message);
    this.name = 'ApiException';
    this.status = status;
    this.data = data;
  }
}
