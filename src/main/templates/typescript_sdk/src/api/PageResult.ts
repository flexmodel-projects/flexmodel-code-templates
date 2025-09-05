/**
 * 分页结果类
 */
export class PageResult<T> {
  private total: number;
  private list: T[];

  constructor(total: number = 0, list: T[] = []) {
    this.total = total;
    this.list = list;
  }

  /**
   * 获取总数
   */
  getTotal(): number {
    return this.total;
  }

  /**
   * 设置总数
   */
  setTotal(total: number): void {
    this.total = total;
  }

  /**
   * 获取列表
   */
  getList(): T[] {
    return this.list;
  }

  /**
   * 设置列表
   */
  setList(list: T[]): void {
    this.list = list;
  }

  /**
   * 获取当前页数据数量
   */
  getSize(): number {
    return this.list.length;
  }

  /**
   * 是否为空
   */
  isEmpty(): boolean {
    return this.list.length === 0;
  }

  /**
   * 转换为JSON
   */
  toJSON(): { total: number; list: T[] } {
    return {
      total: this.total,
      list: this.list
    };
  }
}
