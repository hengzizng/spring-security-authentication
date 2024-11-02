package nextstep.app.config;

import nextstep.app.interceptor.BasicAuthInterceptor;
import nextstep.app.interceptor.FormLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final FormLoginInterceptor formLoginInterceptor;
    private final BasicAuthInterceptor basicAuthInterceptor;

    public WebConfig(FormLoginInterceptor formLoginInterceptor, BasicAuthInterceptor basicAuthInterceptor) {
        this.formLoginInterceptor = formLoginInterceptor;
        this.basicAuthInterceptor = basicAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(formLoginInterceptor).addPathPatterns("/login");
        registry.addInterceptor(basicAuthInterceptor).addPathPatterns("/members");
    }
}