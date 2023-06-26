package BookLink.BookLink.Domain.Member;

import lombok.*;

/**
 * 권한의 종류를 저장하는 DTO 입니다. (현재는 사용 X)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {
    private String authorityName;
}