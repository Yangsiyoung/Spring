package com.spring.SpringInAction.tacos.dto.request.users;

import com.spring.SpringInAction.tacos.domain.user.TacoUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SignUpRequestDTO {
    @NotBlank(message = "필수입력 사항입니다.")
    private String username;

    @NotBlank(message = "필수입력 사항입니다.")
    private String password;

    @NotBlank(message = "필수입력 사항입니다.")
    private String fullName;

    private String street;
    private String city;
    private String state;
    private String zip;

    @NotBlank(message = "필수입력 사항입니다.")
    private String phone;

    public TacoUser toUser(PasswordEncoder passwordEncoder) {
        return new TacoUser(null,
                username, passwordEncoder.encode(password),
                fullName, street, city, state, zip, phone);
    }

    @Override
    public String toString() {
        return "SignUpRequestDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
