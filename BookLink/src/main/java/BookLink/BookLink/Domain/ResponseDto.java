package BookLink.BookLink.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseDto {

    private HttpStatus status;
    private String message;
    private Object data;

    public ResponseDto() {
        this.status = HttpStatus.OK;
        this.message = "올바른 접근입니다";
    }
}