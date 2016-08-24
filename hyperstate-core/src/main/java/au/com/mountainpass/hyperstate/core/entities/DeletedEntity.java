package au.com.mountainpass.hyperstate.core.entities;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.Relationship;

public class DeletedEntity extends LinkedEntity {

    public DeletedEntity() {
        super(null);
    }

    public DeletedEntity(final EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getTitle(),
                entity.getClasses());
    }

    public DeletedEntity(final Link link) {
        super(link);
    }

}
