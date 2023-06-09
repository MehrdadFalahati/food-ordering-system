package com.food.ordering.system.payment.service.dataacces.creditentry.adapter;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.payment.service.dataacces.creditentry.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.service.dataacces.creditentry.mapper.CreditHistoryDataAccessMapper;
import com.food.ordering.system.payment.service.dataacces.creditentry.repository.CreditHistoryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.port.output.repository.CreditHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDataAccessMapper creditHistoryDataAccessMapper;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return creditHistoryDataAccessMapper
                .creditHistoryEntityToCreditHistory(creditHistoryJpaRepository
                        .save(creditHistoryDataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        Optional<List<CreditHistoryEntity>> creditHistories = creditHistoryJpaRepository.findByCustomerId(customerId.getValue());
        return creditHistories.map(creditHistoryEntities ->
                                    creditHistoryEntities.stream()
                                            .map(creditHistoryDataAccessMapper::creditHistoryEntityToCreditHistory)
                                            .toList());
    }
}
