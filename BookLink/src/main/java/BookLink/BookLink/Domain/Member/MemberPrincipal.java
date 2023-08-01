package BookLink.BookLink.Domain.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class MemberPrincipal implements UserDetails {

    private final Member member;

    public Member getMember() {
        return this.member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // Role role = this.member.getRole();
        authorities.add(new SimpleGrantedAuthority("USER")); // TODO set role
        return authorities;
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }
    @Override
    public String getUsername() {
        return member.getName();
    }
    @Override // 계정 유효한지
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override // 계정 잠기지 않았는지
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override // 비밀번호 유효한지
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override // 사용자 활성화 상태인지
    public boolean isEnabled() {
        return true;
    }
}
