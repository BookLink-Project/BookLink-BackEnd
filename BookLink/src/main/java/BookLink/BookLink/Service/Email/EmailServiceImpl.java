package BookLink.BookLink.Service.Email;

import BookLink.BookLink.Domain.Email.Email;
import BookLink.BookLink.Domain.Email.EmailDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Email.EmailRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;

//    public String ePw = createKey();

    private Map<MimeMessage, String> createMessage(String to)throws Exception{
        System.out.println("보내는 대상 : "+ to);
        String ePw = createKey();
        System.out.println("인증 번호 : "+ePw);

        Map<MimeMessage, String> messageInfo = new HashMap<>();

        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("<이메일 인증 서비스>");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 북링크입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("poweragain0215@gmail.com","BookLink"));//보내는 사람

        messageInfo.put(message, ePw);

        return messageInfo;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public ResponseDto sendSimpleMessage(String email)throws Exception {

        emailRepository.findByEmail(email).ifPresent(emailRepository::delete);

        ResponseDto responseDto = new ResponseDto();
        EmailDto.Response emailDto = new EmailDto.Response();

        // TODO Auto-generated method stub
        Map<MimeMessage, String> messageInfo = createMessage(email);
        MimeMessage message = messageInfo.keySet().iterator().next();
        String ePw = messageInfo.get(message);

        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        Email email_entity = EmailDto.Request.toEntity(email, ePw);
        emailRepository.save(email_entity);

        emailDto.setAuthentication_number(ePw);

        responseDto.setData(emailDto);
        return responseDto;
    }

    @Override
    public ResponseDto confirmMessage(EmailDto.Request emailDto) {

        ResponseDto responseDto = new ResponseDto();

        Optional<Email> byEmail = emailRepository.findByEmail(emailDto.getEmail());
        Email email = byEmail.get();

        if(Objects.equals(emailDto.getAuthentication_number(), email.getNumber())) {
            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("올바른 인증번호입니다.");

        }
        else {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("올바르지 않은 인증번호입니다.");

        }

        emailRepository.delete(email); // 확인 후 삭제해야지 재요청시에 다시 검증을 할 수 있다.

        return responseDto;

    }
}
