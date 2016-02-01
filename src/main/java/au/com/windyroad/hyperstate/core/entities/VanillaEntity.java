package au.com.windyroad.hyperstate.core.entities;

import java.util.Properties;

public class VanillaEntity extends EntityWrapper<Properties> {

    public VanillaEntity(VanillaEntity src) {
        super(src);
    }

    protected VanillaEntity() {
        super(new Properties());
    }

    public VanillaEntity(String path, String label, String... natures) {
        super(path, new Properties(), label, natures);
    }

}
