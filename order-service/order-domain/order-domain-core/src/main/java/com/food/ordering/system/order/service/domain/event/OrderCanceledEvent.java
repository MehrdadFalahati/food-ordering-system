package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCanceledEvent extends OrderEvent {
    public OrderCanceledEvent(Order order, ZonedDateTime createAt) {
        super(order, createAt);
    }
}
