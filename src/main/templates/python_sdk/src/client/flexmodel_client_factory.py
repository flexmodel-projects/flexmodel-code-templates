"""
Flexmodel客户端工厂
"""

from typing import Dict, Any, Optional
from .api_client import ApiClient
from .flexmodel_client import FlexmodelClient


class FlexmodelClientFactory:
    """Flexmodel客户端工厂 - 提供便捷的客户端创建方法"""
    
    @staticmethod
    def create_client(
        base_url: str, 
        datasource_name: str, 
        options: Optional[Dict[str, Any]] = None
    ) -> FlexmodelClient:
        """
        创建Flexmodel客户端
        
        Args:
            base_url: 基础URL
            datasource_name: 数据源名称
            options: 配置选项
            
        Returns:
            Flexmodel客户端实例
        """
        api_client = ApiClient(base_url, options)
        return FlexmodelClient(api_client, datasource_name)
    
    @staticmethod
    def create_client_with_api_key(
        base_url: str,
        datasource_name: str,
        api_key: str,
        options: Optional[Dict[str, Any]] = None
    ) -> FlexmodelClient:
        """
        创建Flexmodel客户端（带API Key认证）
        
        Args:
            base_url: 基础URL
            datasource_name: 数据源名称
            api_key: API密钥
            options: 其他选项
            
        Returns:
            Flexmodel客户端实例
        """
        if options is None:
            options = {}
        options['api_key'] = api_key
        return FlexmodelClientFactory.create_client(base_url, datasource_name, options)
    
    @staticmethod
    def create_client_with_credentials(
        base_url: str,
        datasource_name: str,
        username: str,
        password: str,
        options: Optional[Dict[str, Any]] = None
    ) -> FlexmodelClient:
        """
        创建Flexmodel客户端（带用户名密码认证）
        
        Args:
            base_url: 基础URL
            datasource_name: 数据源名称
            username: 用户名
            password: 密码
            options: 其他选项
            
        Returns:
            Flexmodel客户端实例
        """
        if options is None:
            options = {}
        options['username'] = username
        options['password'] = password
        return FlexmodelClientFactory.create_client(base_url, datasource_name, options)
    
    @staticmethod
    def create_default_client(
        datasource_name: str,
        options: Optional[Dict[str, Any]] = None
    ) -> FlexmodelClient:
        """
        创建默认配置的客户端（用于开发环境）
        
        Args:
            datasource_name: 数据源名称
            options: 其他选项
            
        Returns:
            Flexmodel客户端实例
        """
        return FlexmodelClientFactory.create_client('http://localhost:8080', datasource_name, options)
