package com.teamnine.noFreeRider.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("live")
public class LiveDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url("jdbc:h2:file:./db")
                .driverClassName("org.h2.Driver")
                .username("sa")
                .password("password")
                .build();
    }
}