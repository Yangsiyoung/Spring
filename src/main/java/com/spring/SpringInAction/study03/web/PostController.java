package com.spring.SpringInAction.study03.web;

import com.spring.SpringInAction.study03.domain.Comment;
import com.spring.SpringInAction.study03.domain.Post;
import com.spring.SpringInAction.study03.hateoas.PostRepresentationModel;
import com.spring.SpringInAction.study03.hateoas.PostRepresentationModelAssemblerSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/post")
@RestController
public class PostController {

    private List<Post> posts = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (long index = 1; index <= 10; index++) {
            List<Comment> comments = new ArrayList<>();
            for(long commentIndex = 1; commentIndex <= 2; commentIndex++) {
                comments.add(new Comment("post" + index + "comment" + commentIndex, "message"));
            }
            posts.add(new Post(index, "title" + index, "content" + index, comments));
        }
    }

    @GetMapping("/recentOne")
    public EntityModel<PostRepresentationModel> recentOne() {
        PostRepresentationModel postResource = new PostRepresentationModelAssemblerSupport().toModel(posts.get(0));
        return EntityModel.of(postResource);
    }

    @GetMapping("/recent")
    public CollectionModel<PostRepresentationModel> recent() {
        return CollectionModel.of(new PostRepresentationModelAssemblerSupport().toModels(posts));
    }
}
