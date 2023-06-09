package com.food.ordering.system.payment.service.dataacces.credithistory.repository;

import com.food.ordering.system.payment.service.dataacces.credithistory.entity.CreditEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {
    Optional<CreditEntryEntity> findByCustomerId(UUID customerId);
}
