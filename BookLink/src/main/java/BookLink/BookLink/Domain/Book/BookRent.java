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
    private Long id;

    private String book_rating; // 책 상태 등급

    // 도서 참고사진 등록

    private String book_status; // 도서 상태 설명

    private Integer rental_fee; // 대여료

    private Integer min_date; // 대여 최소기간 (단위는 주)

    private Integer max_date; // 대여 최대기간

    private String rent_location; //대여장소

    private String rent_method; // 도서 대여 방법

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_id")
//    private Book book

    @Builder
    public BookRent(String book_rating, String book_status, Integer rental_fee, Integer min_date, Integer max_date, String rent_location, String rent_method) {
        this.book_rating = book_rating;
        this.book_status = book_status;
        this.rental_fee = rental_fee;
        this.min_date = min_date;
        this.max_date = max_date;
        this.rent_location = rent_location;
        this.rent_method = rent_method;
    }

}
