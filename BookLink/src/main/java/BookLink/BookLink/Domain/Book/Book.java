package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Member.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    private String recommendation; // 추천사

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

    // openapi에서 넘어올 때 null인 경우도 존재
    private LocalDate pud_date; // 출간일

    @NotNull
    @Column(name = "rent_signal")
    private Boolean rentSignal; // 대여 신청 가능 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rent_id")
    private BookRent bookRent;


    @Builder
    public Book(Long id, String title, String authors, String recommendation, String isbn, Integer price_sales
            , String cover, String category_name, String publisher, LocalDate pud_date, Boolean rent_signal, Member member, BookRent bookRent) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.recommendation = recommendation;
        this.isbn = isbn;
        this.price_sales = price_sales;
        this.cover = cover;
        this.category_name = category_name;
        this.publisher = publisher;
        this.pud_date = pud_date;
        this.rentSignal = rent_signal;
        this.member = member;
        this.bookRent = bookRent;
    }
}
