package com.nklmish.cs.catalogs

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication
@EnableHystrix
@EnableDiscoveryClient
class Application {

    @Bean
    @Profile("docker")
    public ConnectionFactory createRabbitMqConnectionFactory(@Value('${app.rabbitmq.host}') String host) {
       return new CachingConnectionFactory(host)
    }

    public static void main(String[] args) {
        SpringApplication.run(Application, args)
    }
}
