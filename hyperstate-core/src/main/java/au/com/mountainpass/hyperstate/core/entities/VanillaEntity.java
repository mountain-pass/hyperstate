package au.com.mountainpass.hyperstate.core.entities;

import java.util.Properties;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;

public class VanillaEntity extends EntityWrapper<Properties> {

    protected VanillaEntity() {
        super(new Properties());
    }

    public VanillaEntity(final RepositoryResolver resolver, final String path,
            final String title, final String... natures) {
        super(resolver, path, new Properties(), title, natures);
    }

    public VanillaEntity(final VanillaEntity src) {
        super(src);
    }

}
