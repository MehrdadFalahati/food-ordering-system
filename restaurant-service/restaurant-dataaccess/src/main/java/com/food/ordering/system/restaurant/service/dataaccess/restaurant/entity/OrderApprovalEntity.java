package com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_approval", schema = "restaurant")
@Entity
public class OrderApprovalEntity {

    @Id
    private UUID id;
    private UUID restaurantId;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private OrderApprovalStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderApprovalEntity that = (OrderApprovalEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
