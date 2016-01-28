package au.com.windyroad.hyperstate.core.entities;

import java.util.Properties;

import org.springframework.context.ApplicationContext;

import au.com.windyroad.hyperstate.core.EntityRepository;

public class VanillaEntity extends EntityWrapper<Properties> {

    public VanillaEntity(VanillaEntity src) {
        super(src);
    }

    protected VanillaEntity() {
        super(new Properties());
    }

    protected VanillaEntity(ApplicationContext context,
            EntityRepository repository, String path, String label) {
        super(context, repository, path, new Properties(), label);
    }

}
