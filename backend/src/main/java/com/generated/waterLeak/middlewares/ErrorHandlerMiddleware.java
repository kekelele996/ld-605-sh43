package com.generated.waterLeak.middlewares;

import com.generated.waterLeak.constants.ErrorCodes;
import com.generated.waterLeak.constants.ErrorMessages;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerMiddleware {

  @ExceptionHandler(BizException.class)
  public ResponseEntity<Map<String, Object>> biz(BizException ex) {
    return ResponseEntity.status(ex.getHttpStatus())
        .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, IllegalArgumentException.class})
  public ResponseEntity<Map<String, Object>> badRequest(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("code", ErrorCodes.VALIDATION_FAILED, "message", ErrorMessages.VALIDATION_FAILED));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> fallback(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("code", ErrorCodes.INTERNAL_ERROR, "message", ErrorMessages.INTERNAL_ERROR));
  }
}
