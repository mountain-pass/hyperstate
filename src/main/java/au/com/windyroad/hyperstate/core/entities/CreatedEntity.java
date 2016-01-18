package au.com.windyroad.hyperstate.core.entities;

import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.Relationship;

public class CreatedEntity extends LinkedEntity {

    public CreatedEntity(EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getLabel(),
                entity.getNatures());
    }

    public CreatedEntity(Link link) {
        super(link);
    }

}
