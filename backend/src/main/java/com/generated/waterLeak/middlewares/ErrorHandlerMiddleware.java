package com.generated.waterLeak.middlewares;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import com.generated.waterLeak.types.BizException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局错误处理：统一输出 {code, message}。
 * service 抛出 BizException，controller 记录日志，本类只负责序列化，不吞异常。
 */
@RestControllerAdvice
public class ErrorHandlerMiddleware {
  private static final Logger log = LoggerFactory.getLogger(ErrorHandlerMiddleware.class);

  @ExceptionHandler(BizException.class)
  public ResponseEntity<Map<String, Object>> biz(BizException e) {
    HttpStatus status = switch (e.getCode()) {
      case ErrorCodes.NOT_FOUND -> HttpStatus.NOT_FOUND;
      case ErrorCodes.VALIDATION_FAILED -> HttpStatus.BAD_REQUEST;
      default -> HttpStatus.CONFLICT;
    };
    return ResponseEntity.status(status).body(body(e.getCode(), e.getMessage()));
  }

  /** 并发派工兜底：(crew_id, dispatch_date) 未关闭唯一索引冲突 → 只成功一单。 */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> conflict(DataIntegrityViolationException e) {
    log.warn("data integrity violation: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(body(ErrorCodes.CREW_DAY_CONFLICT, ErrorMessages.CREW_DAY_CONFLICT));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
    HttpMessageNotReadableException.class, IllegalArgumentException.class})
  public ResponseEntity<Map<String, Object>> badRequest(Exception e) {
    log.warn("bad request: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(body(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> unknown(Exception e) {
    log.error("unhandled error", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(body("INTERNAL_ERROR", "服务器内部错误"));
  }

  private Map<String, Object> body(String code, String message) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("code", code);
    map.put("message", message);
    return map;
  }
}
