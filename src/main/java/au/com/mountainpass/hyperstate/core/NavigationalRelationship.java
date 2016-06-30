package au.com.mountainpass.hyperstate.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;

public class NavigationalRelationship extends Relationship {

  private Link link;

  private NavigationalRelationship() {
  }

  public NavigationalRelationship(final EntityWrapper<?> accounts, final String... natures) {
    super(natures);
    this.link = accounts.toLinkedEntity().getLink();
  }

  public NavigationalRelationship(final Link link, final String... natures) {
    super(natures);
    this.link = link;
  }

  @JsonUnwrapped
  public Link getLink() {
    return this.link;
  }

  @Autowired
  public void setApplicationContext(final ApplicationContext context) {
    final AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
    bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
    bpp.processInjection(this.link);
  }

  public void setLink(final Link link) {
    this.link = link;
  }

}
