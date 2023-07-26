package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.Community.BookClub.BookClub;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReply;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyDto;
import BookLink.BookLink.Domain.CommunityReply.BookClubReply.BookClubReplyUpdateDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.CommunityReply.BookClubReplyRepository;
import BookLink.BookLink.Repository.Community.BookClubRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class BookClubReplyServiceImpl implements BookClubReplyService{

    private final MemberRepository memberRepository;
    private final BookClubRepository bookClubRepository;
    private final BookClubReplyRepository bookClubReplyRepository;


    @Override
    @Transactional
    public ResponseDto writeReply(String memEmail, Long postId, BookClubReplyDto.Request replyDto) throws MalformedURLException {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");

            return responseDto;
        }

        BookClub post = bookClubRepository.findById(postId).orElse(null);

        BookClubReply savedReply;

        if (replyDto.getParentId() != 0) { // 자식 댓글의 경우

            BookClubReply parent = bookClubReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                return responseDto;
            }

            BookClubReply bookClubReply = replyDto.toEntity(post, loginMember, parent);
            savedReply = bookClubReplyRepository.save(bookClubReply);

        } else { // 부모 댓글의 경우

            BookClubReply bookClubReply = replyDto.toEntity(post, loginMember, null);
            savedReply = bookClubReplyRepository.save(bookClubReply);

            // dirty checking
            BookClubReply updateReply = bookClubReplyRepository.findById(bookClubReply.getId()).orElse(new BookClubReply());
            updateReply.updateParent(savedReply);

        }

        BookClubReplyDto.Response responseData = new BookClubReplyDto.Response(
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
    public ResponseDto updateReply(Long postId, Long replyId, BookClubReplyUpdateDto replyDto) {

        ResponseDto responseDto = new ResponseDto();

        BookClubReply updateReply = bookClubReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

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

        replyDto.setContent(updateReply.getContent());

        responseDto.setStatus(HttpStatus.CREATED);
        responseDto.setData(replyDto);

        return responseDto;

    }

    @Override
    @Transactional
    public ResponseDto deleteReply(Long postId, Long replyId) {

        ResponseDto responseDto = new ResponseDto();

        BookClubReply deleteReply = bookClubReplyRepository.findByIdAndPostId(replyId, postId).orElse(null);

        if (deleteReply == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 댓글");
            return responseDto;
        }

        deleteReply.updateDeleted();

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }
}
