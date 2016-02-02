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

    public NavigationalRelationship(Link link, String... natures) {
        super(natures);
        this.link = link;
    }

    public NavigationalRelationship(EntityWrapper<?> accounts,
            String... natures) {
        super(natures);
        this.link = accounts.toLinkedEntity().getLink();
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
        bpp.processInjection(this.link);
    }

    @JsonUnwrapped
    public Link getLink() {
        return this.link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

}
