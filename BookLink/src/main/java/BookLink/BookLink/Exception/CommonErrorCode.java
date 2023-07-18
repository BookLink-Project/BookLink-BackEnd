package BookLink.BookLink.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Resource already exists");

    private final HttpStatus httpStatus;
    private final String message;

}
