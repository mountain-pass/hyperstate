package au.com.mountainpass.hyperstate.core.entities;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Relationship;

public class CreatedEntity extends LinkedEntity {

    public CreatedEntity(EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getLabel(),
                entity.getNatures());
    }

    public CreatedEntity(Link link) {
        super(link);
    }

}
