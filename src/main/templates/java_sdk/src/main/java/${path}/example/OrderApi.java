package ${packageName}.example;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ${packageName}.client.ApiClient;
import ${packageName}.PageResult;

/**
 * Order API - 直接访问Order实体（示例）
 */
public class OrderApi {
  private final ApiClient apiClient;
  private final String datasourceName;
  private final String modelName;

  public OrderApi(ApiClient apiClient, String datasourceName, String modelName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  public PageResult<Order> listOrders(Integer current, Integer pageSize, String filter, Boolean nestedQuery, String sort) {
    String path = String.format("/api/f/datasources/%s/models/%s/records", encode(datasourceName), encode(modelName));
    Map<String, Object> q = new HashMap<>();
    if (current != null) q.put("current", current);
    if (pageSize != null) q.put("pageSize", pageSize);
    if (filter != null) q.put("filter", filter);
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);
    if (sort != null) q.put("sort", sort);

    return apiClient.get(path, q, new TypeReference<PageResult<Order>>() {});
  }

  public List<Order> listOrders(String filter, Boolean nestedQuery, String sort) {
    PageResult<Order> pageResult = listOrders(null, null, filter, nestedQuery, sort);
    return pageResult.getList();
  }

  public List<Order> listOrders() {
    return listOrders(null, null, null);
  }

  public Order getOrder(String id, Boolean nestedQuery) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    Map<String, Object> q = new HashMap<>();
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);

    return apiClient.get(path, q, new TypeReference<Order>() {});
  }

  public Order getOrder(String id) {
    return getOrder(id, null);
  }

  public Order createOrder(Order order) {
    String path = String.format("/api/f/datasources/%s/models/%s/records", encode(datasourceName), encode(modelName));
    return apiClient.post(path, order, new TypeReference<Order>() {});
  }

  public Order updateOrder(String id, Order order) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    return apiClient.put(path, order, new TypeReference<Order>() {});
  }

  public Order patchOrder(String id, Order order) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    return apiClient.patch(path, order, new TypeReference<Order>() {});
  }

  public void deleteOrder(String id) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    apiClient.delete(path);
  }

  private String encode(String s) {
    return s.replace("/", "%2F");
  }
}
