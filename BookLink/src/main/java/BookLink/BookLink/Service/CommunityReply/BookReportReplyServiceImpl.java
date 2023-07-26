package BookLink.BookLink.Service.CommunityReply;

import BookLink.BookLink.Domain.Community.BookReport.BookReport;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReply;
import BookLink.BookLink.Domain.CommunityReply.BookReportReply.BookReportReplyDto;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.BookReportRepository;
import BookLink.BookLink.Repository.CommunityReply.BookReportReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookReportReplyServiceImpl implements BookReportReplyService {

    private final MemberRepository memberRepository;
    private final BookReportRepository bookReportRepository;
    private final BookReportReplyRepository bookReportReplyRepository;

    @Override
    @Transactional
    public ResponseDto writeReply(String memEmail, Long postId, BookReportReplyDto.Request replyDto) {

        ResponseDto responseDto = new ResponseDto();

        Member loginMember = memberRepository.findByEmail(memEmail).orElse(null);

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");

            return responseDto;
        }

        BookReport post = bookReportRepository.findById(postId).orElse(null);

        BookReportReply savedReply;

        if (replyDto.getParentId() != 0) {

            BookReportReply parent = bookReportReplyRepository.findById(replyDto.getParentId()).orElse(null);

            if (parent == null) {
                responseDto.setMessage("존재하지 않는 부모 댓글");
                responseDto.setStatus(HttpStatus.BAD_REQUEST);
                return responseDto;
            }

            BookReportReply bookReportReply = replyDto.toEntity(post, loginMember, parent);
            bookReportReplyRepository.save(bookReportReply);
        } else {

            BookReportReply bookReportReply = replyDto.toEntity(post, loginMember, null);
            savedReply = bookReportReplyRepository.save(bookReportReply);

            //dirty checking
            BookReportReply updateReply = bookReportReplyRepository.findById(bookReportReply.getId()).orElse(new BookReportReply());
            updateReply.updateParent(savedReply);
        }

        return responseDto;
    }
}
