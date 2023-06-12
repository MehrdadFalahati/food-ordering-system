package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;

import java.util.List;

public record RestaurantServiceDto(Restaurant restaurant,
                                   List<String> failureMessages,
                                   DomainEventPublisher<OrderApprovedEvent> publisher,
                                   DomainEventPublisher<OrderRejectedEvent> rejectedPublisher) {
}
