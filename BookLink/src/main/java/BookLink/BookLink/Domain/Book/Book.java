package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Common.RentalEnum;
import lombok.*;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String authors;

    private String description; // 책 소개 요약

    private String isbn; // 책 고유번호 13자리

    private Integer price_sales; // 정가

    private Integer price_standard; // 도서 판매가

    private String cover; // 도서 표지 미리보기 URL

    private String category_name; // 카테고리 이름

    private String publisher;

    private LocalDate pub_date; // 출간일

//    private String status; // 도서 판매 상태 정보

//    private RentalEnum rental_status; // 대여 상태
//
//    private int book_status; // 책 상태
//
//    private String book_status_exp; // 책 상태 설명

    @Builder
    public Book(Long id, String title, String authors, String description, String isbn, Integer price_sales,
                Integer price_standard, String cover, String category_name, String publisher, LocalDate pub_date) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.isbn = isbn;
        this.price_sales = price_sales;
        this.price_standard = price_standard;
        this.cover = cover;
        this.category_name = category_name;
        this.publisher = publisher;
        this.pub_date = pub_date;
    }
}
