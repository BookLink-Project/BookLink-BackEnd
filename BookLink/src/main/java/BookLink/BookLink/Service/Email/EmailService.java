package BookLink.BookLink.Service.Email;

import BookLink.BookLink.Domain.Email.EmailDto;
import BookLink.BookLink.Domain.ResponseDto;

public interface EmailService {

    ResponseDto sendSimpleMessage(String to) throws Exception;

    ResponseDto confirmMessage(EmailDto.Request emailDto);
}
