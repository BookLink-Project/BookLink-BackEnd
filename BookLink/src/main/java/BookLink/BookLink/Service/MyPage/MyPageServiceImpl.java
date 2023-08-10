package BookLink.BookLink.Service.MyPage;

import BookLink.BookLink.Domain.Book.Book;
import BookLink.BookLink.Domain.Book.BookRecordDto;
import BookLink.BookLink.Domain.Book.BookRent;
import BookLink.BookLink.Domain.Book.Rent;
import BookLink.BookLink.Domain.Common.RentStatus;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.MyPage.*;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Book.BookRentRepository;
import BookLink.BookLink.Repository.Book.BookRepository;
import BookLink.BookLink.Repository.Book.RentRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookRentRepository bookRentRepository;
    private final RentRepository rentRepository;

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
    public ResponseDto updateAccount(AccountDto.Request accountDto, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        Member selectedMember = memberRepository.findById(loginMember.getId()).orElse(null);

        selectedMember.updateAccount(
                accountDto.getImage(),
                accountDto.getName(),
                accountDto.getNickname(),
                accountDto.getEmail(),
                passwordEncoder.encode(accountDto.getPassword()),
                accountDto.getBirth(),
                accountDto.getAddress()
//                accountDto.getCard()
        );

        responseDto.setStatus(HttpStatus.CREATED);
        return responseDto;
    }

    @Override
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

        for (Rent rent : rents) {

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

            RentStatus rent_status = bookRent.getRent_status();

            if (rent_status == RentStatus.RENTING) {
                rent_cnt += 1;
            } else {
                rent_available_cnt += 1;
            }

            MyRecordBookDto myRecordBookDto = MyRecordBookDto.builder()
                    .cover(book.getCover())
                    .title(book.getTitle())
                    .authors(book.getAuthors())
                    .publisher(book.getPublisher())
                    .rental_fee(bookRent.getRental_fee())
                    .max_date(bookRent.getMax_date())
                    .build();

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

        List<Rent> chunkRent = rents.subList((page) * chunkSize, (page + 1) * chunkSize);

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

        List<Rent> chunkRent = rents.subList((page) * chunkSize, (page + 1) * chunkSize);

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

}
