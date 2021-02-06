package com.spring.SpringInAction.study03.hateoas;

import com.spring.SpringInAction.study03.domain.Post;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.ArrayList;
import java.util.List;

@Relation(value = "post", collectionRelation = "memberPosts")
@Getter
public class PostRepresentationModel extends RepresentationModel<PostRepresentationModel> {
    private Long postId;

    private String title;

    private String contents;

    private List<CommentRepresentationModel> comments = new ArrayList<>();

    public PostRepresentationModel(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.comments = new CommentRepresentationModelAssemblerSupport().toModels(post.getComments());
    }
}
