package au.com.mountainpass.hyperstate.exceptions;

import au.com.mountainpass.hyperstate.core.Address;

public class EntityNotFoundException extends RuntimeException {

    private Address address;

    public EntityNotFoundException(Address address) {
        this.address = address;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -8849261700837523079L;

}
