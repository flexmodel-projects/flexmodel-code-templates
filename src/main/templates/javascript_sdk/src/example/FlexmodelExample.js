import { FlexmodelClientFactory } from '../client/FlexmodelClientFactory.js';
import { UserApi } from './UserApi.js';
import { ProductApi } from './ProductApi.js';
import { OrderApi } from './OrderApi.js';
import { User } from './User.js';
import { Product } from './Product.js';
import { Order } from './Order.js';

/**
 * Flexmodel SDK 使用示例
 * 展示如何直接访问实体而不是通用封装
 */
export class FlexmodelExample {
  
  static async main() {
    try {
      // 1. 创建API客户端
      const client = FlexmodelClientFactory.createClient(
        'http://localhost:8080', 
        'myDatasource'
      );

      // 2. 直接访问User实体 - 这就是您想要的方式！
      const userApi = new UserApi(client.apiClient, 'myDatasource', 'user');
      
      // 获取所有用户 - 直接返回User对象数组
      const allUsers = await userApi.listUsersSimple();
      console.log('所有用户数量:', allUsers.length);
      
      // 带条件查询用户
      const activeUsers = await userApi.listUsersAsList({
        filter: "status='active'",
        sort: 'name asc'
      });
      console.log('活跃用户数量:', activeUsers.length);
      
      // 分页获取用户
      const userPage = await userApi.listUsers({
        current: 1,
        pageSize: 10
      });
      console.log('总用户数:', userPage.getTotal());
      console.log('当前页用户:', userPage.getSize());
      
      // 3. 获取单个用户
      const user = await userApi.getUser('123');
      console.log('用户姓名:', user.getName());
      
      // 4. 创建新用户
      const newUser = new User({
        name: '张三',
        email: 'zhangsan@example.com',
        age: 25
      });
      
      const createdUser = await userApi.createUser(newUser);
      console.log('创建的用户ID:', createdUser.getId());
      
      // 5. 更新用户
      createdUser.setAge(26);
      const updatedUser = await userApi.updateUser(createdUser.getId(), createdUser);
      console.log('更新后的年龄:', updatedUser.getAge());
      
      // 6. 删除用户
      await userApi.deleteUser(createdUser.getId());
      console.log('用户已删除');
      
      // 7. 访问其他实体 - 同样的模式
      const productApi = new ProductApi(client.apiClient, 'myDatasource', 'product');
      const products = await productApi.listProductsSimple();
      console.log('产品数量:', products.length);
      
      const orderApi = new OrderApi(client.apiClient, 'myDatasource', 'order');
      const orders = await orderApi.listOrdersSimple();
      console.log('订单数量:', orders.length);
      
      // 8. 使用通用Records API（如果需要）
      const recordsApi = client.getRecordsApi();
      const genericRecords = await recordsApi.listRecords(
        'myDatasource', 'user', {
          current: 1,
          pageSize: 10
        }
      );
      console.log('通用API获取的记录数:', genericRecords.getSize());
      
    } catch (error) {
      console.error('示例执行失败:', error.message);
    }
  }
  
  /**
   * 展示如何在实际业务中使用
   */
  static async businessExample() {
    try {
      const client = FlexmodelClientFactory.createClient(
        'http://localhost:8080', 
        'ecommerce'
      );
      
      // 业务场景：获取用户的所有订单
      const userApi = new UserApi(client.apiClient, 'ecommerce', 'user');
      const orderApi = new OrderApi(client.apiClient, 'ecommerce', 'order');
      
      // 获取用户
      const user = await userApi.getUser('user123');
      
      // 获取该用户的订单 - 直接返回Order对象数组
      const userOrders = await orderApi.listOrdersAsList({
        filter: `userId='${user.getId()}'`,
        sort: 'createTime desc'
      });
      
      // 处理订单
      for (const order of userOrders) {
        console.log(`订单ID: ${order.getId()}, 金额: ${order.getAmount()}`);
      }
      
    } catch (error) {
      console.error('业务示例执行失败:', error.message);
    }
  }
}

// 如果直接运行此文件，执行示例
if (import.meta.url === `file://${process.argv[1]}`) {
  FlexmodelExample.main();
}
