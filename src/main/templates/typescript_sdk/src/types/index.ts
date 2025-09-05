/**
 * API客户端选项
 */
export interface ApiClientOptions {
  apiKey?: string;
  username?: string;
  password?: string;
  timeout?: number;
  headers?: Record<string, string>;
}

/**
 * 分页选项
 */
export interface PaginationOptions {
  current?: number;
  pageSize?: number;
  filter?: string;
  nestedQuery?: boolean;
  sort?: string;
}

/**
 * 查询选项
 */
export interface QueryOptions {
  filter?: string;
  nestedQuery?: boolean;
  sort?: string;
}

/**
 * API异常数据
 */
export interface ApiExceptionData {
  message?: string;
  code?: string;
  details?: any;
}
