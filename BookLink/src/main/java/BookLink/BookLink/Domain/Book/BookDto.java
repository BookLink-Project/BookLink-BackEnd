package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class BookDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String author;
        private LocalDate pudDate; // 출간일
        private String description; // 책 소개 요약
        private String isbn13; // 책 고유번호 13자리
        private Integer priceSales; // 정가
        private Integer priceStandard; // 도서 판매가
        private String cover; // 도서 표지 미리보기 URL
        private String categoryName; // 카테고리 이름
        private String publisher;

        public static Book toEntity(BookDto.Request dto) {
            return Book.builder()
                    .title(dto.getTitle())
                    .authors(dto.getAuthor())
                    .description(dto.getDescription())
                    .isbn(dto.getIsbn13())
                    .price_sales(dto.getPriceSales())
                    .price_standard(dto.getPriceStandard())
                    .cover(dto.getCover())
                    .category_name(dto.getCategoryName())
                    .publisher(dto.getPublisher())
                    .pud_date(dto.getPudDate())
                    .build();
        }
    }
}
