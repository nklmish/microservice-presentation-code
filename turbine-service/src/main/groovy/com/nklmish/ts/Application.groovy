package com.nklmish.ts

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.turbine.amqp.EnableTurbineAmqp
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbineAmqp
class Application {

    @Bean
    @Profile("docker")
    public ConnectionFactory rabbitMq(@Value('${app.rabbitmq.host}') String host) {
        return new CachingConnectionFactory(host)
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application).run(args)
    }
}

