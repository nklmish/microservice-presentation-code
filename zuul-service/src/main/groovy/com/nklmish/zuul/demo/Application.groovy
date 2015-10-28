package com.nklmish.zuul.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication
@EnableZuulProxy
class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application).web(true).run(args)
    }

}
