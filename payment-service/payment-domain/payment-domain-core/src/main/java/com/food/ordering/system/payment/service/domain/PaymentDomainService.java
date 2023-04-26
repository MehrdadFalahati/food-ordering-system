package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.event.PaymentEvent;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(PaymentServiceDto paymentServiceDto);

    PaymentEvent validateAndCancelPayment(PaymentServiceDto paymentServiceDto);
}
