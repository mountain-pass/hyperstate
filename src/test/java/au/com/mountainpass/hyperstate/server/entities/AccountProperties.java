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

  @JsonIgnore
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  public AccountProperties() {
  }

  public AccountProperties(String username, String creationDate) {
    this.username = username;
    this.creationDate = creationDate;
  }

  private String username;

  private String creationDate;

  public String getUsername() {
    return this.username;
  }

  public String getCreationDate() {
    return this.creationDate;
  }
}
