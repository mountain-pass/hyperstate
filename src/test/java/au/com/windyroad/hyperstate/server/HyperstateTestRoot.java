package au.com.windyroad.hyperstate.server;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.windyroad.hyperstate.core.EntityRepository;
import au.com.windyroad.hyperstate.core.entities.VanillaEntity;
import au.com.windyroad.hyperstate.server.HyperstateController;

class HyperstateTestRoot extends VanillaEntity {

    /**
     * 
     */
    public HyperstateTestRoot() {
    }

    public HyperstateTestRoot(ApplicationContext context,
            EntityRepository repository,
            HyperstateController hyperstateController, String label) {
        super(context, repository,
                AnnotationUtils.findAnnotation(hyperstateController.getClass(),
                        RequestMapping.class).value()[0],
                label);
    }
}