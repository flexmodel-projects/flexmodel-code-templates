"""
User API - 直接访问User实体（示例）
"""

from typing import List, Optional, Dict, Any
from ..client.api_client import ApiClient
from ..api.page_result import PageResult
from .user import User


class UserApi:
    """User API - 直接访问User实体（示例）"""
    
    def __init__(self, api_client: ApiClient, datasource_name: str, model_name: str):
        """
        初始化User API
        
        Args:
            api_client: API客户端实例
            datasource_name: 数据源名称
            model_name: 模型名称
        """
        self.api_client = api_client
        self.datasource_name = datasource_name
        self.model_name = model_name
    
    def _encode(self, text: str) -> str:
        """URL编码"""
        return text.replace('/', '%2F')
    
    def list_users(
        self,
        current: Optional[int] = None,
        page_size: Optional[int] = None,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> PageResult[User]:
        """
        获取所有User记录（分页）
        
        Args:
            current: 当前页
            page_size: 页大小
            filter_expr: 过滤条件
            nested_query: 是否嵌套查询
            sort: 排序
            
        Returns:
            分页结果
        """
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records"
        
        params = {}
        if current is not None:
            params['current'] = current
        if page_size is not None:
            params['pageSize'] = page_size
        if filter_expr is not None:
            params['filter'] = filter_expr
        if nested_query is not None:
            params['nestedQuery'] = nested_query
        if sort is not None:
            params['sort'] = sort
        
        data = self.api_client.get(path, params)
        users = [User.from_dict(item) for item in data['list']]
        return PageResult(data['total'], users)
    
    def list_users_as_list(
        self,
        filter_expr: Optional[str] = None,
        nested_query: Optional[bool] = None,
        sort: Optional[str] = None
    ) -> List[User]:
        """
        获取所有User记录（直接返回列表）
        这是您想要的方法：users = user_api.list_users_as_list()
        
        Args:
            filter_expr: 过滤条件
            nested_query: 是否嵌套查询
            sort: 排序
            
        Returns:
            User对象列表
        """
        page_result = self.list_users(
            filter_expr=filter_expr,
            nested_query=nested_query,
            sort=sort
        )
        return page_result.get_items()
    
    def list_users_simple(self) -> List[User]:
        """
        获取所有User记录（最简单的调用）
        
        Returns:
            User对象列表
        """
        return self.list_users_as_list()
    
    def get_user(
        self,
        user_id: str,
        nested_query: Optional[bool] = None
    ) -> User:
        """
        根据ID获取User记录
        
        Args:
            user_id: 用户ID
            nested_query: 是否嵌套查询
            
        Returns:
            User对象
        """
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(user_id)}"
        
        params = {}
        if nested_query is not None:
            params['nestedQuery'] = nested_query
        
        data = self.api_client.get(path, params)
        return User.from_dict(data)
    
    def create_user(self, user: User) -> User:
        """
        创建新的User记录
        
        Args:
            user: User对象
            
        Returns:
            创建的User对象
        """
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records"
        data = self.api_client.post(path, user.to_dict())
        return User.from_dict(data)
    
    def update_user(self, user_id: str, user: User) -> User:
        """
        更新User记录
        
        Args:
            user_id: 用户ID
            user: User对象
            
        Returns:
            更新后的User对象
        """
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(user_id)}"
        data = self.api_client.put(path, user.to_dict())
        return User.from_dict(data)
    
    def patch_user(self, user_id: str, user: User) -> User:
        """
        部分更新User记录
        
        Args:
            user_id: 用户ID
            user: User对象
            
        Returns:
            更新后的User对象
        """
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(user_id)}"
        data = self.api_client.patch(path, user.to_dict())
        return User.from_dict(data)
    
    def delete_user(self, user_id: str) -> None:
        """
        删除User记录
        
        Args:
            user_id: 用户ID
        """
        path = f"/api/f/datasources/{self._encode(self.datasource_name)}/models/{self._encode(self.model_name)}/records/{self._encode(user_id)}"
        self.api_client.delete(path)
