package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {
    @Override
    public PaymentEvent validateAndInitiatePayment(PaymentServiceDto paymentServiceDto) {
        paymentServiceDto.payment().validatePayment(paymentServiceDto.failureMessages());
        paymentServiceDto.payment().initializePayment();
        validateCreditEntry(paymentServiceDto);
        subtractCreditEntry(paymentServiceDto);
        updateCreditHistory(paymentServiceDto, TransactionType.DEBIT);
        validateCreditHistory(paymentServiceDto);

        if (paymentServiceDto.failureMessages().isEmpty()) {
            log.info("Payment is initiated for order id: {}", paymentServiceDto.payment().getOrderId().getValue());
            paymentServiceDto.payment().updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(paymentServiceDto.payment(), ZonedDateTime.now(ZoneId.of(UTC)));
        }

        log.info("Payment is initiation is failed order id: {}", paymentServiceDto.payment().getOrderId().getValue());
        paymentServiceDto.payment().updateStatus(PaymentStatus.FAILED);
        return new PaymentFailedEvent(paymentServiceDto.payment(), ZonedDateTime.now(ZoneId.of(UTC)), paymentServiceDto.failureMessages());
    }

    @Override
    public PaymentEvent validateAndCancelPayment(PaymentServiceDto paymentServiceDto) {
        paymentServiceDto.payment().validatePayment(paymentServiceDto.failureMessages());
        addCreditEntry(paymentServiceDto);
        updateCreditHistory(paymentServiceDto, TransactionType.CREDIT);
        if (paymentServiceDto.failureMessages().isEmpty()) {
            log.info("Payment is cancelled for order id: {}", paymentServiceDto.payment().getOrderId().getValue());
            paymentServiceDto.payment().updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(paymentServiceDto.payment(), ZonedDateTime.now(ZoneId.of(UTC)));
        }

        log.info("Payment is cancellation is failed order id: {}", paymentServiceDto.payment().getOrderId().getValue());
        paymentServiceDto.payment().updateStatus(PaymentStatus.FAILED);
        return new PaymentFailedEvent(paymentServiceDto.payment(), ZonedDateTime.now(ZoneId.of(UTC)), paymentServiceDto.failureMessages());
    }

    private void validateCreditEntry(PaymentServiceDto paymentServiceDto) {
        Money paymentPrice = paymentServiceDto.payment().getPrice();
        Money totalCreditAmount = paymentServiceDto.creditEntry().getTotalCreditAmount();
        if (paymentPrice.isGreaterThan(totalCreditAmount)) {
            UUID customerIdValue = paymentServiceDto.payment().getCustomerId().getValue();
            log.error("Customer with id: {} doesn't have enough credit for payment",
                    customerIdValue);
            paymentServiceDto.failureMessages().add("Customer with id: "+ customerIdValue + "doesn't have enough credit for payment");
        }
    }

    private void subtractCreditEntry(PaymentServiceDto paymentServiceDto) {
        paymentServiceDto.creditEntry().subtractCreditAmount(paymentServiceDto.payment().getPrice());
    }

    private void updateCreditHistory(PaymentServiceDto paymentServiceDto, TransactionType transactionType) {
        paymentServiceDto.creditHistories().add(
                CreditHistory.builder()
                        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                        .customerId(paymentServiceDto.payment().getCustomerId())
                        .amount(paymentServiceDto.payment().getPrice())
                        .transactionType(transactionType)
                        .build()
        );
    }

    private void validateCreditHistory(PaymentServiceDto paymentServiceDto) {
        Money totalCreditHistory = getTotalHistoryAmount(paymentServiceDto.creditHistories(), TransactionType.CREDIT);
        Money totalDebitHistory = getTotalHistoryAmount(paymentServiceDto.creditHistories(), TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            UUID customerIdValue = paymentServiceDto.creditEntry().getCustomerId().getValue();
            log.error("Customer with id: {} doesn't have enough credit according to credit history",
                    customerIdValue);
            paymentServiceDto.failureMessages().add("Customer with id: " + customerIdValue + " doesn't have enough credit according to credit history");
        }

        if (paymentServiceDto.creditEntry().getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            UUID customerIdValue = paymentServiceDto.creditEntry().getCustomerId().getValue();
            log.error("Credit history total is not equal to current credit for customer id: {}!", customerIdValue);
            paymentServiceDto.failureMessages().add("Credit history total is not equal to current credit for customer id: " + customerIdValue + "!");
        }
    }

    private static Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void addCreditEntry(PaymentServiceDto paymentServiceDto) {
        paymentServiceDto.creditEntry().addCreditAmount(paymentServiceDto.payment().getPrice());
    }
}
