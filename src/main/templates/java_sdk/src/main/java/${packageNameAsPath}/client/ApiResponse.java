package ${packageName};

public class ApiResponse<T> {
  private final int statusCode;
  private final T data;

  public ApiResponse(int statusCode, T data) {
    this.statusCode = statusCode;
    this.data = data;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public T getData() {
    return data;
  }
}


