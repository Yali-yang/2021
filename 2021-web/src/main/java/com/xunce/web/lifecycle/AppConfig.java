package com.xunce.web.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public Yung yung(){
        return new Yung();
    }

}
