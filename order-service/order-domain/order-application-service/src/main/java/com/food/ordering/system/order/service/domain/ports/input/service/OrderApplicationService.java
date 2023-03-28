package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOderResponse;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResonse;
import jakarta.validation.Valid;

public interface OrderApplicationService {
    CreateOderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResonse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
