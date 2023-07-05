package BookLink.BookLink.Domain.Book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDto {

    public Integer totalResults;
    List<Item> item = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Item{

        private String title;
        private String author;
        private LocalDate pubDate; // 출간일
        private String description; // 책 소개 요약
        private String isbn13; // 책 고유번호 13자리
        private Integer priceSales; // 정가
        private Integer priceStandard; // 도서 판매가
        private String cover; // 도서 표지 미리보기 URL
        private String categoryName; // 카테고리 이름
        private String publisher;


    }

}
