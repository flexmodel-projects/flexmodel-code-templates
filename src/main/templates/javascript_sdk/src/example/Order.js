/**
 * Order实体类 - 示例
 */
export class Order {
  constructor(data = {}) {
    this.id = data.id || null;
    this.userId = data.userId || null;
    this.productId = data.productId || null;
    this.quantity = data.quantity || null;
    this.amount = data.amount || null;
    this.status = data.status || null;
    this.createTime = data.createTime || null;
    this.updateTime = data.updateTime || null;
  }

  getId() {
    return this.id;
  }

  setId(id) {
    this.id = id;
  }

  getUserId() {
    return this.userId;
  }

  setUserId(userId) {
    this.userId = userId;
  }

  getProductId() {
    return this.productId;
  }

  setProductId(productId) {
    this.productId = productId;
  }

  getQuantity() {
    return this.quantity;
  }

  setQuantity(quantity) {
    this.quantity = quantity;
  }

  getAmount() {
    return this.amount;
  }

  setAmount(amount) {
    this.amount = amount;
  }

  getStatus() {
    return this.status;
  }

  setStatus(status) {
    this.status = status;
  }

  getCreateTime() {
    return this.createTime;
  }

  setCreateTime(createTime) {
    this.createTime = createTime;
  }

  getUpdateTime() {
    return this.updateTime;
  }

  setUpdateTime(updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * 转换为JSON对象
   */
  toJSON() {
    return {
      id: this.id,
      userId: this.userId,
      productId: this.productId,
      quantity: this.quantity,
      amount: this.amount,
      status: this.status,
      createTime: this.createTime,
      updateTime: this.updateTime
    };
  }

  /**
   * 从JSON对象创建Order实例
   */
  static fromJSON(data) {
    return new Order(data);
  }
}
