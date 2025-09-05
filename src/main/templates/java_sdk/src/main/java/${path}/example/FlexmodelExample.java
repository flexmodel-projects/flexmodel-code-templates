package ${packageName}.example;

import java.util.List;
import ${packageName}.client.ApiClient;
{packageName}.client.FlexmodelClient;
{packageName}.client.FlexmodelClientFactory;
{packageName}.api.RecordsApi;
{packageName}.PageResult;
import java.util.Map;

/**
 * Flexmodel SDK 使用示例
 * 展示如何直接访问实体而不是通用封装
 */
public class FlexmodelExample {

    public static void main(String[] args) {
        // 1. 创建API客户端
        ApiClient apiClient = new ApiClient("http://localhost:8080");
        FlexmodelClient client = new FlexmodelClient(apiClient, "myDatasource");

        // 2. 直接访问User实体 - 这就是您想要的方式！
        UserApi userApi = client.users();

        // 获取所有用户 - 直接返回List<User>
        List<User> allUsers = userApi.listUsers();
        System.out.println("所有用户数量: " + allUsers.size());

        // 带条件查询用户
        List<User> activeUsers = userApi.listUsers("status='active'", null, "name asc");
        System.out.println("活跃用户数量: " + activeUsers.size());

        // 分页获取用户
        PageResult<User> userPage = userApi.listUsers(1, 10, null, null, null);
        System.out.println("总用户数: " + userPage.getTotal());
        System.out.println("当前页用户: " + userPage.getList().size());

        // 3. 获取单个用户
        User user = userApi.getUser("123");
        System.out.println("用户姓名: " + user.getName());

        // 4. 创建新用户
        User newUser = new User();
        newUser.setName("张三");
        newUser.setEmail("zhangsan@example.com");
        newUser.setAge(25);

        User createdUser = userApi.createUser(newUser);
        System.out.println("创建的用户ID: " + createdUser.getId());

        // 5. 更新用户
        createdUser.setAge(26);
        User updatedUser = userApi.updateUser(createdUser.getId(), createdUser);
        System.out.println("更新后的年龄: " + updatedUser.getAge());

        // 6. 删除用户
        userApi.deleteUser(createdUser.getId());
        System.out.println("用户已删除");

        // 7. 访问其他实体 - 同样的模式
        ProductApi productApi = client.products();
        List<Product> products = productApi.listProducts();
        System.out.println("产品数量: " + products.size());

        OrderApi orderApi = client.orders();
        List<Order> orders = orderApi.listOrders();
        System.out.println("订单数量: " + orders.size());

        // 8. 使用通用Records API（如果需要）
        RecordsApi recordsApi = client.getRecordsApi();
        PageResult<Map<String, Object>> genericRecords = recordsApi.listRecords(
            "myDatasource", "user", 1, 10, null, null, null
        );
        System.out.println("通用API获取的记录数: " + genericRecords.getList().size());
    }

    /**
     * 展示如何在实际业务中使用
     */
    public void businessExample() {
        ApiClient apiClient = new ApiClient("http://localhost:8080");
        FlexmodelClient client = new FlexmodelClient(apiClient, "ecommerce");

        // 业务场景：获取用户的所有订单
        UserApi userApi = client.users();
        OrderApi orderApi = client.orders();

        // 获取用户
        User user = userApi.getUser("user123");

        // 获取该用户的订单 - 直接返回List<Order>
        List<Order> userOrders = orderApi.listOrders("userId='" + user.getId() + "'", null, "createTime desc");

        // 处理订单
        for (Order order : userOrders) {
            System.out.println("订单ID: " + order.getId() + ", 金额: " + order.getAmount());
        }
    }
}
