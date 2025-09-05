"""
User实体类 - 示例
"""

from typing import Optional
from pydantic import BaseModel, Field


class User(BaseModel):
    """User实体类 - 示例"""
    
    id: Optional[str] = Field(None, description="用户ID")
    name: Optional[str] = Field(None, description="用户姓名")
    email: Optional[str] = Field(None, description="邮箱地址")
    age: Optional[int] = Field(None, description="年龄")
    phone: Optional[str] = Field(None, description="电话号码")
    address: Optional[str] = Field(None, description="地址")
    
    class Config:
        """Pydantic配置"""
        json_encoders = {
            # 可以在这里添加自定义编码器
        }
    
    def get_id(self) -> Optional[str]:
        """获取用户ID"""
        return self.id
    
    def set_id(self, user_id: Optional[str]) -> None:
        """设置用户ID"""
        self.id = user_id
    
    def get_name(self) -> Optional[str]:
        """获取用户姓名"""
        return self.name
    
    def set_name(self, name: Optional[str]) -> None:
        """设置用户姓名"""
        self.name = name
    
    def get_email(self) -> Optional[str]:
        """获取邮箱地址"""
        return self.email
    
    def set_email(self, email: Optional[str]) -> None:
        """设置邮箱地址"""
        self.email = email
    
    def get_age(self) -> Optional[int]:
        """获取年龄"""
        return self.age
    
    def set_age(self, age: Optional[int]) -> None:
        """设置年龄"""
        self.age = age
    
    def get_phone(self) -> Optional[str]:
        """获取电话号码"""
        return self.phone
    
    def set_phone(self, phone: Optional[str]) -> None:
        """设置电话号码"""
        self.phone = phone
    
    def get_address(self) -> Optional[str]:
        """获取地址"""
        return self.address
    
    def set_address(self, address: Optional[str]) -> None:
        """设置地址"""
        self.address = address
    
    @classmethod
    def from_dict(cls, data: dict) -> 'User':
        """从字典创建User实例"""
        return cls(**data)
    
    def to_dict(self) -> dict:
        """转换为字典"""
        return self.dict()
