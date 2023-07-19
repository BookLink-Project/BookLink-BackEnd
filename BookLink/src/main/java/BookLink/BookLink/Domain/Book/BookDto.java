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
        private LocalDate pud_date; // 출간일
        private String description; // 책 소개 요약
        private String isbn13; // 책 고유번호 13자리
        private Integer price_sales; // 정가
        private String cover; // 도서 표지 미리보기 URL
        private String category_name; // 카테고리 이름
        private String publisher;

        private Boolean rent_signal; // 대여 신청 가능 여부

        private Integer book_rating; // 책 상태 표현
        // 도서 참고사진 등록
        private String book_status; // 도서 판매 상태 설명
        private Integer rental_fee; // 대여료
        private Integer min_date; // 대여 최소기간
        private Integer max_date; // 대여 최대기간
        private String book_status_exp; // 책 상태 설명

        public static Book toBookEntity(BookDto.Request dto) {
            return Book.builder()
                    .title(dto.getTitle())
                    .authors(dto.getAuthor())
                    .description(dto.getDescription())
                    .isbn(dto.getIsbn13())
                    .price_sales(dto.getPrice_sales())
                    .cover(dto.getCover())
                    .category_name(dto.getCategory_name())
                    .publisher(dto.getPublisher())
                    .pud_date(dto.getPud_date())
                    .rent_signal(dto.getRent_signal())
                    .build();

        }

        public static BookRent toRentEntity(BookDto.Request dto) {
            return BookRent.builder()
                    .book_rating(dto.getBook_rating())
                    .book_status(dto.getBook_status())
                    .rental_fee(dto.getRental_fee())
                    .min_date(dto.getMin_date())
                    .max_date(dto.getMax_date())
                    .book_status_exp(dto.getBook_status_exp())
                    .build();
        }
    }
}
