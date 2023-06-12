package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    @Override
    public OrderApprovalEvent validateOrder(RestaurantServiceDto restaurantServiceDto) {
        Restaurant restaurant = restaurantServiceDto.restaurant();
        List<String> failureMessages = restaurantServiceDto.failureMessages();
        restaurant.validateOrder(failureMessages);
        log.info("Order validation with id {}", restaurant.getOrderDetail().getId());
        if (failureMessages.isEmpty()) {
            log.info("Order validation with id {} is successful", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(),
                    failureMessages, ZonedDateTime.now(ZoneId.of(UTC)), restaurantServiceDto.publisher());
        } else {
            log.info("Order validation with id {} is failed", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(),
                    failureMessages, ZonedDateTime.now(), restaurantServiceDto.rejectedPublisher());
        }

    }
}
