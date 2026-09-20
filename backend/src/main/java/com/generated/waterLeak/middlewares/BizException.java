package com.generated.waterLeak.middlewares;

/** 业务异常：携带错误码与 HTTP 状态，由 ErrorHandlerMiddleware 统一转成 {code, message}。 */
public class BizException extends RuntimeException {
  private final String code;
  private final int httpStatus;

  public BizException(String code, String message, int httpStatus) {
    super(message);
    this.code = code;
    this.httpStatus = httpStatus;
  }

  public static BizException conflict(String code, String message) { return new BizException(code, message, 409); }
  public static BizException notFound(String code, String message) { return new BizException(code, message, 404); }
  public static BizException badRequest(String code, String message) { return new BizException(code, message, 400); }

  public String getCode() { return code; }
  public int getHttpStatus() { return httpStatus; }
}
