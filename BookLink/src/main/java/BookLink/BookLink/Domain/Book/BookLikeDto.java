package BookLink.BookLink.Domain.Book;

import BookLink.BookLink.Domain.Member.Member;

/**
 * 사용하지 않을 수도 있는 DTO
 */

public class BookLikeDto {

    private String state;

    public BookLike toEntity(String isbn, Member member) {
        return BookLike.builder()
                .isbn(isbn)
                .member(member)
                .build();
    }

}
