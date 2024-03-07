package BookLink.BookLink.Exception.Enum;

import BookLink.BookLink.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.CONFLICT, "입력한 정보들을 다시 확인해주세요.");

    private final HttpStatus httpStatus;
    private final String message;

}
