package ${packageName}.example;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ${packageName}.client.ApiClient;
import ${packageName}.PageResult;

/**
 * User API - 直接访问User实体（示例）
 * 示例用法：
 * UserApi userApi = new UserApi(apiClient, "myDatasource", "user");
 * List<User> users = userApi.listUsers();
 * User user = userApi.getUser("123");
 */
public class UserApi {
  private final ApiClient apiClient;
  private final String datasourceName;
  private final String modelName;

  public UserApi(ApiClient apiClient, String datasourceName, String modelName) {
    this.apiClient = apiClient;
    this.datasourceName = datasourceName;
    this.modelName = modelName;
  }

  /**
   * 获取所有User记录（分页）
   */
  public PageResult<User> listUsers(Integer current, Integer pageSize, String filter, Boolean nestedQuery, String sort) {
    String path = String.format("/api/f/datasources/%s/models/%s/records", encode(datasourceName), encode(modelName));
    Map<String, Object> q = new HashMap<>();
    if (current != null) q.put("current", current);
    if (pageSize != null) q.put("pageSize", pageSize);
    if (filter != null) q.put("filter", filter);
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);
    if (sort != null) q.put("sort", sort);

    return apiClient.get(path, q, new TypeReference<PageResult<User>>() {});
  }

  /**
   * 获取所有User记录（直接返回列表）
   * 这是您想要的方法：List<User> listUsers()
   */
  public List<User> listUsers(String filter, Boolean nestedQuery, String sort) {
    PageResult<User> pageResult = listUsers(null, null, filter, nestedQuery, sort);
    return pageResult.getList();
  }

  /**
   * 获取所有User记录（最简单的调用）
   */
  public List<User> listUsers() {
    return listUsers(null, null, null);
  }

  /**
   * 根据ID获取User记录
   */
  public User getUser(String id, Boolean nestedQuery) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    Map<String, Object> q = new HashMap<>();
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);

    return apiClient.get(path, q, new TypeReference<User>() {});
  }

  /**
   * 根据ID获取User记录（简化版本）
   */
  public User getUser(String id) {
    return getUser(id, null);
  }

  /**
   * 创建新的User记录
   */
  public User createUser(User user) {
    String path = String.format("/api/f/datasources/%s/models/%s/records", encode(datasourceName), encode(modelName));
    return apiClient.post(path, user, new TypeReference<User>() {});
  }

  /**
   * 更新User记录
   */
  public User updateUser(String id, User user) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    return apiClient.put(path, user, new TypeReference<User>() {});
  }

  /**
   * 部分更新User记录
   */
  public User patchUser(String id, User user) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    return apiClient.patch(path, user, new TypeReference<User>() {});
  }

  /**
   * 删除User记录
   */
  public void deleteUser(String id) {
    String path = String.format("/api/f/datasources/%s/models/%s/records/%s", encode(datasourceName), encode(modelName), encode(id));
    apiClient.delete(path);
  }

  private String encode(String s) {
    return s.replace("/", "%2F");
  }
}
