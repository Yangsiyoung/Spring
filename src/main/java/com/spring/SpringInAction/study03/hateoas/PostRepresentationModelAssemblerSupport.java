package com.spring.SpringInAction.study03.hateoas;

import com.spring.SpringInAction.study03.domain.Post;
import com.spring.SpringInAction.study03.web.PostController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;
import java.util.stream.Collectors;

public class PostRepresentationModelAssemblerSupport extends RepresentationModelAssemblerSupport<Post, PostRepresentationModel> {

    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
    public PostRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<PostRepresentationModel> resourceType) {
        super(PostController.class, PostRepresentationModel.class);
    }

    public PostRepresentationModelAssemblerSupport() {
        super(PostController.class, PostRepresentationModel.class);
    }

    @Override
    protected PostRepresentationModel instantiateModel(Post entity) {
        return new PostRepresentationModel(entity);
    }

    @Override
    public PostRepresentationModel toModel(Post entity) {
        return createModelWithId(entity.getPostId(), entity);
    }

    public List<PostRepresentationModel> toModels(List<Post> entities) {
        return entities.stream().map((entity) -> createModelWithId(entity.getPostId(), entity)).collect(Collectors.toList());
    }
}
