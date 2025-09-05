"""
Product实体类 - 示例
"""

from typing import Optional
from decimal import Decimal
from pydantic import BaseModel, Field


class Product(BaseModel):
    """Product实体类 - 示例"""
    
    id: Optional[str] = Field(None, description="产品ID")
    name: Optional[str] = Field(None, description="产品名称")
    description: Optional[str] = Field(None, description="产品描述")
    price: Optional[Decimal] = Field(None, description="价格")
    stock: Optional[int] = Field(None, description="库存数量")
    category: Optional[str] = Field(None, description="产品分类")
    status: Optional[str] = Field(None, description="产品状态")
    
    class Config:
        """Pydantic配置"""
        json_encoders = {
            Decimal: str,  # Decimal类型转换为字符串
        }
    
    def get_id(self) -> Optional[str]:
        """获取产品ID"""
        return self.id
    
    def set_id(self, product_id: Optional[str]) -> None:
        """设置产品ID"""
        self.id = product_id
    
    def get_name(self) -> Optional[str]:
        """获取产品名称"""
        return self.name
    
    def set_name(self, name: Optional[str]) -> None:
        """设置产品名称"""
        self.name = name
    
    def get_description(self) -> Optional[str]:
        """获取产品描述"""
        return self.description
    
    def set_description(self, description: Optional[str]) -> None:
        """设置产品描述"""
        self.description = description
    
    def get_price(self) -> Optional[Decimal]:
        """获取价格"""
        return self.price
    
    def set_price(self, price: Optional[Decimal]) -> None:
        """设置价格"""
        self.price = price
    
    def get_stock(self) -> Optional[int]:
        """获取库存数量"""
        return self.stock
    
    def set_stock(self, stock: Optional[int]) -> None:
        """设置库存数量"""
        self.stock = stock
    
    def get_category(self) -> Optional[str]:
        """获取产品分类"""
        return self.category
    
    def set_category(self, category: Optional[str]) -> None:
        """设置产品分类"""
        self.category = category
    
    def get_status(self) -> Optional[str]:
        """获取产品状态"""
        return self.status
    
    def set_status(self, status: Optional[str]) -> None:
        """设置产品状态"""
        self.status = status
    
    @classmethod
    def from_dict(cls, data: dict) -> 'Product':
        """从字典创建Product实例"""
        return cls(**data)
    
    def to_dict(self) -> dict:
        """转换为字典"""
        return self.dict()
