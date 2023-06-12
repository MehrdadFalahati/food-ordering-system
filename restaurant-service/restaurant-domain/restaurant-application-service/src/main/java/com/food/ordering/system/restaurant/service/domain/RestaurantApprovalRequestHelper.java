package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestHelper {
    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest request) {
        log.info("Persisting order approval request: {}", request);
        List<String> failureMessages = new ArrayList<>();
        var restaurant = findRestaurant(request);
        var event = restaurantDomainService.validateOrder
                (new RestaurantServiceDto(restaurant, failureMessages, orderApprovedMessagePublisher, orderRejectedMessagePublisher));

        orderApprovalRepository.save(restaurant.getOrderApproval());
        return event;

    }

    private Restaurant findRestaurant(RestaurantApprovalRequest request) {
        var restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(request);
        var resultRestaurant =  restaurantRepository.findRestaurantInformation(restaurant).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant not found")
        );
        restaurant.setActive(resultRestaurant.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product -> {
            resultRestaurant.getOrderDetail().getProducts().forEach(p -> {
                if (p.getId().equals(product.getId())) {
                    p.updateWithConfirmedNamePriceAndAvailability(p.getName(),p.getPrice(), p.isAvailable());
                }});
        });
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(request.getOrderId())));
        return restaurant;
    }
}
