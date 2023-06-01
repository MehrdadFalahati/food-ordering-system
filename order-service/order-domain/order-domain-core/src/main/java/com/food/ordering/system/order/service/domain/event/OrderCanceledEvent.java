package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCanceledEvent extends OrderEvent {
    private final DomainEventPublisher<OrderCanceledEvent> orderCanceledEventDomainEventPublisher;

    public OrderCanceledEvent(Order order, ZonedDateTime createAt, DomainEventPublisher<OrderCanceledEvent> orderCanceledEventDomainEventPublisher) {
        super(order, createAt);
        this.orderCanceledEventDomainEventPublisher = orderCanceledEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderCanceledEventDomainEventPublisher.publish(this);
    }
}
