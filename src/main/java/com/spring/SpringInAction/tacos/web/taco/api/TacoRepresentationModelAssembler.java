package com.spring.SpringInAction.tacos.web.taco.api;

import com.spring.SpringInAction.tacos.domain.taco.Taco;
import com.spring.SpringInAction.tacos.web.order.DesignTacoController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

// ResourceAssemblerSupport -> RepresentationModelAssemblerSupport
public class TacoRepresentationModelAssembler extends RepresentationModelAssemblerSupport<Taco, TacoResource> {

    public TacoRepresentationModelAssembler() {
        super(DesignTacoController.class, TacoResource.class);
    }

    @Override
    protected TacoResource instantiateModel(Taco entity) {
        return new TacoResource(entity);
    }

    @Override
    public TacoResource toModel(Taco entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
