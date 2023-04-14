package com.food.ordering.system.application.handler;

import lombok.Builder;


@Builder
public record ErrorDto(String code, String message) {
}
