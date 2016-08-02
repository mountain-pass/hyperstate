package au.com.mountainpass.hyperstate.core.entities;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Relationship;

public class CreatedEntity extends LinkedEntity {

    public CreatedEntity(final EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getTitle(),
                entity.getNatures());
    }

    public CreatedEntity(final Link link) {
        super(link);
    }

}
