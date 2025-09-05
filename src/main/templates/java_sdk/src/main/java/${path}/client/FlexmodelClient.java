package ${packageName};

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flexmodel客户端 - 提供对各个实体API的访问
 */
public class FlexmodelClient {
  private final ApiClient apiClient;
  private final String datasourceName;
  private final Map<String, Object> entityApis = new ConcurrentHashMap<>();

  public FlexmodelClient(ApiClient apiClient, String datasourceName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
  }

  /**
   * 获取指定模型的API实例
   * @param modelName 模型名称
   * @param apiClass API类
   * @return API实例
   */
  @SuppressWarnings("unchecked")
  public <T> T getApi(String modelName, Class<T> apiClass) {
    String key = modelName + "_" + apiClass.getSimpleName();
    return (T) entityApis.computeIfAbsent(key, k -> {
      try {
        return apiClass.getConstructor(ApiClient.class, String.class, String.class)
            .newInstance(apiClient, datasourceName, modelName);
      } catch (Exception e) {
        throw new RuntimeException("无法创建API实例: " + apiClass.getSimpleName(), e);
      }
    });
  }

  /**
   * 获取通用Records API
   */
  public RecordsApi getRecordsApi() {
    return new RecordsApi(apiClient);
  }

  /**
   * 便捷方法：获取User API（示例）
   * 示例用法：List<User> users = client.users().listUsers();
   * 注意：这些是示例方法，实际使用时需要根据您的实体类型进行调整
   */
  public ${packageName}.example.UserApi users() {
    return getApi("user", ${packageName}.example.UserApi.class);
  }

  /**
   * 便捷方法：获取Product API（示例）
   * 示例用法：List<Product> products = client.products().listProducts();
   */
  public ${packageName}.example.ProductApi products() {
    return getApi("product", ${packageName}.example.ProductApi.class);
  }

  /**
   * 便捷方法：获取Order API（示例）
   * 示例用法：List<Order> orders = client.orders().listOrders();
   */
  public ${packageName}.example.OrderApi orders() {
    return getApi("order", ${packageName}.example.OrderApi.class);
  }

  // 可以根据需要添加更多便捷方法...
}
