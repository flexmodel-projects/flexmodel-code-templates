package ${packageName}.example;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ${packageName}.client.ApiClient;
import ${packageName}.PageResult;

/**
 * Product API - 直接访问Product实体（示例）
 */
public class ProductApi {
  private final ApiClient apiClient;
  private final String datasourceName;
  private final String modelName;

  public ProductApi(ApiClient apiClient, String datasourceName, String modelName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  public PageResult<Product> listProducts(Integer current, Integer pageSize, String filter, Boolean nestedQuery, String sort) {
    String path = String.format("/api/f/datasources/%s/models/%s/records", encode(datasourceName), encode(modelName));
    Map<String, Object> q = new HashMap<>();
    if (current != null) q.put("current", current);
    if (pageSize != null) q.put("pageSize", pageSize);
    if (filter != null) q.put("filter", filter);
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);
    if (sort != null) q.put("sort", sort);

    return apiClient.get(path, q, new TypeReference<PageResult<Product>>() {});
  }

  public List<Product> listProducts(String filter, Boolean nestedQuery, String sort) {
    PageResult<Product> pageResult = listProducts(null, null, filter, nestedQuery, sort);
    return pageResult.getList();
  }

  public List<Product> listProducts() {
    return listProducts(null, null, null);
  }

  public Product getProduct(String id, Boolean nestedQuery) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    Map<String, Object> q = new HashMap<>();
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);

    return apiClient.get(path, q, new TypeReference<Product>() {});
  }

  public Product getProduct(String id) {
    return getProduct(id, null);
  }

  public Product createProduct(Product product) {
    String path = String.format("/api/f/datasources/%s/models/%s/records", encode(datasourceName), encode(modelName));
    return apiClient.post(path, product, new TypeReference<Product>() {});
  }

  public Product updateProduct(String id, Product product) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    return apiClient.put(path, product, new TypeReference<Product>() {});
  }

  public Product patchProduct(String id, Product product) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    return apiClient.patch(path, product, new TypeReference<Product>() {});
  }

  public void deleteProduct(String id) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    apiClient.delete(path);
  }

  private String encode(String s) {
    return s.replace("/", "%2F");
  }
}
