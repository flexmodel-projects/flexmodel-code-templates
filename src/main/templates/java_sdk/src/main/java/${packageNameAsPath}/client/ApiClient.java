package ${packageName};

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ApiClient {
  private final String baseUrl;
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final Map<String, String> defaultHeaders = new HashMap<>();

  public ApiClient(String baseUrl) {
    this(baseUrl, HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
  }

  public ApiClient(String baseUrl, HttpClient httpClient) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.httpClient = httpClient;
    this.objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.defaultHeaders.put("Content-Type", "application/json");
    this.defaultHeaders.put("Accept", "application/json");
  }

  public ApiClient setDefaultHeader(String name, String value) {
    this.defaultHeaders.put(name, value);
    return this;
  }

  public <T> T get(String path, Map<String, Object> query, TypeReference<T> typeRef) {
    HttpRequest request = baseRequest("GET", buildUrl(path, query), null);
    return send(request, typeRef);
  }

  public <T> T post(String path, Object body, TypeReference<T> typeRef) {
    HttpRequest request = baseRequest("POST", buildUrl(path, null), serialize(body));
    return send(request, typeRef);
  }

  public <T> T put(String path, Object body, TypeReference<T> typeRef) {
    HttpRequest request = baseRequest("PUT", buildUrl(path, null), serialize(body));
    return send(request, typeRef);
  }

  public <T> T patch(String path, Object body, TypeReference<T> typeRef) {
    HttpRequest request = baseRequest("PATCH", buildUrl(path, null), serialize(body));
    return send(request, typeRef);
  }

  public void delete(String path) {
    HttpRequest request = baseRequest("DELETE", buildUrl(path, null), null);
    send(request, new TypeReference<Void>() {});
  }

  private HttpRequest baseRequest(String method, String url, String body) {
    HttpRequest.Builder builder = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofSeconds(30));

    for (Map.Entry<String, String> e : defaultHeaders.entrySet()) {
      builder.header(e.getKey(), e.getValue());
    }

    if ("GET".equals(method) || body == null) {
      builder.method(method, HttpRequest.BodyPublishers.noBody());
    } else {
      builder.method(method, HttpRequest.BodyPublishers.ofString(body));
    }
    return builder.build();
  }

  private String buildUrl(String path, Map<String, Object> query) {
    StringBuilder sb = new StringBuilder();
    sb.append(baseUrl);
    if (!path.startsWith("/")) sb.append('/');
    sb.append(path);
    if (query != null && !query.isEmpty()) {
      StringJoiner joiner = new StringJoiner("&");
      for (Map.Entry<String, Object> e : query.entrySet()) {
        if (e.getValue() == null) continue;
        joiner.add(encode(e.getKey()) + "=" + encode(String.valueOf(e.getValue())));
      }
      String qs = joiner.toString();
      if (!qs.isEmpty()) sb.append('?').append(qs);
    }
    return sb.toString();
  }

  private String encode(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }

  private String serialize(Object value) {
    try {
      return objectMapper.writeValueAsString(value == null ? new HashMap<>() : value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T send(HttpRequest request, TypeReference<T> typeRef) {
    try {
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      int code = response.statusCode();
      String body = response.body();
      if (code >= 200 && code < 300) {
        if (typeRef.getType().getTypeName().equals(Void.class.getTypeName())) {
          return null;
        }
        return objectMapper.readValue(body, typeRef);
      }
      throw new ApiException(code, "HTTP " + code, body);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}


