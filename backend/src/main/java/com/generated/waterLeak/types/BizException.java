package com.generated.waterLeak.types;

/**
 * 业务异常：service 抛出，controller 层经 ErrorHandlerMiddleware 统一包装为
 * {code, message} 响应。禁止只在一个全局位置吞掉全部异常——
 * service 负责翻译业务规则，controller 建议日志，middleware 负责序列化。
 */
public class BizException extends RuntimeException {
  private final String code;

  public BizException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() { return code; }
}
