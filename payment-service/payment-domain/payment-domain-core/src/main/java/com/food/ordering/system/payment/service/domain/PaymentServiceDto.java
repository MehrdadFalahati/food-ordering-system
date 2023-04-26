package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.util.List;

public record PaymentServiceDto(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories,
                                List<String> failureMessages) {
}
