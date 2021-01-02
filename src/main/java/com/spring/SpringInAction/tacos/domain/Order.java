package com.spring.SpringInAction.tacos.domain;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class Order {

	@NotBlank(message = "빈값이 될 수 없습니다.")
	private String deliveryName;

	@NotBlank(message = "빈값이 될 수 없습니다.")
	private String deliveryStreet;

	@NotBlank(message = "빈값이 될 수 없습니다.")
	private String deliveryCity;

	@NotBlank(message = "빈값이 될 수 없습니다.")
	private String deliveryState;

	@NotBlank(message = "빈값이 될 수 없습니다.")
	private String deliveryZip;

	@CreditCardNumber(message = "올바른 형식의 카드 번호를 입력해주세요.")
	private String ccNumber;

	@Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])", message = "올바른 형식의 카드 유효기간을 입력해주세요.")
	private String ccExpiration;

	@Digits(integer = 3, fraction = 0, message = "올바른 형식의 CVV 값을 입력해주세요.")
	private String ccCVV;

}
