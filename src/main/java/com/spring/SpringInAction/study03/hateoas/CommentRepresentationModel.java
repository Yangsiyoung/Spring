package com.spring.SpringInAction.study03.hateoas;

import com.spring.SpringInAction.study03.domain.Comment;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "comment", collectionRelation = "postComments")
@Getter
public class CommentRepresentationModel extends RepresentationModel<CommentRepresentationModel> {
    private String commentId;
    private String message;

    public CommentRepresentationModel(Comment entity) {
        this.commentId = entity.getCommentId();
        this.message = entity.getMessage();
    }
}
