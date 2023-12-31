package org.niket.apierrors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonTypeInfo(
  include = JsonTypeInfo.As.WRAPPER_OBJECT,
  use = JsonTypeInfo.Id.CUSTOM,
  property = "error",
  visible = true
)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {
  private HttpStatus status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;

  private String message;
  private String debugMessage;
  private List<ApiSubError> subErrors;

  private ApiError() {
    timestamp = LocalDateTime.now();
  }

  public ApiError(HttpStatus status) {
    this();
    this.status = status;
  }

  public ApiError(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.message = "Unexpected error";
    this.debugMessage = ex.getLocalizedMessage();
  }

  public ApiError(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  public void addSubErrors(List<ApiSubError> subErrors) {
    if (this.subErrors == null) {
      this.subErrors = new ArrayList<>();
    }
    this.subErrors.addAll(subErrors);
  }
}
