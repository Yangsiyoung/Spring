package com.spring.SpringInAction.tacos.domain.order;

import com.spring.SpringInAction.tacos.domain.taco.Taco;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "Taco_Order")
@Entity
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	//@CreditCardNumber(message = "올바른 형식의 카드 번호를 입력해주세요.")
	private String ccNumber;

	@Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])", message = "올바른 형식의 카드 유효기간을 입력해주세요.")
	private String ccExpiration;

	@Digits(integer = 3, fraction = 0, message = "올바른 형식의 CVV 값을 입력해주세요.")
	private String ccCVV;

	@ManyToMany
	private List<Taco> tacos = new ArrayList<>();

	private LocalDate placedAt;

	public void addTaco(Taco taco) {
		this.tacos.add(taco);
	}

	@PrePersist
	void initPlaceAt() {
		this.placedAt = LocalDate.now();
	}
}
