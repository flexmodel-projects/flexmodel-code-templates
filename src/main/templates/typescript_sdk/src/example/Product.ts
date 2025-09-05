/**
 * Product实体类 - 示例
 */
export class Product {
  private id: string | null;
  private name: string | null;
  private description: string | null;
  private price: number | null;
  private stock: number | null;
  private category: string | null;
  private status: string | null;

  constructor(data: Partial<Product> = {}) {
    this.id = data.id || null;
    this.name = data.name || null;
    this.description = data.description || null;
    this.price = data.price || null;
    this.stock = data.stock || null;
    this.category = data.category || null;
    this.status = data.status || null;
  }

  getId(): string | null {
    return this.id;
  }

  setId(id: string | null): void {
    this.id = id;
  }

  getName(): string | null {
    return this.name;
  }

  setName(name: string | null): void {
    this.name = name;
  }

  getDescription(): string | null {
    return this.description;
  }

  setDescription(description: string | null): void {
    this.description = description;
  }

  getPrice(): number | null {
    return this.price;
  }

  setPrice(price: number | null): void {
    this.price = price;
  }

  getStock(): number | null {
    return this.stock;
  }

  setStock(stock: number | null): void {
    this.stock = stock;
  }

  getCategory(): string | null {
    return this.category;
  }

  setCategory(category: string | null): void {
    this.category = category;
  }

  getStatus(): string | null {
    return this.status;
  }

  setStatus(status: string | null): void {
    this.status = status;
  }

  /**
   * 转换为JSON对象
   */
  toJSON(): Record<string, any> {
    return {
      id: this.id,
      name: this.name,
      description: this.description,
      price: this.price,
      stock: this.stock,
      category: this.category,
      status: this.status
    };
  }

  /**
   * 从JSON对象创建Product实例
   */
  static fromJSON(data: Record<string, any>): Product {
    return new Product(data);
  }
}
