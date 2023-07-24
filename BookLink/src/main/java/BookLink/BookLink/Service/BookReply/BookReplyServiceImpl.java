package BookLink.BookLink.Service.BookReply;

import BookLink.BookLink.Domain.BookReply.*;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
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
import java.time.LocalDateTime;
import java.util.Date;

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
                new URL("https://soccerquick.s3.ap-northeast-2.amazonaws.com/1689834239634.png") // TODO dummy
        );
        responseDto.setData(responseData);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto updateReply(String isbn, BookReplyUpdateDto.Request replyDto) {

        ResponseDto responseDto = new ResponseDto();

//        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);
//
//        if (loginMember == null) {
//            responseDto.setStatus(HttpStatus.BAD_REQUEST);
//            responseDto.setMessage("로그인 필요");
//
//            return responseDto;
//        }

        BookReply updateReply = bookReplyRepository.findByIdAndIsbn(replyDto.getReplyId(), isbn).orElse(null);

        if (updateReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");

            return responseDto;
        }

        if (updateReply.getContent().equals(replyDto.getContent())) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("수정된 내용 없음");

            return responseDto;
        }

        updateReply.updateReply(replyDto.getContent());

        responseDto.setMessage("댓글 수정 성공");
        responseDto.setStatus(HttpStatus.OK);

        BookReplyUpdateDto.Response responseData = new BookReplyUpdateDto.Response(
                updateReply.getContent(),
                updateReply.isUpdated()
        );
        responseDto.setData(responseData);

        return responseDto;

    }

    @Override
    @Transactional
    public ResponseDto deleteReply(String isbn, Long replyId) {

        ResponseDto responseDto = new ResponseDto();

        BookReply deleteReply = bookReplyRepository.findByIdAndIsbn(replyId, isbn).orElse(null);

        if (deleteReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        deleteReply.updateDeleted();

        BookReplyDeleteDto.Response responseData = new BookReplyDeleteDto.Response(deleteReply.isDeleted());

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("댓글 삭제 성공");
        responseDto.setData(responseData);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likeReply(String memEmail, Long replyId) {

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

        boolean is_liked = replyLikeRepository.existsByMemberAndReply(loginMember, bookReply);

        if (!is_liked) { // 좋아요 안 눌린 상태

            BookReplyLike replyLike = BookReplyLike.builder()
                    .member(loginMember)
                    .reply(bookReply)
                    .build();

            replyLikeRepository.save(replyLike);
            bookReply.increaseLikeCnt();

            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("좋아요 성공");

            BookReplyLikeDto bookReplyLikeDto = new BookReplyLikeDto(bookReply.getLike_cnt());
            responseDto.setData(bookReplyLikeDto);

            // TODO exception
            /*
            catch (Exception ex) { // SQLIntegrityConstraintViolationException

                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                responseDto.setMessage("좋아요 불가");
            }
            */

        } else { // 좋아요 눌린 상태

            BookReplyLike replyLike = replyLikeRepository.findByReplyAndMember(bookReply, loginMember);

            replyLikeRepository.delete(replyLike);
            bookReply.decreaseLikeCnt();

            responseDto.setStatus(HttpStatus.OK);
            responseDto.setMessage("좋아요 취소 성공");

            BookReplyLikeDto bookReplyLikeDto = new BookReplyLikeDto(bookReply.getLike_cnt());
            responseDto.setData(bookReplyLikeDto);

            // TODO exception
            /*
            catch (Exception ex) { // IllegalArgumentException

                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                responseDto.setMessage("좋아요 취소 불가");
            }
            */
        }
        return responseDto;
    }

}
