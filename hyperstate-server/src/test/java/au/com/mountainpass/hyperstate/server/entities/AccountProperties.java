package au.com.mountainpass.hyperstate.server.entities;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccountProperties implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5101362070340337389L;

    private String creationDate;

    @JsonIgnore
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String username;

    public AccountProperties() {
    }

    public AccountProperties(final String username, final String creationDate) {
        this.username = username;
        this.creationDate = creationDate;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
