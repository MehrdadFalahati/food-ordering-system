package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

@Component
public class OrderDataAccessMapper {



    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().amount())
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ? String.join(FAILURE_MESSAGES_DELIMITER, order.getFailureMessages()) : "")
                .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .orderItems(orderItemEntitiesToOrderItem(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(List.of(orderEntity.getFailureMessages().split(FAILURE_MESSAGES_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItem(List<OrderItemEntity> items) {
        return items.stream()
                .map(OrderDataAccessMapper::createOrderItem)
                .toList();
    }

    private static OrderItem createOrderItem(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .orderItemId(new OrderItemId(orderItemEntity.getId()))
                .product(new Product(new ProductId(orderItemEntity.getProductId())))
                .price(new Money(orderItemEntity.getPrice()))
                .quantity(orderItemEntity.getQuantity())
                .subTotal(new Money(orderItemEntity.getSubTotal()))
                .build();
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity());
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream()
                .map(OrderDataAccessMapper::createOrderItemEntity)
                .toList();
    }

    private static OrderItemEntity createOrderItemEntity(OrderItem orderItem) {
        return OrderItemEntity.builder()
                .id(orderItem.getId().getValue())
                .productId(orderItem.getProduct().getId().getValue())
                .price(orderItem.getPrice().amount())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal().amount())
                .build();
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.id())
                .street(deliveryAddress.street())
                .postalCode(deliveryAddress.postalCode())
                .city(deliveryAddress.city())
                .build();
    }
}
