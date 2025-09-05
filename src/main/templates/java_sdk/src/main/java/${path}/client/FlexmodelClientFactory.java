package ${packageName};

/**
 * Flexmodel客户端工厂
 * 提供便捷的客户端创建方法
 */
public class FlexmodelClientFactory {

    /**
     * 创建Flexmodel客户端
     * @param baseUrl 基础URL
     * @param datasourceName 数据源名称
     * @return Flexmodel客户端
     */
    public static FlexmodelClient createClient(String baseUrl, String datasourceName) {
        ApiClient apiClient = new ApiClient(baseUrl);
        return new FlexmodelClient(apiClient, datasourceName);
    }

    /**
     * 创建Flexmodel客户端（带认证）
     * @param baseUrl 基础URL
     * @param datasourceName 数据源名称
     * @param apiKey API密钥
     * @return Flexmodel客户端
     */
    public static FlexmodelClient createClient(String baseUrl, String datasourceName, String apiKey) {
        ApiClient apiClient = new ApiClient(baseUrl);
        apiClient.setApiKey(apiKey);
        return new FlexmodelClient(apiClient, datasourceName);
    }

    /**
     * 创建Flexmodel客户端（带用户名密码认证）
     * @param baseUrl 基础URL
     * @param datasourceName 数据源名称
     * @param username 用户名
     * @param password 密码
     * @return Flexmodel客户端
     */
    public static FlexmodelClient createClient(String baseUrl, String datasourceName, String username, String password) {
        ApiClient apiClient = new ApiClient(baseUrl);
        apiClient.setUsername(username);
        apiClient.setPassword(password);
        return new FlexmodelClient(apiClient, datasourceName);
    }

    /**
     * 创建默认配置的客户端（用于开发环境）
     * @param datasourceName 数据源名称
     * @return Flexmodel客户端
     */
    public static FlexmodelClient createDefaultClient(String datasourceName) {
        return createClient("http://localhost:8080", datasourceName);
    }
}
