package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;

public interface RestaurantDomainService {
    OrderApprovalEvent validateOrder(RestaurantServiceDto restaurantServiceDto);
}
