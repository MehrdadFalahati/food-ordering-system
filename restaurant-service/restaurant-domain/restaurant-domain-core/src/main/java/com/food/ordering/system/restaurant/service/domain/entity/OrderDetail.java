package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;

import java.util.List;

public class OrderDetail extends BaseEntity<OrderId> {
    private OrderStatus status;
    private Money totalAmount;
    private final List<Product> products;

    private OrderDetail(Builder builder) {
        setId(builder.orderId);
        status = builder.status;
        totalAmount = builder.totalAmount;
        products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public List<Product> getProducts() {
        return products;
    }


    public static final class Builder {
        private OrderId orderId;
        private OrderStatus status;
        private Money totalAmount;
        private List<Product> products;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder status(OrderStatus val) {
            status = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}
