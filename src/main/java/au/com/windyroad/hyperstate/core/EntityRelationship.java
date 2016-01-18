package au.com.windyroad.hyperstate.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import au.com.windyroad.hyperstate.core.entities.Entity;
import au.com.windyroad.hyperstate.core.entities.LinkedEntity;

public class EntityRelationship extends Relationship {

    private Entity entity;

    protected EntityRelationship() {
    }

    public EntityRelationship(Entity entity, String... natures) {
        super(natures);
        this.entity = entity;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(context.getAutowireCapableBeanFactory());
        bpp.processInjection(this.entity);
    }

    public EntityRelationship(Link link, String label, String[] natures) {
        super(natures);
        this.entity = new LinkedEntity(link, label, null);
    }

    /**
     * @return the entity
     */
    @JsonIgnore
    public Entity getEntity() {
        return entity;
    }

    @JsonUnwrapped
    public LinkedEntity getEntityLink() {
        return entity.toLinkedEntity();
    }
}
