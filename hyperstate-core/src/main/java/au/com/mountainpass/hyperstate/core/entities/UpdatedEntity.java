package au.com.mountainpass.hyperstate.core.entities;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Relationship;

public class UpdatedEntity extends LinkedEntity {

    public UpdatedEntity(final EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getTitle(),
                entity.getNatures());
    }

    public UpdatedEntity(final Link link) {
        super(link);
    }

}
