package BookLink.BookLink.Service.BookReply;

import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Domain.BookReply.BookReply;
import BookLink.BookLink.Domain.BookReply.BookReplyDto;
import BookLink.BookLink.Domain.BookReply.BookReplyLike;
import BookLink.BookLink.Repository.Member.MemberRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyLikeRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookReplyServiceImpl implements BookReplyService {

    private final MemberRepository memberRepository;
    private final BookReplyRepository bookReplyRepository;
    private final BookReplyLikeRepository replyLikeRepository;

    @Override
    @Transactional
    public ResponseDto writeReply(String memEmail, String isbn, BookReplyDto.Request replyDto) throws MalformedURLException {

        /*
        Long bookId = bookRepository.findByIsbn(isbn);
        System.out.println("bookId = " + bookId);
         */
        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");

            return responseDto;
        }

        BookReply savedReply;

        if (replyDto.getParentId() != 0) { // 자식 댓글의 경우 parent 찾기

            // 유효한 parent인지 확인 TODO exception
            // 프론트에서 걸러지는 부분이지만 한 번 더 처리? or not
            // bookReplyRepository.findByIdAndParent(replyDto.getParentId()).orElse(null);

            BookReply parent = bookReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.OK);
                return responseDto;
            }

            BookReply bookReply = replyDto.toEntity(loginMember, isbn, parent);
            savedReply = bookReplyRepository.save(bookReply);

        } else { // 부모 댓글의 경우 불필요한 쿼리 날리지 않기 위해 null TODO refactoring 가능하면 실시

            BookReply bookReply = replyDto.toEntity(loginMember, isbn, null);
            savedReply = bookReplyRepository.save(bookReply);

            // dirty checking
            BookReply updateReply = bookReplyRepository.findById(bookReply.getId()).orElse(new BookReply());
            updateReply.updateParent(savedReply);

        }

        responseDto.setMessage("성공");
        responseDto.setStatus(HttpStatus.OK);

        BookReplyDto.Response responseData = new BookReplyDto.Response(
                savedReply.getId(),
                savedReply.getCreatedTime(),
                savedReply.getContent(),
                loginMember.getNickname(),
                // loginMember.getImage()
                new URL("https://m.blog.naver.com/yunam69/221690011454") // TODO dummy
        );
        responseDto.setData(responseData);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likeReply(String memEmail, Long replyId, String state) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        BookReply bookReply = bookReplyRepository.findById(replyId).orElse(null);

        if (bookReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하지 않는 댓글");
            return responseDto;
        }

        if (state.equals("up")) {

            BookReplyLike replyLike = BookReplyLike.builder()
                    .member(loginMember)
                    .reply(bookReply)
                    .build();

            replyLikeRepository.save(replyLike);
            bookReply.increaseLikeCnt();

            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("좋아요 성공");

            // TODO exception
            /*
            try {
                replyLikeRepository.save(replyLike);
                reply.increaseLikeCnt();

                responseDto.setStatus(HttpStatus.OK);
                responseDto.setMessage("좋아요 성공");

            } catch (Exception ex) { // SQLIntegrityConstraintViolationException

                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                responseDto.setMessage("좋아요 불가");
            }

             */

        } else { // "down"

            BookReplyLike replyLike = replyLikeRepository.findByReplyAndMember(bookReply, loginMember);

            replyLikeRepository.delete(replyLike);
            bookReply.decreaseLikeCnt();

            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("좋아요 취소 성공");

            // TODO exception
            /*
            try {
                replyLikeRepository.delete(replyLike);
                reply.decreaseLikeCnt();

                responseDto.setStatus(HttpStatus.OK);
                responseDto.setMessage("좋아요 취소 성공");

            } catch (Exception ex) { // IllegalArgumentException

                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                responseDto.setMessage("좋아요 취소 불가");
            }

             */

        }
        return responseDto;
    }

}
