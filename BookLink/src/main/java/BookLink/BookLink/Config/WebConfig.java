package BookLink.BookLink.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //CORS : Cross Origin을 열어주는것을 매 컨트롤러마다 설정 x. 아래와같이설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", "http://localhost:5173", "http://book-link.store/", "http://www.book-link.store/",
                        "http://127.0.0.1:5173","http://52.79.70.115", "https://www.book-link.store/", "https://book-link.store/")
                .allowCredentials(true)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
    }

}