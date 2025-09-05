"""
Order实体类 - 示例
"""

from typing import Optional
from decimal import Decimal
from datetime import datetime
from pydantic import BaseModel, Field


class Order(BaseModel):
    """Order实体类 - 示例"""
    
    id: Optional[str] = Field(None, description="订单ID")
    user_id: Optional[str] = Field(None, description="用户ID")
    product_id: Optional[str] = Field(None, description="产品ID")
    quantity: Optional[int] = Field(None, description="数量")
    amount: Optional[Decimal] = Field(None, description="金额")
    status: Optional[str] = Field(None, description="订单状态")
    create_time: Optional[datetime] = Field(None, description="创建时间")
    update_time: Optional[datetime] = Field(None, description="更新时间")
    
    class Config:
        """Pydantic配置"""
        json_encoders = {
            Decimal: str,  # Decimal类型转换为字符串
            datetime: lambda v: v.isoformat() if v else None,  # datetime转换为ISO格式
        }
    
    def get_id(self) -> Optional[str]:
        """获取订单ID"""
        return self.id
    
    def set_id(self, order_id: Optional[str]) -> None:
        """设置订单ID"""
        self.id = order_id
    
    def get_user_id(self) -> Optional[str]:
        """获取用户ID"""
        return self.user_id
    
    def set_user_id(self, user_id: Optional[str]) -> None:
        """设置用户ID"""
        self.user_id = user_id
    
    def get_product_id(self) -> Optional[str]:
        """获取产品ID"""
        return self.product_id
    
    def set_product_id(self, product_id: Optional[str]) -> None:
        """设置产品ID"""
        self.product_id = product_id
    
    def get_quantity(self) -> Optional[int]:
        """获取数量"""
        return self.quantity
    
    def set_quantity(self, quantity: Optional[int]) -> None:
        """设置数量"""
        self.quantity = quantity
    
    def get_amount(self) -> Optional[Decimal]:
        """获取金额"""
        return self.amount
    
    def set_amount(self, amount: Optional[Decimal]) -> None:
        """设置金额"""
        self.amount = amount
    
    def get_status(self) -> Optional[str]:
        """获取订单状态"""
        return self.status
    
    def set_status(self, status: Optional[str]) -> None:
        """设置订单状态"""
        self.status = status
    
    def get_create_time(self) -> Optional[datetime]:
        """获取创建时间"""
        return self.create_time
    
    def set_create_time(self, create_time: Optional[datetime]) -> None:
        """设置创建时间"""
        self.create_time = create_time
    
    def get_update_time(self) -> Optional[datetime]:
        """获取更新时间"""
        return self.update_time
    
    def set_update_time(self, update_time: Optional[datetime]) -> None:
        """设置更新时间"""
        self.update_time = update_time
    
    @classmethod
    def from_dict(cls, data: dict) -> 'Order':
        """从字典创建Order实例"""
        return cls(**data)
    
    def to_dict(self) -> dict:
        """转换为字典"""
        return self.dict()
