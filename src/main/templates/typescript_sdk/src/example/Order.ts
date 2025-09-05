/**
 * Order实体类 - 示例
 */
export class Order {
  private id: string | null;
  private userId: string | null;
  private productId: string | null;
  private quantity: number | null;
  private amount: number | null;
  private status: string | null;
  private createTime: string | null;
  private updateTime: string | null;

  constructor(data: Partial<Order> = {}) {
    this.id = data.id || null;
    this.userId = data.userId || null;
    this.productId = data.productId || null;
    this.quantity = data.quantity || null;
    this.amount = data.amount || null;
    this.status = data.status || null;
    this.createTime = data.createTime || null;
    this.updateTime = data.updateTime || null;
  }

  getId(): string | null {
    return this.id;
  }

  setId(id: string | null): void {
    this.id = id;
  }

  getUserId(): string | null {
    return this.userId;
  }

  setUserId(userId: string | null): void {
    this.userId = userId;
  }

  getProductId(): string | null {
    return this.productId;
  }

  setProductId(productId: string | null): void {
    this.productId = productId;
  }

  getQuantity(): number | null {
    return this.quantity;
  }

  setQuantity(quantity: number | null): void {
    this.quantity = quantity;
  }

  getAmount(): number | null {
    return this.amount;
  }

  setAmount(amount: number | null): void {
    this.amount = amount;
  }

  getStatus(): string | null {
    return this.status;
  }

  setStatus(status: string | null): void {
    this.status = status;
  }

  getCreateTime(): string | null {
    return this.createTime;
  }

  setCreateTime(createTime: string | null): void {
    this.createTime = createTime;
  }

  getUpdateTime(): string | null {
    return this.updateTime;
  }

  setUpdateTime(updateTime: string | null): void {
    this.updateTime = updateTime;
  }

  /**
   * 转换为JSON对象
   */
  toJSON(): Record<string, any> {
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
  static fromJSON(data: Record<string, any>): Order {
    return new Order(data);
  }
}
