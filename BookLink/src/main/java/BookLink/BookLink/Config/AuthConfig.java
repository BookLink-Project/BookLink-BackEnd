package BookLink.BookLink.Config;


import BookLink.BookLink.Repository.Token.RefreshTokenRepository;
//import BookLink.BookLink.Service.OAuth.CustomOAuth2Member;
//import BookLink.BookLink.Service.OAuth.Service.CustomOAuth2MemberService;
//import BookLink.BookLink.Service.OAuth.handler.OAuth2LoginFailureHandler;
//import BookLink.BookLink.Service.OAuth.handler.OAuth2LoginSuccessHandler;
import BookLink.BookLink.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
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
//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
//    private final CustomOAuth2MemberService customOAuth2MemberService;
    // private final JwtFilter jwtFilter; // 생성자 대신

    @Bean
    public PasswordEncoder passwordEncoder() { // 회원가입 시 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                    .httpBasic().disable()
                    .csrf().disable()
                    .cors()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/members/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/login/oauth2/code/**").permitAll()
//                    .antMatchers("/api/v1/books/**").permitAll()
//                    .antMatchers("/api/v1/communities/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/**").authenticated()
                .and()
                    .sessionManagement() // 세션을 사용하지 않기 때문에 STATELESS
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                    .oauth2Login()
//                    .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
//                    .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
//                    .userInfoEndpoint().userService(customOAuth2MemberService) // customUserService 설정
                .and()
                    .addFilterBefore(new JwtFilter(jwtUtil, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class)
//        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);
                .build();
    }




}
