package au.com.windyroad.hyperstate.core.entities;

import au.com.windyroad.hyperstate.core.Link;
import au.com.windyroad.hyperstate.core.Relationship;

public class UpdatedEntity extends LinkedEntity {

    public UpdatedEntity(EntityWrapper<?> entity) {
        super(entity.getLink(Relationship.SELF), entity.getLabel(),
                entity.getNatures());
    }

    public UpdatedEntity(Link link) {
        super(link);
    }

}
