package BookLink.BookLink.Config;


import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
import BookLink.BookLink.Service.Member.MemberPrincipalService;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberPrincipalService memberPrincipalService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()

                .authorizeRequests()
                .antMatchers("/api/v1/members/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login/oauth2/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                .and()

                .sessionManagement() // 세션을 사용하지 않기 때문에 STATELESS
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        httpSecurity.addFilterBefore(new JwtFilter(jwtUtil, refreshTokenRepository, memberPrincipalService),
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
