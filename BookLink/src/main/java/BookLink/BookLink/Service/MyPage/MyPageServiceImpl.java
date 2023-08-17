package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Book.Rent;
import BookLink.BookLink.Domain.Common.RentStatus;
import BookLink.BookLink.Domain.Book.BookDetailDto;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.Community.FreeBoard.FreeBoard;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReply;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.*;
import BookLink.BookLink.Domain.MyPage.AccountDto;
import BookLink.BookLink.Domain.MyPage.HistoryDto;
import BookLink.BookLink.Domain.MyPage.VerifyDto;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.Book.RentRepository;
import BookLink.BookLink.Repository.Book.BookLikeRepository;
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
    private final BookRentRepository bookRentRepository;
    private final RentRepository rentRepository;
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
        HistoryDto.Profile profileDto = HistoryDto.Profile.builder()
                .image(member.getImage())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birth(member.getBirth())
                .address(member.getAddress())
                .build();
        dataDto.setProfile(profileDto);

        Long rent_cnt = bookRepository.countByRentSignalAndWriter(true, member);
        Long register_cnt = bookRepository.countByWriter(member);
        Long like_cnt = bookLikeRepository.countByMember(member);
        Long report_cnt = bookReportRepository.countIsbnByWriter(member);

        HistoryDto.MyBook myBookDto = HistoryDto.MyBook.builder()
                .register(register_cnt)
                .rent(rent_cnt)
                .like(like_cnt)
                .report(report_cnt)
                .build();
        dataDto.setMyBook(myBookDto);

//        Long renting_cnt = rentRepository.countByRenterAndRentStatus(member, RentStatus.RENTING);
//        Long lending_cnt = rentRepository.countByLenderAndRentStatus(member, RentStatus.RENTING);
//        Long rentTotal_cnt = rentRepository.countByRenter(member); // 사용하려면 rentStatus 에 종료 타입 필요
//        Long lendTotal_cnt = rentRepository.countByLender(member); // 사용하려면 rentStatus 에 종료 타입 필요

        HistoryDto.MyRent myRentDto = HistoryDto.MyRent.builder()
                .renting(0L)
                .lending(0L)
                .rentTotal(0L)
                .lendTotal(0L)
                .build();
        dataDto.setMyRent(myRentDto);

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

            BookClub post = reply.getPost();
            Long parentId = reply.getParent().getId();
            Long sub_reply_cnt = parentId.equals(reply.getId()) ?
                    bookClubReplyRepository.countByParentId(parentId) - 1 : 0;

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("독서 모임")
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(sub_reply_cnt)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }

        // 2. 독후감 댓글
        List<BookReportReply> bookReportReplies = bookReportReplyRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (BookReportReply reply : bookReportReplies) {

            BookReport post = reply.getPost();
            Long parentId = reply.getParent().getId();
            Long sub_reply_cnt = parentId.equals(reply.getId()) ?
                    bookReportReplyRepository.countByParentId(parentId) - 1 : 0;

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("독후감")
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(sub_reply_cnt)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }

        // 3. 자유글 댓글
        List<FreeBoardReply> freeBoardReplies = freeBoardReplyRepository.findByWriterOrderByCreatedTimeDesc(member);

        for (FreeBoardReply reply : freeBoardReplies) {

            FreeBoard post = reply.getPost();
            Long parentId = reply.getParent().getId();
            Long sub_reply_cnt = parentId.equals(reply.getId()) ?
                    freeBoardReplyRepository.countByParentId(parentId) - 1 : 0;

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type("자유글")
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(sub_reply_cnt)
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
            String type = parentId.equals(reply.getId()) ? "후기" : "답글";
            Long sub_reply_cnt = parentId.equals(reply.getId()) ?
                    bookReplyRepository.countByParentId(parentId) - 1 : 0;

            HistoryDto.CommunityHistory community = HistoryDto.CommunityHistory.builder()
                    .type(type)
                    .postId(Long.valueOf(book.getIsbn13()))
                    .title(book.getTitle())
                    .content(reply.getContent())
                    .date(reply.getCreatedTime())
                    .location(null)
                    .like_cnt(reply.getLike_cnt())
                    .reply_cnt(sub_reply_cnt)
                    .view_cnt(null)
                    .build();
            communityHistoryDto.add(community);
        }
        return communityHistoryDto;
    }

    private List<HistoryDto.RentHistory> getPaymentList() { // 결제 내역

        List<HistoryDto.RentHistory> rentHistoryDto = new ArrayList<>();

        HistoryDto.RentHistory payment = HistoryDto.RentHistory.builder()
                .date(LocalDateTime.of(2023, 7, 26, 16, 42, 0)) // TODO dummy
                .type("결제")
                .content("대여료 결제")
                .price(3000)
                .info_title("피프티 피플")
                .info_author("정세랑")
                .info_publisher("창비")
                .info_owner("길동아")
                .info_rent_from(LocalDate.of(2023, 7, 26))
                .info_rent_to(LocalDate.of(2023, 8, 25))
                .info_card_name("초록현대카드")
                .return_location(null)
                .build();
        rentHistoryDto.add(payment);
        return rentHistoryDto;
    }

    private List<HistoryDto.RentHistory> getOverdueList() {

        List<HistoryDto.RentHistory> rentHistoryDto = new ArrayList<>();

        HistoryDto.RentHistory over = HistoryDto.RentHistory.builder()
                .date(LocalDateTime.of(2023, 7, 26, 0, 0, 0)) // TODO dummy
                .type("연체중")
                .content(null)
                .price(1000)
                .info_title("세이노의 가르침")
                .info_author(null)
                .info_publisher(null)
                .info_owner("가나다라마바사사")
                .info_rent_from(null)
                .info_rent_to(null)
                .info_card_name(null)
                .return_location("캐치카페 화장실 하수구 1동 A 상가 앞")
                .build();
        rentHistoryDto.add(over);
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
                )
        );
        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto updateAccount(MultipartFile image, AccountDto.Request accountDto, Member loginMember) throws IOException {

        ResponseDto responseDto = new ResponseDto();

        Member selectedMember = memberRepository.findById(loginMember.getId()).orElse(null);

        Member findWithEmail = memberRepository.findByEmail(accountDto.getEmail()).orElse(null);
        Member findWithNickname = memberRepository.findByNickname(accountDto.getNickname()).orElse(null);
        if ( findWithEmail!=null && findWithEmail!=selectedMember ) {
            responseDto.setStatus(HttpStatus.CONFLICT);
            responseDto.setMessage("이메일 중복");
            return responseDto;
        }
        if (findWithNickname!=null && findWithNickname!=selectedMember) {
            responseDto.setStatus(HttpStatus.CONFLICT);
            responseDto.setMessage("닉네임 중복");
            return responseDto;
        }

        String encodedPassword;
        if (!accountDto.getPassword().equals("")) {
            encodedPassword = passwordEncoder.encode(accountDto.getPassword());
        } else {
            encodedPassword = "";
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
        );

        responseDto.setStatus(HttpStatus.CREATED);
        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto myBook(Member member) {

        ResponseDto responseDto = new ResponseDto();

        List<Rent> rents = rentRepository.findByRenter(member);
        List<Rent> byLender = rentRepository.findByLender(member);
        List<Book> books = member.getBooks();

        List<MyBookRentDto> myBookRentDtoList = new ArrayList<>();
        List<MyRecordBookDto> myRecordBookDtoList = new ArrayList<>();

        int record_cnt = books.size();
        int rent_available_cnt = 0;
        int lend_cnt = 0;
        int rent_cnt = byLender.size();

        List<Rent> first_page_rents = rents.subList(0, 8);

        for (Rent rent : first_page_rents) {

            Book book = rent.getBook();
            BookRent bookRent = book.getBookRent();
            Member lender = rent.getLender();

            MyBookRentDto myBookRentDto = MyBookRentDto.builder()
                    .title(book.getTitle())
                    .authors(book.getAuthors())
                    .publisher(book.getPublisher())
                    .lender(lender.getNickname())
                    .rent_location(bookRent.getRent_location())
                    .rent_date(rent.getRent_date())
                    .return_location(rent.getReturn_location())
                    .return_date(rent.getRent_date())
                    .rental_fee(bookRent.getRental_fee())
                    .build();

            myBookRentDtoList.add(myBookRentDto);
        }

        for (Book book : books) {
            BookRent bookRent = book.getBookRent();

            MyRecordBookDto myRecordBookDto = new MyRecordBookDto();

            if (bookRent == null) {
                myRecordBookDto = MyRecordBookDto.builder()
                        .book_id(book.getId())
                        .rent_status(RentStatus.DENIED)
                        .cover(book.getCover())
                        .title(book.getTitle())
                        .authors(book.getAuthors())
                        .publisher(book.getPublisher())
                        .rental_fee(0)
                        .max_date(0)
                        .build();
            }
            else {
                RentStatus rent_status = bookRent.getRent_status();

                if (rent_status == RentStatus.RENTING) {
                    rent_cnt += 1;
                } else {
                    rent_available_cnt += 1;
                }

                myRecordBookDto = MyRecordBookDto.builder()
                        .book_id(book.getId())
                        .rent_status(bookRent.getRent_status())
                        .cover(book.getCover())
                        .title(book.getTitle())
                        .authors(book.getAuthors())
                        .publisher(book.getPublisher())
                        .rental_fee(bookRent.getRental_fee())
                        .max_date(bookRent.getMax_date())
                        .build();
            }

            myRecordBookDtoList.add(myRecordBookDto);
        }

        MyBookDto myBookDto = MyBookDto.builder()
                .record_cnt(record_cnt)
                .rent_available_cnt(rent_available_cnt)
                .lend_cnt(lend_cnt)
                .rent_cnt(rent_cnt)
                .myBookRentDtoList(myBookRentDtoList)
                .myRecordBookDtoList(myRecordBookDtoList)
                .build();

        responseDto.setData(myBookDto);
        return responseDto;
    }

    @Override
    public ResponseDto myRentBook(Integer page, Member member) {

        ResponseDto responseDto = new ResponseDto();

        List<MyBookRentDto> myBookRentDtoList = new ArrayList<>();
        List<Rent> rents = rentRepository.findByRenter(member);

        int chunkSize = 8;
        int endSize = (page + 1) * chunkSize;
        int size = rents.size();

        if (size < chunkSize) {
            endSize = size;
        }

        List<Rent> chunkRent = rents.subList((page) * chunkSize, endSize);

        for (Rent rent : chunkRent) {

            Book book = rent.getBook();
            BookRent bookRent = book.getBookRent();
            Member lender = rent.getLender();

            MyBookRentDto myBookRentDto = MyBookRentDto.builder()
                    .title(book.getTitle())
                    .authors(book.getAuthors())
                    .publisher(book.getPublisher())
                    .lender(lender.getNickname())
                    .rent_location(bookRent.getRent_location())
                    .rent_date(rent.getRent_date())
                    .return_location(rent.getReturn_location())
                    .return_date(rent.getRent_date())
                    .rental_fee(bookRent.getRental_fee())
                    .build();

            myBookRentDtoList.add(myBookRentDto);
        }

        responseDto.setData(myBookRentDtoList);
        return responseDto;
    }

    @Override
    public ResponseDto myLendBook(Integer page, Member member) {

        ResponseDto responseDto = new ResponseDto();

        List<MyBookLendDto> myBookRentDtoList = new ArrayList<>();
        List<Rent> rents = rentRepository.findByLender(member);

        int chunkSize = 8;
        int endSize = (page + 1) * chunkSize;
        int size = rents.size();

        if (size < chunkSize) {
            endSize = size;
        }

        List<Rent> chunkRent = rents.subList((page) * chunkSize, endSize);

        for (Rent rent : chunkRent) {

            Book book = rent.getBook();
            BookRent bookRent = book.getBookRent();
            Member renter = rent.getRenter();

            MyBookLendDto myBookLendDto = MyBookLendDto.builder()
                    .title(book.getTitle())
                    .authors(book.getAuthors())
                    .publisher(book.getPublisher())
                    .renter(renter.getNickname())
                    .rent_location(bookRent.getRent_location())
                    .rent_date(rent.getRent_date())
                    .return_location(rent.getReturn_location())
                    .return_date(rent.getRent_date())
                    .rental_fee(bookRent.getRental_fee())
                    .build();

            myBookRentDtoList.add(myBookLendDto);
        }

        responseDto.setData(myBookRentDtoList);
        return responseDto;
    }

    @Override
    public ResponseDto blockRentBook(Long book_id) {

        ResponseDto responseDto = new ResponseDto();

        Book book = bookRepository.findById(book_id).orElse(null);

        if (book == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 책입니다.");
            return responseDto;
        }

        BookRent bookRent = book.getBookRent();

        if (bookRent == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("대여등록되지 않은 책입니다.");
            return responseDto;
        }

        bookRentRepository.delete(bookRent);

        responseDto.setMessage("대여등록 해제완료");
        return responseDto;
    }

}
