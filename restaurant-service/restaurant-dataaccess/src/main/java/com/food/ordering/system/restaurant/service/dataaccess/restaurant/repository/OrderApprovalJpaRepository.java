package com.food.ordering.system.restaurant.service.dataaccess.restaurant.repository;

import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID> {
}
