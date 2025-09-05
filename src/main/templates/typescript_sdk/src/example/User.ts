/**
 * User实体类 - 示例
 */
export class User {
  private id: string | null;
  private name: string | null;
  private email: string | null;
  private age: number | null;
  private phone: string | null;
  private address: string | null;

  constructor(data: Partial<User> = {}) {
    this.id = data.id || null;
    this.name = data.name || null;
    this.email = data.email || null;
    this.age = data.age || null;
    this.phone = data.phone || null;
    this.address = data.address || null;
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

  getEmail(): string | null {
    return this.email;
  }

  setEmail(email: string | null): void {
    this.email = email;
  }

  getAge(): number | null {
    return this.age;
  }

  setAge(age: number | null): void {
    this.age = age;
  }

  getPhone(): string | null {
    return this.phone;
  }

  setPhone(phone: string | null): void {
    this.phone = phone;
  }

  getAddress(): string | null {
    return this.address;
  }

  setAddress(address: string | null): void {
    this.address = address;
  }

  /**
   * 转换为JSON对象
   */
  toJSON(): Record<string, any> {
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
  static fromJSON(data: Record<string, any>): User {
    return new User(data);
  }
}
