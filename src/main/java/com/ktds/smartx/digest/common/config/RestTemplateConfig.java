package com.ktds.smartx.digest.common.config;

import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * packageName    : com.ktds.smartx.digest.common.config
 * fileName       : RestTemplateConfig
 * author         : Jae Gook Jung
 * date           : 2023/08/23
 * description    :
 * project name   : digest
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/23        Jae Gook Jung       최초 생성
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100)   //연결을 유지할 최대 숫자
                .setMaxConnPerRoute(20)
                .setConnectionTimeToLive(5, TimeUnit.SECONDS)
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                .disableCookieManagement() // 기본적으로 동일 request 에 대한 cookie 를 캐싱한다. 캐싱하지 않도록 설정함.
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        //TODO: RestTemplate 로깅처리
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(factory);

        return builder
                .requestFactory(() -> bufferingClientHttpRequestFactory)
                .setConnectTimeout(Duration.ofSeconds(180))
                .setReadTimeout(Duration.ofSeconds(180))
//                .additionalInterceptors() // Todo 로깅 interceptor
                .build();
    }
}
