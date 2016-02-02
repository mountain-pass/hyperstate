package au.com.mountainpass.hyperstate.server.entities;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.HyperstateController;

public class HyperstateRootEntity extends VanillaEntity {

    protected HyperstateRootEntity() {
    }

    public HyperstateRootEntity(ApplicationContext context,
            Class<? extends HyperstateController> controllerClass) {
        super(AnnotationUtils
                .findAnnotation(controllerClass, RequestMapping.class)
                .value()[0],
                controllerClass.getSimpleName(),
                HyperstateRootEntity.class.getSimpleName());
    }
}
