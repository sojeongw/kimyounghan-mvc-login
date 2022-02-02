package hello.login.domain.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // 만들어 둔 필터를 추가한다.
        filterRegistrationBean.setFilter(new LogFilter());
        // 필터는 체인으로 동작하기 때문에 순서를 지정해줘야 한다.
        filterRegistrationBean.setOrder(1);
        // 모든 url 패턴에 적용한다.
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
