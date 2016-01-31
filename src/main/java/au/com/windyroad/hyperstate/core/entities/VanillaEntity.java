package au.com.windyroad.hyperstate.core.entities;

import java.util.Properties;

import org.springframework.context.ApplicationContext;

public class VanillaEntity extends EntityWrapper<Properties> {

    public VanillaEntity(VanillaEntity src) {
        super(src);
    }

    protected VanillaEntity() {
        super(new Properties());
    }

    public VanillaEntity(ApplicationContext context, String path, String label,
            String... natures) {
        super(context, path, new Properties(), label, natures);
    }

}
