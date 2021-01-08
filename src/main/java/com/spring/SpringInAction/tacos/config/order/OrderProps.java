package com.spring.SpringInAction.tacos.config.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Setter
@Getter
@ConfigurationProperties(prefix = "taco.orders")
@Component
@Validated
public class OrderProps {
    // taco.orders.pageSize 값을 가져오지만 혹시나 해당 값이 없다면 20이 기본 값
    @Min(value = 5, message = "Must be between 5 and 25")
    @Max(value = 25, message = "Must be between 5 and 25")
    private int pageSize = 20;
}
