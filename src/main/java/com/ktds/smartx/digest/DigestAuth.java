package com.ktds.smartx.digest;

import com.ktds.smartx.digest.common.aop.custom.annotation.EnableCommonApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCommonApiResponse
@ConfigurationPropertiesScan
@SpringBootApplication
public class DigestAuth {
    @PostConstruct
    void started(){
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DigestAuth.class, args);
    }
}
