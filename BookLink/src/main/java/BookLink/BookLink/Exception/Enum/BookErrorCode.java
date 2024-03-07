package BookLink.BookLink.Exception.Enum;

import BookLink.BookLink.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {

    ALREADY_SAVED_BOOK(HttpStatus.CONFLICT, "이미 소장처리된 도서입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
