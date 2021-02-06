package com.spring.SpringInAction.study03.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {
    private Long postId;
    private String title;
    private String contents;
    private List<Comment> comments;
}
