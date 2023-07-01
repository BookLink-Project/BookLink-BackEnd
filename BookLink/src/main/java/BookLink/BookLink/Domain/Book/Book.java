package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Common.BaseTimeEntity;
import BookLink.BookLink.Domain.Common.RentalEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String contents;

    private String detail_url;

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

    private RentalEnum rental_status; // 대여 상태

    private int book_status; // 책 상태

    private String book_status_exp; // 책 상태 설명
}
