package BookLink.BookLink.Domain.Book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String title;

        private String contents;

        private String url;

        private String isbn; // 표준 도서번호

        private LocalDateTime datetime;

        @ElementCollection
        private List<String> authors;

        private String publisher;

        @ElementCollection
        private List<String> translators;

        private Integer price; // 정가

        private Integer sale_price; // 도서 판매가

        private String thumbnail; // 도서 표지 미리보기 URL

        private String status; // 도서 판매 상태 정보
    }
}
