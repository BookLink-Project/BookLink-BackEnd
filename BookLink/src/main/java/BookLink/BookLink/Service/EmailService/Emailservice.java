package BookLink.BookLink.Service.EmailService;

import BookLink.BookLink.Domain.Email.EmailDto;

public interface Emailservice {
    EmailDto.Response sendSimpleMessage(String to)throws Exception;
}
