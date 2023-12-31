package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Common.RentalEnum;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String authors;

    @NotNull
    private String description; // 책 소개 요약

    @NotNull
    private String isbn; // 책 고유번호 13자리

    @NotNull
    private Integer price_sales; // 정가

    @NotNull
    private String cover; // 도서 표지 미리보기 URL

    @NotNull
    private String category_name; // 카테고리 이름

    @NotNull
    private String publisher; // 출판사

    @NotNull
    private LocalDate pud_date; // 출간일

    @NotNull
    private String recommend_contents; // 추천사

    @NotNull
    private Boolean rent_signal; // 대여 신청 가능 여부

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rent_id")
    private BookRent bookRent;


    @Builder
    public Book(Long id, String title, String authors, String description, String isbn, Integer price_sales
            , String cover, String category_name, String publisher, LocalDate pud_date, Boolean rent_signal, BookRent bookRent) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.isbn = isbn;
        this.price_sales = price_sales;
        this.cover = cover;
        this.category_name = category_name;
        this.publisher = publisher;
        this.pud_date = pud_date;
        this.rent_signal = rent_signal;
        this.bookRent = bookRent;
    }
}
