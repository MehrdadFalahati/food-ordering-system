package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class PaymentEvent implements DomainEvent<Payment> {

    private final Payment payment;
    private final ZonedDateTime createAt;
    private final List<String> failureMessages;

    public PaymentEvent(Payment payment, ZonedDateTime createAt, List<String> failureMessages) {
        this.payment = payment;
        this.createAt = createAt;
        this.failureMessages = failureMessages;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
