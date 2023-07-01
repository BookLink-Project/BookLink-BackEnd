package BookLink.BookLink.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private HttpStatus status;
    private String message;
    private int statusCode;

    public ResponseDto(HttpStatus status, String message, Object data) {
        this.status = HttpStatus.OK;
        this.message = null;
        this.data = null;
    }
}