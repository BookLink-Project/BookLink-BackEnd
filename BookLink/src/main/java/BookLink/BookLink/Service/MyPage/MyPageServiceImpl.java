package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Book.BookDetailDto;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReply;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.HistoryDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookLikeRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyRepository;
import BookLink.BookLink.Repository.Community.BookClub.BookClubRepository;
import BookLink.BookLink.Repository.Community.BookReport.BookReportRepository;
import BookLink.BookLink.Repository.Community.FreeBoard.FreeBoardRepository;
import BookLink.BookLink.Repository.CommunityReply.BookClub.BookClubReplyRepository;
import BookLink.BookLink.Repository.CommunityReply.BookReport.BookReportReplyRepository;
import BookLink.BookLink.Repository.CommunityReply.FreeBoard.FreeBoardReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Service.Book.BookServiceImpl;
import BookLink.BookLink.Service.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final PasswordEncoder passwordEncoder;
    private final BookServiceImpl bookServiceImpl;
    private final S3Service s3Service;

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;
    private final BookReplyRepository bookReplyRepository;
    private final BookClubRepository bookClubRepository;
    private final BookClubReplyRepository bookClubReplyRepository;
    private final BookReportRepository bookReportRepository;
    private final BookReportReplyRepository bookReportReplyRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardReplyRepository freeBoardReplyRepository;

    @Override
    public ResponseDto showHistory(Member member, String rentType, String communityType) {

        ResponseDto responseDto = new ResponseDto();

        HistoryDto dataDto = new HistoryDto();

        // 1. 내 정보
        HistoryDto.MyInfo myInfoDto = HistoryDto.MyInfo.builder()
                .image(member.getImage())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birth(member.getBirth())
                .address(member.getAddress())
                .canRent(bookRepository.countByRentSignalAndMember(true, member))
                .blocked(0L) // TODO dummy
                .myBooks(bookRepository.countByMember(member))
                .likedBooks(bookLikeRepository.countByMember(member))
                .rentTo(0L)
                .rentFrom(0L)
                .renting(0L)
                .goodReturn(0L)
                .badReturn(0L)
                .overdue(0L)
                .build();
        dataDto.setMyInfo(myInfoDto);

        // 2. 대여
        List<HistoryDto.RentHistory> rentHistoryDto;
        if (rentType.equals("overdue")) {
            rentHistoryDto = getOverdueList();
        } else { // "payment"
            rentHistoryDto = getPaymentList();
        }
        dataDto.setRentHistory(rentHistoryDto);

        // 3. 커뮤니티 활동
        List<HistoryDto.CommunityHistory> communityHistoryDto;
        switch (communityType) {
            case "free":
                communityHistoryDto = getFreeBoardList(member);
                break;
            case "report":
                communityHistoryDto = getBookReportList(member);
                break;
            case "club":
                communityHistoryDto = getBookClubList(member);
                break;
            case "reply":
                communityHistoryDto = getReplyList(member);
                break;
            default: // "review"
                communityHistoryDto = getBookReplyList(member);
                break;
        }
        dataDto.setCommunityHistory(communityHistoryDto);

        responseDto.setData(dataDto);

        return responseDto;
    }

    private List<HistoryDto.CommunityHistory> getFreeBoardList(Member member) { // 자유 글

        List<HistoryDto.CommunityHistory> communityHistoryDto = new ArrayList<>();

        List<FreeBoard> postList = freeBoardRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (FreeBoard post : postList) {

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type(null)
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .date(post.getCreatedTime())
                    .location(null)
                    .like_cnt(post.getLike_cnt())
                    .reply_cnt(post.getReply_cnt())
                    .view_cnt(post.getView_cnt())
                    .build();
            communityHistoryDto.add(community);
        }
        return communityHistoryDto;
    }

    private List<HistoryDto.CommunityHistory> getBookReportList(Member member) { // 독후감 글

        List<HistoryDto.CommunityHistory> communityHistoryDto = new ArrayList<>();

        List<BookReport> postList = bookReportRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (BookReport post : postList) {

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type(null)
                    .postId(post.getId())
                    .title(post.getBook_title())
                    .content(post.getContent())
                    .date(post.getCreatedTime())
                    .location(null)
                    .like_cnt(post.getLike_cnt())
                    .reply_cnt(post.getReply_cnt())
                    .view_cnt(post.getView_cnt())
                    .build();
            communityHistoryDto.add(community);
        }
        return communityHistoryDto;
    }

    private List<HistoryDto.CommunityHistory> getBookClubList(Member member) { // 독서모임 글

        List<HistoryDto.CommunityHistory> communityHistoryDto = new ArrayList<>();

        List<BookClub> postList = bookClubRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (BookClub post : postList) {

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type(null)
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .date(post.getCreatedTime())
                    .location(post.getLocation())
                    .like_cnt(post.getLike_cnt())
                    .reply_cnt(post.getReply_cnt())
                    .view_cnt(post.getView_cnt())
                    .build();
            communityHistoryDto.add(community);
        }
        return communityHistoryDto;
    }

    private List<HistoryDto.CommunityHistory> getReplyList(Member member) { // 모든 댓글

        List<HistoryDto.CommunityHistory> communityHistoryDto = new ArrayList<>();

        // 1. 독서 모임 댓글
        List<BookClubReply> bookClubReplies = bookClubReplyRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (BookClubReply reply : bookClubReplies) {

            Long parentId = reply.getParent().getId();

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("독서 모임")
                    .postId(reply.getPost().getId())
                    .title(reply.getPost().getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(parentId.equals(reply.getId()) ? bookClubReplyRepository.countByParentId(parentId) - 1 : 0)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }

        // 2. 독후감 댓글
        List<BookReportReply> bookReportReplies = bookReportReplyRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (BookReportReply reply : bookReportReplies) {

            Long parentId = reply.getParent().getId();

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("독후감")
                    .postId(reply.getPost().getId())
                    .title(reply.getPost().getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(parentId.equals(reply.getId()) ? bookReportReplyRepository.countByParentId(parentId) - 1 : 0)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }

        // 3. 자유글 댓글
        List<FreeBoardReply> freeBoardReplies = freeBoardReplyRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (FreeBoardReply reply : freeBoardReplies) {

            Long parentId = reply.getParent().getId();

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("자유글")
                    .postId(reply.getPost().getId())
                    .title(reply.getPost().getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(parentId.equals(reply.getId()) ? freeBoardReplyRepository.countByParentId(parentId) - 1 : 0)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }
        return communityHistoryDto
                .stream()
                .sorted(Comparator.comparing(HistoryDto.CommunityHistory::getDate).reversed())
                .collect(Collectors.toList());
    }

    private List<HistoryDto.CommunityHistory> getBookReplyList(Member member) { // 도서 후기

        List<HistoryDto.CommunityHistory> communityHistoryDto = new ArrayList<>();

        List<BookReply> replies = bookReplyRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (BookReply reply : replies) {

            BookDetailDto.Item book = bookServiceImpl.showBookApi(reply.getIsbn(), new RestTemplate()).getItem().get(0);

            Long parentId = reply.getParent().getId();

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("후기") // TODO ???
                    .postId(Long.valueOf(book.getIsbn13()))
                    .title(book.getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(parentId.equals(reply.getId()) ? bookReplyRepository.countByParentId(parentId) - 1 : 0)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }
        return communityHistoryDto;
    }

    private List<HistoryDto.RentHistory> getPaymentList() { // 결제 내역

        List<HistoryDto.RentHistory> rentHistoryDto = new ArrayList<>();

        // TODO for 문 반복문
        HistoryDto.RentHistory rent = HistoryDto.RentHistory.builder()
                .date(LocalDate.now()) // TODO dummy
                .type("결제")
                .content("대여료")
                .price(3000)
                .info_title("피프티 피플")
                .info_author("정세랑")
                .info_publisher("창비")
                .info_whose("길동아")
                .info_rent_from(LocalDate.of(2023, 7, 26))
                .info_rent_to(LocalDate.of(2023, 8, 25))
                .info_pay_date(LocalDateTime.of(2023, 7, 26, 16, 42, 0))
                .build();
        rentHistoryDto.add(rent);
        return rentHistoryDto;
    }

    private List<HistoryDto.RentHistory> getOverdueList() { // TODO FIX

        List<HistoryDto.RentHistory> rentHistoryDto = new ArrayList<>();

        // TODO for 문 반복문
        HistoryDto.RentHistory rent = HistoryDto.RentHistory.builder()
                .date(LocalDate.now()) // TODO dummy
                .type("결제")
                .content("대여료")
                .price(3000)
                .info_title("피프티 피플")
                .info_author("정세랑")
                .info_publisher("창비")
                .info_whose("길동아")
                .info_rent_from(LocalDate.of(2023, 7, 26))
                .info_rent_to(LocalDate.of(2023, 8, 25))
                .info_pay_date(LocalDateTime.of(2023, 7, 26, 16, 42, 0))
                .build();
        rentHistoryDto.add(rent);
        return rentHistoryDto;
    }

    @Override
    public ResponseDto verifyAccount(VerifyDto verifyDto, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        if (!passwordEncoder.matches(verifyDto.getPassword(), loginMember.getPassword())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("잘못된 비밀번호");
            return responseDto;
        }
        return responseDto;
    }

    @Override
    public ResponseDto showAccount(Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        responseDto.setData(
                new AccountDto.Response(
                        loginMember.getImage(),
                        loginMember.getName(),
                        loginMember.getNickname(),
                        loginMember.getEmail(),
                        loginMember.getBirth(),
                        loginMember.getAddress()
                        // loginMember.getCard()
                )
        );
        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto updateAccount(MultipartFile image, AccountDto.Request accountDto, Member loginMember) throws IOException {

        ResponseDto responseDto = new ResponseDto();

        Member selectedMember = memberRepository.findById(loginMember.getId()).orElse(null);

        String encodedPassword; // TODO 빈문자열로 예외 처리
        if (accountDto.getPassword() != null) {
            encodedPassword = passwordEncoder.encode(accountDto.getPassword());
        } else {
            encodedPassword = null;
        }

        URL imageUrl = s3Service.uploadImage(image);

        selectedMember.updateAccount(
                imageUrl,
                accountDto.getName(),
                accountDto.getNickname(),
                accountDto.getEmail(),
                encodedPassword,
                accountDto.getBirth(),
                accountDto.getAddress()
//                accountDto.getCard()
        );

        responseDto.setStatus(HttpStatus.CREATED);
        return responseDto;
    }
}
