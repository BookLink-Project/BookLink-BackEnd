package BookLink.BookLink.Service.BookReply;

import BookLink.BookLink.Domain.BookReply.*;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.BookReply.BookReplyLikeRepository;
import BookLink.BookLink.Repository.BookReply.BookReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookReplyServiceImpl implements BookReplyService {

    private final BookReplyRepository bookReplyRepository;
    private final BookReplyLikeRepository replyLikeRepository;

    @Override
    @Transactional
    public ResponseDto writeReply(Member loginMember, String isbn, BookReplyDto.Request replyDto) {

        ResponseDto responseDto = new ResponseDto();

        BookReply savedReply;

        if (replyDto.getParentId() != 0) { // 자식 댓글의 경우 parent 찾기

            BookReply parent = bookReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                return responseDto;
            }

            BookReply bookReply = replyDto.toEntity(loginMember, isbn, parent);
            savedReply = bookReplyRepository.save(bookReply);

        } else { // 부모 댓글의 경우 불필요한 쿼리 날리지 않기 위해 null

            BookReply bookReply = replyDto.toEntity(loginMember, isbn, null);
            savedReply = bookReplyRepository.save(bookReply);

            // dirty checking
            BookReply updateReply = bookReplyRepository.findById(bookReply.getId()).orElse(new BookReply());
            updateReply.updateParent(savedReply);
        }

        BookReplyDto.Response responseData = new BookReplyDto.Response(
                savedReply.getId(),
                savedReply.getCreatedTime(),
                savedReply.getContent(),
                loginMember.getNickname(),
                loginMember.getImage()
        );
        responseDto.setData(responseData);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto updateReply(String isbn, Long replyId, BookReplyUpdateDto replyDto) {

        ResponseDto responseDto = new ResponseDto();

        BookReply updateReply = bookReplyRepository.findByIdAndIsbn(replyId, isbn).orElse(null);

        if (updateReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        updateReply.updateReply(replyDto.getContent());

        replyDto.setContent(updateReply.getContent());

        responseDto.setStatus(HttpStatus.CREATED);
        responseDto.setData(replyDto);

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

        bookReplyRepository.deleteById(replyId);

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likeReply(Member loginMember, Long replyId, String isbn) {

        ResponseDto responseDto = new ResponseDto();

        BookReply bookReply = bookReplyRepository.findByIdAndIsbn(replyId, isbn).orElse(null);

        if (bookReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("존재하지 않는 댓글");
            return responseDto;
        }

        BookReplyLike replyLike = replyLikeRepository.findByReplyAndMember(bookReply, loginMember).orElse(null);

        if (replyLike == null) { // 좋아요 안 눌린 상태

            replyLike = BookReplyLike.builder()
                    .member(loginMember)
                    .reply(bookReply)
                    .build();

            replyLikeRepository.save(replyLike);
            bookReply.increaseLikeCnt();

            responseDto.setMessage("좋아요 성공");

        } else { // 좋아요 눌린 상태

            replyLikeRepository.delete(replyLike);
            bookReply.decreaseLikeCnt();

            responseDto.setMessage("좋아요 취소 성공");
        }

        BookReplyLikeDto bookReplyLikeDto = new BookReplyLikeDto(bookReply.getLike_cnt());
        responseDto.setData(bookReplyLikeDto);

        return responseDto;
    }
}
