package com.spring.SpringInAction.study01;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class Order {
    @NotNull(message = "ordererName is null")
    @Size(min=2, message = "Name is must be at least 2 characters long")
    private String ordererName;

    @NotEmpty(message = "menu is empty")
    private String menu;

    @NotBlank(message = "address is blank")
    private String address;

    @Size(min = 1, message = "Beverage is must be at least 1")
    private String[] beverages;

    @Digits(integer = 3, fraction = 0, message = "zipCode length is over than 3")
    private String zipCode;
}
