"""
分页结果类
"""

from typing import List, TypeVar, Generic, Dict, Any

T = TypeVar('T')


class PageResult(Generic[T]):
    """分页结果类"""
    
    def __init__(self, total: int = 0, items: List[T] = None):
        """
        初始化分页结果
        
        Args:
            total: 总数
            items: 数据列表
        """
        self.total = total
        self.items = items or []
    
    def get_total(self) -> int:
        """获取总数"""
        return self.total
    
    def set_total(self, total: int) -> None:
        """设置总数"""
        self.total = total
    
    def get_items(self) -> List[T]:
        """获取数据列表"""
        return self.items
    
    def set_items(self, items: List[T]) -> None:
        """设置数据列表"""
        self.items = items
    
    def get_size(self) -> int:
        """获取当前页数据数量"""
        return len(self.items)
    
    def is_empty(self) -> bool:
        """是否为空"""
        return len(self.items) == 0
    
    def to_dict(self) -> Dict[str, Any]:
        """转换为字典"""
        return {
            'total': self.total,
            'items': self.items
        }
    
    def __repr__(self) -> str:
        return f"PageResult(total={self.total}, size={self.get_size()})"
