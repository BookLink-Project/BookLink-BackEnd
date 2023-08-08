package BookLink.BookLink.Domain.MyPage;

import BookLink.BookLink.Domain.Card.Card;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {

    private MyInfo myInfo;
    private List<RentHistory> rentHistory = new ArrayList<>();
    private List<CommunityHistory> communityHistory = new ArrayList<>();

    @Getter
    @Setter
    @SuperBuilder
    public static class MyInfo extends AccountDto.Response {

        private Long canRent; // 대여 가능한 도서

        private Long blocked; // 이용 제한

        private Long myBooks;
        private Long likedBooks;
        private Long rentTo;
        private Long rentFrom;

        private Long renting;
        private Long goodReturn;
        private Long badReturn;
        private Long overdue;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentHistory {

        private LocalDateTime date;
        private String type;
        private String content;
        private Integer price;
        private String info_title;
        private String info_author;
        private String info_publisher;
        private String info_owner;
        private LocalDate info_rent_from;
        private LocalDate info_rent_to;
        private String info_card_name;
        private String return_location;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityHistory {

        private String type;
        private Long postId;
        private String title;
        private String content;
        private LocalDateTime date;
        private String location;
        private Long like_cnt;
        private Long reply_cnt;
        private Long view_cnt;

    }
}
