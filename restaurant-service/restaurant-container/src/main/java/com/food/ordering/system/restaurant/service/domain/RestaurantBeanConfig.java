package com.food.ordering.system.restaurant.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantBeanConfig {
    @Bean
    public RestaurantDomainService restaurantService() {
        return new RestaurantDomainServiceImpl();
    }
}
