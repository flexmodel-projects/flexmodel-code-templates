/**
 * User实体类 - 示例
 */
export class User {
  constructor(data = {}) {
    this.id = data.id || null;
    this.name = data.name || null;
    this.email = data.email || null;
    this.age = data.age || null;
    this.phone = data.phone || null;
    this.address = data.address || null;
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

  getEmail() {
    return this.email;
  }

  setEmail(email) {
    this.email = email;
  }

  getAge() {
    return this.age;
  }

  setAge(age) {
    this.age = age;
  }

  getPhone() {
    return this.phone;
  }

  setPhone(phone) {
    this.phone = phone;
  }

  getAddress() {
    return this.address;
  }

  setAddress(address) {
    this.address = address;
  }

  /**
   * 转换为JSON对象
   */
  toJSON() {
    return {
      id: this.id,
      name: this.name,
      email: this.email,
      age: this.age,
      phone: this.phone,
      address: this.address
    };
  }

  /**
   * 从JSON对象创建User实例
   */
  static fromJSON(data) {
    return new User(data);
  }
}
