"""
基础API客户端
"""

import requests
from typing import Dict, Any, Optional, Union
from urllib.parse import urljoin


class ApiException(Exception):
    """API异常类"""
    
    def __init__(self, message: str, status_code: int = 0, data: Optional[Dict[str, Any]] = None):
        super().__init__(message)
        self.message = message
        self.status_code = status_code
        self.data = data or {}


class ApiClient:
    """基础API客户端"""
    
    def __init__(self, base_url: str, options: Optional[Dict[str, Any]] = None):
        """
        初始化API客户端
        
        Args:
            base_url: 基础URL
            options: 配置选项
        """
        self.base_url = base_url.rstrip('/')
        self.options = options or {}
        
        # 创建session
        self.session = requests.Session()
        
        # 设置默认headers
        self.session.headers.update({
            'Content-Type': 'application/json',
            'User-Agent': 'Flexmodel-Python-SDK/1.0.0'
        })
        
        # 设置认证
        if 'api_key' in self.options:
            self.session.headers['Authorization'] = f"Bearer {self.options['api_key']}"
        elif 'username' in self.options and 'password' in self.options:
            self.session.auth = (self.options['username'], self.options['password'])
        
        # 设置超时
        self.timeout = self.options.get('timeout', 30)
        
        # 设置其他headers
        if 'headers' in self.options:
            self.session.headers.update(self.options['headers'])
    
    def _build_url(self, path: str) -> str:
        """构建完整URL"""
        return urljoin(self.base_url + '/', path.lstrip('/'))
    
    def _handle_response(self, response: requests.Response) -> Union[Dict[str, Any], list]:
        """处理响应"""
        try:
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as e:
            try:
                error_data = response.json()
                raise ApiException(
                    error_data.get('message', str(e)),
                    response.status_code,
                    error_data
                )
            except ValueError:
                raise ApiException(str(e), response.status_code)
        except requests.exceptions.RequestException as e:
            raise ApiException(f"网络请求失败: {str(e)}", 0)
    
    def get(self, path: str, params: Optional[Dict[str, Any]] = None) -> Union[Dict[str, Any], list]:
        """GET请求"""
        url = self._build_url(path)
        response = self.session.get(url, params=params, timeout=self.timeout)
        return self._handle_response(response)
    
    def post(self, path: str, data: Optional[Dict[str, Any]] = None) -> Union[Dict[str, Any], list]:
        """POST请求"""
        url = self._build_url(path)
        response = self.session.post(url, json=data, timeout=self.timeout)
        return self._handle_response(response)
    
    def put(self, path: str, data: Optional[Dict[str, Any]] = None) -> Union[Dict[str, Any], list]:
        """PUT请求"""
        url = self._build_url(path)
        response = self.session.put(url, json=data, timeout=self.timeout)
        return self._handle_response(response)
    
    def patch(self, path: str, data: Optional[Dict[str, Any]] = None) -> Union[Dict[str, Any], list]:
        """PATCH请求"""
        url = self._build_url(path)
        response = self.session.patch(url, json=data, timeout=self.timeout)
        return self._handle_response(response)
    
    def delete(self, path: str) -> None:
        """DELETE请求"""
        url = self._build_url(path)
        response = self.session.delete(url, timeout=self.timeout)
        self._handle_response(response)
    
    def set_api_key(self, api_key: str) -> None:
        """设置API Key"""
        self.session.headers['Authorization'] = f"Bearer {api_key}"
    
    def set_credentials(self, username: str, password: str) -> None:
        """设置用户名密码"""
        self.session.auth = (username, password)
