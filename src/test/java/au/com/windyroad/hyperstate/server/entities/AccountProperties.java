package au.com.windyroad.hyperstate.server.entities;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountProperties implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5101362070340337389L;

    @JsonIgnore
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public AccountProperties() {
    }

    public AccountProperties(Map<String, String> properties) {
        this.username = properties.get("username");
        this.creationDate = properties.get("creationDate");
    }

    @JsonProperty
    private String username;

    @JsonProperty
    private String creationDate;

}
