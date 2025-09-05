/**
 * Product实体类 - 示例
 */
export class Product {
  constructor(data = {}) {
    this.id = data.id || null;
    this.name = data.name || null;
    this.description = data.description || null;
    this.price = data.price || null;
    this.stock = data.stock || null;
    this.category = data.category || null;
    this.status = data.status || null;
  }

  getId() {
    return this.id;
  }

  setId(id) {
    this.id = id;
  }

  getName() {
    return this.name;
  }

  setName(name) {
    this.name = name;
  }

  getDescription() {
    return this.description;
  }

  setDescription(description) {
    this.description = description;
  }

  getPrice() {
    return this.price;
  }

  setPrice(price) {
    this.price = price;
  }

  getStock() {
    return this.stock;
  }

  setStock(stock) {
    this.stock = stock;
  }

  getCategory() {
    return this.category;
  }

  setCategory(category) {
    this.category = category;
  }

  getStatus() {
    return this.status;
  }

  setStatus(status) {
    this.status = status;
  }

  /**
   * 转换为JSON对象
   */
  toJSON() {
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
  static fromJSON(data) {
    return new Product(data);
  }
}
