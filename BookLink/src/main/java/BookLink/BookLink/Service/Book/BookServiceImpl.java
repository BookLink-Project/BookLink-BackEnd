package BookLink.BookLink.Service.Book;

import BookLink.BookLink.Domain.Book.BookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

//    @Value("${kakao.key}")
//    private String key;
//    private String url = "https://dapi.kakao.com/v3/search/book";

    private String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=[ttbelwlahstmxjf2057002] &Query=aladdin&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&InputEncoding=utf-8&Version=20131101";
    private String key = "ttbelwlahstmxjf2057001";

    @Override
    public Map callApi(String query) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", "KakaoAK " + key); //Authorization 설정
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("query", query) //인자
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();

        //GetForObject는 헤더를 정의할 수 없음
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        Map body = result.getBody();
        return body;
    }




}
