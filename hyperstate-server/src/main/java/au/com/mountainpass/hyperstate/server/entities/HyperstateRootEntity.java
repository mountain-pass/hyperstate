package au.com.mountainpass.hyperstate.server.entities;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.HyperstateController;

public class HyperstateRootEntity extends VanillaEntity {

    private RepositoryResolver resolver;

    protected HyperstateRootEntity() {
    }

    public HyperstateRootEntity(RepositoryResolver resolver,
            final Class<? extends HyperstateController> controllerClass) {
        super(resolver,
                AnnotationUtils
                        .findAnnotation(controllerClass, RequestMapping.class)
                        .value()[0],
                controllerClass.getSimpleName(),
                HyperstateRootEntity.class.getSimpleName());
        this.resolver = resolver;
    }

}
