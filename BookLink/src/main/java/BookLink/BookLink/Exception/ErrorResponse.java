package BookLink.BookLink.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;

//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private final List<ValidationError> errors;

//    @Getter
//    @Builder
//    @RequiredArgsConstructor
//    public static class ValidationError {
//
//        private final String field;
//        private final String message;
//
//        public static ValidationError of(final FieldError fieldError) {
//            return ValidationError.builder()
//                    .field(fieldError.getField())
//                    .message(fieldError.getDefaultMessage())
//                    .build();
//        }
//    }
}
