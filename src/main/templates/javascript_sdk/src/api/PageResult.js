/**
 * 分页结果类
 */
export class PageResult {
  constructor(total = 0, list = []) {
    this.total = total;
    this.list = list;
  }

  /**
   * 获取总数
   */
  getTotal() {
    return this.total;
  }

  /**
   * 设置总数
   */
  setTotal(total) {
    this.total = total;
  }

  /**
   * 获取列表
   */
  getList() {
    return this.list;
  }

  /**
   * 设置列表
   */
  setList(list) {
    this.list = list;
  }

  /**
   * 获取当前页数据数量
   */
  getSize() {
    return this.list.length;
  }

  /**
   * 是否为空
   */
  isEmpty() {
    return this.list.length === 0;
  }

  /**
   * 转换为JSON
   */
  toJSON() {
    return {
      total: this.total,
      list: this.list
    };
  }
}
