import { FlexmodelClientFactory } from '../client/FlexmodelClientFactory.js';
import { UserApi } from './UserApi.js';
import { User } from './User.js';

/**
 * Flexmodel SDK 使用示例
 * 展示如何直接访问实体而不是通用封装
 */
export class FlexmodelExample {
  
  static async main(): Promise<void> {
    try {
      // 1. 创建API客户端
      const client = FlexmodelClientFactory.createClient(
        'http://localhost:8080', 
        'myDatasource'
      );

      // 2. 直接访问User实体 - 这就是您想要的方式！
      const userApi = new UserApi(client['apiClient'], 'myDatasource', 'user');
      
      // 获取所有用户 - 直接返回User对象数组
      const allUsers: User[] = await userApi.listUsersSimple();
      console.log('所有用户数量:', allUsers.length);
      
      // 带条件查询用户
      const activeUsers: User[] = await userApi.listUsersAsList({
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
      const user: User = await userApi.getUser('123');
      console.log('用户姓名:', user.getName());
      
      // 4. 创建新用户
      const newUser = new User({
        name: '张三',
        email: 'zhangsan@example.com',
        age: 25
      });
      
      const createdUser: User = await userApi.createUser(newUser);
      console.log('创建的用户ID:', createdUser.getId());
      
      // 5. 更新用户
      createdUser.setAge(26);
      const updatedUser: User = await userApi.updateUser(createdUser.getId()!, createdUser);
      console.log('更新后的年龄:', updatedUser.getAge());
      
      // 6. 删除用户
      await userApi.deleteUser(createdUser.getId()!);
      console.log('用户已删除');
      
    } catch (error: any) {
      console.error('示例执行失败:', error.message);
    }
  }
  
  /**
   * 展示如何在实际业务中使用
   */
  static async businessExample(): Promise<void> {
    try {
      const client = FlexmodelClientFactory.createClient(
        'http://localhost:8080', 
        'ecommerce'
      );
      
      // 业务场景：获取用户的所有订单
      const userApi = new UserApi(client['apiClient'], 'ecommerce', 'user');
      
      // 获取用户
      const user: User = await userApi.getUser('user123');
      
      // 获取该用户的订单 - 直接返回User对象数组
      const userOrders: User[] = await userApi.listUsersAsList({
        filter: `userId='${user.getId()}'`,
        sort: 'createTime desc'
      });
      
      // 处理订单
      for (const order of userOrders) {
        console.log(`订单ID: ${order.getId()}, 金额: ${order.getAge()}`);
      }
      
    } catch (error: any) {
      console.error('业务示例执行失败:', error.message);
    }
  }
}

// 如果直接运行此文件，执行示例
if (require.main === module) {
  FlexmodelExample.main();
}
