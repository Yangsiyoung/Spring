package com.spring.SpringInAction.tacos.dto.request.users;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDTO {

    @NotBlank
    private String userID;

    @NotBlank
    private String password;

}
