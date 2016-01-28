package au.com.windyroad.hyperstate.server;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.VanillaEntity;

class HyperstateRootEntity extends VanillaEntity {

    /**
     * 
     */
    public HyperstateRootEntity() {
    }

    public HyperstateRootEntity(ApplicationContext context,
            EntityRepository repository,
            Class<? extends HyperstateController> controllerClass) {
        super(context, repository,
                AnnotationUtils
                        .findAnnotation(controllerClass, RequestMapping.class)
                        .value()[0],
                controllerClass.getSimpleName());
    }
}