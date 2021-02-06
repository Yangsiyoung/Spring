package com.spring.SpringInAction.study03.hateoas;

import com.spring.SpringInAction.study03.domain.Comment;
import com.spring.SpringInAction.study03.web.CommentController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;
import java.util.stream.Collectors;

public class CommentRepresentationModelAssemblerSupport extends RepresentationModelAssemblerSupport<Comment, CommentRepresentationModel> {
    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public CommentRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<CommentRepresentationModel> resourceType) {
        super(controllerClass, resourceType);
    }

    public CommentRepresentationModelAssemblerSupport() {
        super(CommentController.class, CommentRepresentationModel.class);
    }

    @Override
    protected CommentRepresentationModel instantiateModel(Comment entity) {
        return new CommentRepresentationModel(entity);
    }

    @Override
    public CommentRepresentationModel toModel(Comment entity) {
        return createModelWithId(entity.getCommentId(), entity);
    }

    public List<CommentRepresentationModel> toModels(List<Comment> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }
}
