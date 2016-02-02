package au.com.mountainpass.hyperstate.core.entities;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Relationship;

public class UpdatedEntity extends LinkedEntity {

    public UpdatedEntity(EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getLabel(),
                entity.getNatures());
    }

    public UpdatedEntity(Link link) {
        super(link);
    }

}
