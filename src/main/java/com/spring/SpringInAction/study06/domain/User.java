package com.spring.SpringInAction.study06.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long idx;

    private String nickname;

    private String email;

    @CreatedDate
    private LocalDate signUpDate;

    @Builder
    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
