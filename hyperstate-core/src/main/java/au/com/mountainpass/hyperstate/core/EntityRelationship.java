package au.com.mountainpass.hyperstate.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.LinkedEntity;

public class EntityRelationship extends Relationship {

  private Entity entity;

  protected EntityRelationship() {
  }

  public EntityRelationship(final Entity entity, final String... natures) {
    super(natures);
    this.entity = entity;
  }

  /**
   * @return the entity
   */
  @JsonIgnore
  public Entity getEntity() {
    return entity;
  }

  // public EntityRelationship(Link link, String label, String[] natures) {
  // super(natures);
  // this.entity = new LinkedEntity(link, label, null);
  // }

  @JsonUnwrapped
  public LinkedEntity getEntityLink() {
    return entity.toLinkedEntity();
  }

  @Autowired
  public void setApplicationContext(final ApplicationContext context) {
    final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
    bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
    bpp.processInjection(this.entity);
  }
}
