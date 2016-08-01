package au.com.mountainpass.hyperstate.core.entities;

import java.util.Properties;

import au.com.mountainpass.hyperstate.core.Resolver;

public class VanillaEntity extends EntityWrapper<Properties> {

    protected VanillaEntity() {
        super(new Properties());
    }

    public VanillaEntity(final Resolver resolver, final String path,
            final String label, final String... natures) {
        super(resolver, path, new Properties(), label, natures);
    }

    public VanillaEntity(final VanillaEntity src) {
        super(src);
    }

}
