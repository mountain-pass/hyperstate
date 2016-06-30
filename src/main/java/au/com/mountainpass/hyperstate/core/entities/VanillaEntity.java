package au.com.mountainpass.hyperstate.core.entities;

import java.util.Properties;

public class VanillaEntity extends EntityWrapper<Properties> {

  protected VanillaEntity() {
    super(new Properties());
  }

  public VanillaEntity(final String path, final String label, final String... natures) {
    super(path, new Properties(), label, natures);
  }

  public VanillaEntity(final VanillaEntity src) {
    super(src);
  }

}
