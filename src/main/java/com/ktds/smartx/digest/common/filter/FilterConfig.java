package com.ktds.smartx.digest.common.filter;

/**
 * packageName    : com.ktds.smartx.digest.common.filter
 * fileName       : FilterConfig
 * author         : Jae Gook Jung
 * date           : 2023/09/04
 * description    :
 * project name   : digitaltwin-digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/04        Jae Gook Jung       최초 생성
 */
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<LoggingFilter> filter1(){
        FilterRegistrationBean<LoggingFilter> bean = new FilterRegistrationBean<>(new LoggingFilter());
        bean.addUrlPatterns("/*");                                      // 모든 요청에 대해서 적용
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);                      // spring security관련 필터가 최전방에서 작업하기에 그 앞단에 로깅필터가 설칠 수 있게 설정

        return bean;
    }


}
