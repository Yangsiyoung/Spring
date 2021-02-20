package com.spring.SpringInAction.study04.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private String id;
    private String password;
    private String email;

    public void comparePassword(String password) {
        if(!this.password.equals(password)) {
            throw new IllegalArgumentException();
        }
    }
}
