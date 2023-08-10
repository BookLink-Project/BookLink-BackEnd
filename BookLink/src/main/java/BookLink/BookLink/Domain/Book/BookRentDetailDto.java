package BookLink.BookLink.Domain.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookRentDetailDto {

    private Integer record_cnt;
    private Integer rent_available_cnt;
    private Integer renting_cnt;

    private List<BookRecordDto> bookRecordDtoList = new ArrayList<>();

    private String title;
    private String authors;
    private String recommendation; // 추천사
    private String isbn; // 책 고유번호 13자리
    private String cover; // 도서 표지 미리보기 URL
    private String publisher; // 출판사

    private String book_rating;
    private String rent_location;
    private String rent_method;
    private Integer min_date;
    private Integer max_date;
    private Integer rental_fee;
    private String book_status;

    // 도서 대여 사진

    private List<BookRentInfoDto> bookRentInfoDtoList = new ArrayList<>();

    @Builder
    public BookRentDetailDto(Integer record_cnt, Integer rent_available_cnt, Integer renting_cnt, List<BookRecordDto> bookRecordDtoList,
                             String title, String authors, String recommendation, String isbn, String cover, String publisher, String book_rating,
                             String rent_location, String rent_method, Integer min_date, Integer max_date, Integer rental_fee, String book_status,
                             List<BookRentInfoDto> bookRentInfoDtoList) {
        this.record_cnt = record_cnt;
        this.rent_available_cnt = rent_available_cnt;
        this.renting_cnt = renting_cnt;
        this.bookRecordDtoList = bookRecordDtoList;
        this.title = title;
        this.authors = authors;
        this.recommendation = recommendation;
        this.isbn = isbn;
        this.cover = cover;
        this.publisher = publisher;
        this.book_rating = book_rating;
        this.rent_location = rent_location;
        this.rent_method = rent_method;
        this.min_date = min_date;
        this.max_date = max_date;
        this.rental_fee = rental_fee;
        this.book_status = book_status;
        this.bookRentInfoDtoList = bookRentInfoDtoList;
    }
}
