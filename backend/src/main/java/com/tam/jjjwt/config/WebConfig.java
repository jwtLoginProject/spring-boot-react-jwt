package com.tam.jjjwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 이동은
 * @version 1.0
 * @Description
 * @Modification Information
 * Created 2021/02/03
 * @
 * @ 수정일       	수정자        수정내용
 * @ ———   			————    	—————————————
 * @ 2021/02/03     이동은        최초 작성
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //운영 환경에 배포할 경우에는 주석 처리
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true)
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}
