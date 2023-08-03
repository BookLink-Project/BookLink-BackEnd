package BookLink.BookLink.Service.Community.FreeBoard;

import BookLink.BookLink.Domain.Community.FreeBoard.*;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardRepliesDto;
import BookLink.BookLink.Domain.CommunityReply.FreeBoardReply.FreeBoardReply;
import BookLink.BookLink.Domain.Member.Member;
import BookLink.BookLink.Domain.ResponseDto;
import BookLink.BookLink.Repository.Community.FreeBoard.FreeBoardLikeRepository;
import BookLink.BookLink.Repository.Community.FreeBoard.FreeBoardRepository;
import BookLink.BookLink.Repository.CommunityReply.FreeBoard.FreeBoardReplyLikeRepository;
import BookLink.BookLink.Repository.CommunityReply.FreeBoard.FreeBoardReplyRepository;
import BookLink.BookLink.Repository.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {

    private final MemberRepository memberRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardLikeRepository freeBoardLikeRepository;
    private final FreeBoardReplyRepository freeBoardReplyRepository;
    private final FreeBoardReplyLikeRepository freeBoardReplyLikeRepository;

    @Override
    public ResponseDto writePost(Member loginMember, FreeBoardDto.Request freeBoardDto) {

        ResponseDto responseDto = new ResponseDto();

        FreeBoard freeBoard = freeBoardDto.toEntity(loginMember);

        try {
            freeBoardRepository.save(freeBoard);

        } catch (Exception ex) {

            log.info("error={}", ex);

            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("입력 미완료");

            return responseDto;
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("성공");

        return responseDto;
    }

    @Override
    public ResponseDto freeBoardList() {

        ResponseDto responseDto = new ResponseDto();

        List<FreeBoard> all_freeBoard = freeBoardRepository.findAll();
        List<FreeBoardDto.Response> free_response = new ArrayList<>();

        for (int i = 0; i < all_freeBoard.size(); i++) {
            FreeBoardDto.Response response = FreeBoardDto.Response.toDto(all_freeBoard.get(i));
            free_response.add(i, response);
        }

        responseDto.setStatus(HttpStatus.OK);
        responseDto.setMessage("자유글 목록 조회입니다.");
        responseDto.setData(free_response);

        return responseDto;

    }

    @Override
    public ResponseDto freeBoardDetail(Long id, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        FreeBoard post = freeBoardRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }


        post.view_plus(); // 조회수 증가

        boolean isLiked = freeBoardLikeRepository.existsByMemberAndPost(loginMember, post);

        // 댓글 조회
        List<FreeBoardReply> replyList = freeBoardReplyRepository.findByPostOrderByParentDescIdDesc(post);

        List<FreeBoardRepliesDto> replies = new ArrayList<>();

        for (FreeBoardReply reply : replyList) {

            Long parentId = reply.getParent().getId();
            Long replyId = reply.getId();
            Member writer = reply.getWriter();

            Long sub_reply_cnt = parentId.equals(replyId) ? freeBoardReplyRepository.countByParentId(parentId) - 1 : 0; // 대댓글 수

            // 좋아요 상태
            boolean isLikedReply = freeBoardReplyLikeRepository.existsByMemberAndReply(loginMember, reply);

            FreeBoardRepliesDto rv;

            rv = new FreeBoardRepliesDto(
                    replyId,
                    parentId,
                    writer.getNickname(),
                    reply.getContent(),
                    reply.getCreatedTime(),
                    writer.getImage(),
                    reply.getLike_cnt(),
                    sub_reply_cnt,
                    isLikedReply,
                    reply.isUpdated()
            );
            replies.add(rv);
        }

        //
        FreeBoardDetailDto result = new FreeBoardDetailDto(
                post.getCategory(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedTime(),
                post.getWriter().getNickname(),
                post.getWriter().getImage(),
                post.getView_cnt(),
                post.getLike_cnt(),
                post.getReply_cnt(),
                isLiked,
                post.isUpdated(),
                replies

        );
        responseDto.setData(result);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto freeBoardUpdate(Long id, FreeBoardUpdateDto freeBoardUpdateDto) {

        ResponseDto responseDto = new ResponseDto();

        FreeBoard updatePost = freeBoardRepository.findById(id).orElse(null);

        if (updatePost == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        String newTitle = freeBoardUpdateDto.getTitle();
        String newContent = freeBoardUpdateDto.getContent();

        updatePost.updatePost(newTitle, newContent);

        responseDto.setStatus(HttpStatus.CREATED);

        freeBoardUpdateDto.setTitle(newTitle);
        freeBoardUpdateDto.setContent(newContent);
        responseDto.setData(freeBoardUpdateDto);

        return responseDto;
    }

    @Override
    public ResponseDto deletePost(Long id) {

        ResponseDto responseDto = new ResponseDto();

        FreeBoard post = freeBoardRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글");
            return responseDto;
        }

        freeBoardRepository.deleteById(id);

        responseDto.setStatus(HttpStatus.NO_CONTENT);

        return responseDto;
    }

    @Override
    @Transactional
    public ResponseDto likePost(Long id, Member loginMember) {

        ResponseDto responseDto = new ResponseDto();

        if (loginMember == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("로그인 필요");
            return responseDto;
        }

        FreeBoard post = freeBoardRepository.findById(id).orElse(null);

        if (post == null) {
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setMessage("없는 글입니다");
            return responseDto;
        }

        FreeBoardLike freeBoardLike = freeBoardLikeRepository.findByMemberAndPost(loginMember, post).orElse(null);

        if (freeBoardLike != null) { // 좋아요 눌린 상태
            freeBoardLikeRepository.delete(freeBoardLike);
            post.like_minus();

            responseDto.setMessage("좋아요 취소 성공");
        } else { // 좋아요 안눌린 상태
            freeBoardLike = FreeBoardLike.builder()
                    .post(post)
                    .member(loginMember)
                    .build();

            freeBoardLikeRepository.save(freeBoardLike);
            post.like_plus();

            responseDto.setMessage("좋아요 성공");
        }

        FreeBoardLikeDto freeBoardLikeDto = new FreeBoardLikeDto(post.getLike_cnt());
        responseDto.setData(freeBoardLikeDto);

        return responseDto;
    }

}
