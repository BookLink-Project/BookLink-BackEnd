//package BookLink.BookLink.Member.Controller;
//
//import BookLink.BookLink.BaseControllerTest;
//import BookLink.BookLink.Domain.Member.Member;
//import BookLink.BookLink.Repository.Member.MemberRepository;
//import net.minidev.json.JSONObject;
//import org.aspectj.lang.annotation.After;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.restdocs.snippet.Snippet;
//
//import javax.persistence.EntityManager;
//import java.time.LocalDate;
//import java.util.Date;
//
//import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
//import static io.restassured.RestAssured.given;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//
//public class MemberControllerTest extends BaseControllerTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
////    private EntityManager;
//
////    @AfterEach
////    public void afterEach() {
////        entityManager.clear();
////    }
//
//    private static final Snippet REQUEST_FIELDS = requestFields(
//            fieldWithPath("email").type(JsonFieldType.STRING).description("test@naver.com")
//    );
//
//    private static final Snippet RESPONSE_FIELDS = responseFields(
//            fieldWithPath("status").type(JsonFieldType.OBJECT).description("이름"),
//            fieldWithPath("message").type(JsonFieldType.STRING).description("나이"),
//            fieldWithPath("data").type(JsonFieldType.OBJECT).description("나이")
//    );
//
//    @Test
//    void email_doubleCheck() {
//        String email = "test@naver.com";
//
//        LocalDate birth_date = LocalDate.of(2000, 2, 15);
//
//        Member member1 = Member.builder()
//                .email("elwlahstmxjf@naver.com")
//                .password("password12")
//                .nickname("백엔드개발자")
//                .name("정유석")
//                .birth(birth_date)
//                .address("서울특별시 영등포구")
//                .build();
//
//        memberRepository.save(member1);
//
//
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("email", email);
//
//
//        given(this.spec)
//                .filter(document(DEFAULT_RESTDOC_PATH, REQUEST_FIELDS)) // API 문서 관련 필터 추가
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .header("Content-type", "application/json")
//                .body(requestBody)
//                .log().all()
//                .when()
//                .post("/api/v1/member/double-check/email")
//
//
//                .then()
//                .statusCode(HttpStatus.OK.value());
//    }
//}
