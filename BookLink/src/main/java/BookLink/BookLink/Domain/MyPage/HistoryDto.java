package BookLink.BookLink.Domain.MyPage;

import lombok.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {

    private Profile profile;
    private MyBook myBook;
    private MyRent myRent;
    private List<RentHistory> rentHistory = new ArrayList<>();
    private List<CommunityHistory> communityHistory = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    public static class Profile {

        private URL image;
        private String name;
        private String nickname;
        private String email;
        private LocalDate birth;
        private String address;
    }

    @Getter
    @Setter
    @Builder
    public static class MyBook {

        private Long register; // 기록한 도서
        private Long rent; // 대여 등록된 도서

        private Long like; // 좋아요한 도서
        private Long report; // 독후감 작성 도서

    }

    @Getter
    @Setter
    @Builder
    public static class MyRent {

        private Long renting; // 빌린 도서 현황
        private Long lending; // 빌려준 도서 현황

        private Long rentTotal; // 빌린 횟수
        private Long lendTotal; // 빌려준 횟수

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
        private String info_card_name; //
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
