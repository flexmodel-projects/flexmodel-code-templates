package ${packageName};

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ${packageName}.client.ApiClient;
import ${packageName}.client.ApiClient;


public class RecordsApi {
  private final ApiClient apiClient;

  public RecordsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  // 通用方法 - 返回分页结果
  public PageResult<Map<String, Object>> listRecords(
      String datasourceName,
      String modelName,
      Integer current,
      Integer pageSize,
      String filter,
      Boolean nestedQuery,
      String sort
  ) {
    String path = String.format(
        "/api/f/datasources/%s/models/%s/records",
        encode(datasourceName), encode(modelName)
    );
    Map<String, Object> q = new HashMap<>();
    if (current != null) q.put("current", current);
    if (pageSize != null) q.put("pageSize", pageSize);
    if (filter != null) q.put("filter", filter);
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);
    if (sort != null) q.put("sort", sort);

    return apiClient.get(
        path, q, new TypeReference<PageResult<Map<String, Object>>>() {}
    );
  }

  // 通用方法 - 直接返回实体列表（不分页）
  public List<Map<String, Object>> listRecordsAsList(
      String datasourceName,
      String modelName,
      String filter,
      Boolean nestedQuery,
      String sort
  ) {
    PageResult<Map<String, Object>> pageResult = listRecords(
        datasourceName, modelName, null, null, filter, nestedQuery, sort
    );
    return pageResult.getList();
  }

  public Map<String, Object> createRecord(
      String datasourceName,
      String modelName,
      Map<String, Object> body
  ) {
    String path = String.format(
        "/api/f/datasources/%s/models/%s/records",
        encode(datasourceName), encode(modelName)
    );
    return apiClient.post(path, body, new TypeReference<Map<String, Object>>() {});
  }

  public Map<String, Object> getRecord(
      String datasourceName,
      String modelName,
      String id,
      Boolean nestedQuery
  ) {
    String path = String.format(
        "/api/f/datasources/%s/models/%s/records/%s",
        encode(datasourceName), encode(modelName), encode(id)
    );
    Map<String, Object> q = new HashMap<>();
    if (nestedQuery != null) q.put("nestedQuery", nestedQuery);
    return apiClient.get(path, q, new TypeReference<Map<String, Object>>() {});
  }

  public Map<String, Object> updateRecord(
      String datasourceName,
      String modelName,
      String id,
      Map<String, Object> body
  ) {
    String path = String.format(
        "/api/f/datasources/%s/models/%s/records/%s",
        encode(datasourceName), encode(modelName), encode(id)
    );
    return apiClient.put(path, body, new TypeReference<Map<String, Object>>() {});
  }

  public Map<String, Object> patchRecord(
      String datasourceName,
      String modelName,
      String id,
      Map<String, Object> body
  ) {
    String path = String.format(
        "/api/f/datasources/%s/models/%s/records/%s",
        encode(datasourceName), encode(modelName), encode(id)
    );
    return apiClient.patch(path, body, new TypeReference<Map<String, Object>>() {});
  }

  public void deleteRecord(
      String datasourceName,
      String modelName,
      String id
  ) {
    String path = String.format(
        "/api/f/datasources/%s/models/%s/records/%s",
        encode(datasourceName), encode(modelName), encode(id)
    );
    apiClient.delete(path);
  }

  private String encode(String s) {
    return s.replace("/", "%2F");
  }
}


