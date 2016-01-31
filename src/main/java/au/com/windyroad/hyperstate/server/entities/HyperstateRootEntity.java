package au.com.windyroad.hyperstate.server.entities;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.entities.VanillaEntity;
import au.com.windyroad.hyperstate.server.HyperstateController;

public class HyperstateRootEntity extends VanillaEntity {

    protected HyperstateRootEntity() {
    }

    public HyperstateRootEntity(ApplicationContext context,
            Class<? extends HyperstateController> controllerClass) {
        super(context,
                AnnotationUtils
                        .findAnnotation(controllerClass, RequestMapping.class)
                        .value()[0],
                controllerClass.getSimpleName(),
                HyperstateRootEntity.class.getSimpleName());
    }
}
