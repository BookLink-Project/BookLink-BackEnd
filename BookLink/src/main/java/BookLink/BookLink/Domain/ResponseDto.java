package BookLink.BookLink.Domain;

import BookLink.BookLink.Domain.Common.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.spi.Status;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {

    private StatusEnum status;
    private String message;
    private Object data;

    public ResponseDto(StatusEnum status, String message, Object data) {
        this.status = StatusEnum.OK;
        this.message = null;
        this.data = null;
    }
}