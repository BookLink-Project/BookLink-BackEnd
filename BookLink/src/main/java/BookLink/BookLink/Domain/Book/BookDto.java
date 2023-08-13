package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Member.Member;
import lombok.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class BookDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String book_title;
        private String authors;
        private LocalDate pud_date; // 출간일
        private String recommendation; // 책 소개 요약
        private String isbn13; // 책 고유번호 13자리
        private Integer price_sales; // 정가
        private String cover; // 도서 표지 미리보기 URL
        private String category_name; // 카테고리 이름
        private String publisher;

        private Boolean rent_signal; // 대여 신청 가능 여부

        private String book_rating; // 책 상태 표현
        private String book_status; // 도서 판매 상태 설명
        private Integer rental_fee; // 대여료
        private Integer min_date; // 대여 최소기간
        private Integer max_date; // 대여 최대기간
        private String rent_location;
        private String rent_method;

        public static Book toBookEntity(BookDto.Request dto, Member loginMember) {
            return Book.builder()
                    .title(dto.getBook_title())
                    .authors(dto.getAuthors())
                    .recommendation(dto.getRecommendation())
                    .isbn(dto.getIsbn13())
                    .price_sales(dto.getPrice_sales())
                    .cover(dto.getCover())
                    .category_name(dto.getCategory_name())
                    .publisher(dto.getPublisher())
                    .pud_date(dto.getPud_date())
                    .rent_signal(dto.getRent_signal())
                    .writer(loginMember)
                    .build();

        }

        public static Book toBookEntity(BookDto.Request dto, BookRent bookRent, Member member) {
            return Book.builder()
                    .title(dto.getBook_title())
                    .authors(dto.getAuthors())
                    .recommendation(dto.getRecommendation())
                    .isbn(dto.getIsbn13())
                    .price_sales(dto.getPrice_sales())
                    .cover(dto.getCover())
                    .category_name(dto.getCategory_name())
                    .publisher(dto.getPublisher())
                    .pud_date(dto.getPud_date())
                    .rent_signal(dto.getRent_signal())
                    .writer(member)
                    .bookRent(bookRent)
                    .build();
        }


    }
}
