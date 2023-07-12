package BookLink.BookLink.Domain.Book;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rent_id;

    private Integer book_rating; // 책 상태 표현

    // 도서 참고사진 등록

    private String book_status; // 도서 판매 상태 설명

    private Integer rental_fee; // 대여료

    private Integer min_date; // 대여 최소기간

    private Integer max_date; // 대여 최대기간

    private String book_status_exp; // 책 상태 설명

    @Builder
    public BookRent(Long rent_id, Integer book_rating, String book_status, Integer rental_fee, Integer min_date, Integer max_date, String book_status_exp) {
        this.rent_id = rent_id;
        this.book_rating = book_rating;
        this.book_status = book_status;
        this.rental_fee = rental_fee;
        this.min_date = min_date;
        this.max_date = max_date;
        this.book_status_exp = book_status_exp;
    }

    //    @ManyToOne
//    private Member member;
}
