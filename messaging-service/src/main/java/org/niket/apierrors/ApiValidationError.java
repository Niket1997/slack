package org.niket.apierrors;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.FieldError;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }

  public static List<ApiSubError> createApiValidationErrors(List<FieldError> fieldErrors) {
    List<ApiSubError> apiValidationErrors = new ArrayList<>();
    fieldErrors.forEach(
        it ->
            apiValidationErrors.add(
                new ApiValidationError(
                    it.getObjectName(),
                    it.getField(),
                    it.getRejectedValue(),
                    it.getDefaultMessage())));
    return apiValidationErrors;
  }
}
