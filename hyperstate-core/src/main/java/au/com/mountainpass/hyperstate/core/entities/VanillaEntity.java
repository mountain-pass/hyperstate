package au.com.mountainpass.hyperstate.core.entities;

import java.util.Properties;

import au.com.mountainpass.hyperstate.core.EntityRepository;

public class VanillaEntity extends EntityWrapper<Properties> {

    protected VanillaEntity() {
        super(new Properties());
    }

    public VanillaEntity(final EntityRepository repository, final String path,
            final String title, final String... classes) {
        super(repository, path, new Properties(), title, classes);
    }

}
