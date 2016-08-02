package au.com.mountainpass.hyperstate.core;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonPropertyOrder({ "class", "properties", "entities", "actions", "links",
//        "title" })
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameter {

  private String identifier;

  @Nullable
  private String title;

  private final String[] natures = {};

  private String type;

  @Nullable
  private String value;

  protected Parameter() {
  }

  public Parameter(@JsonProperty("name") final String identifier) {
    this.identifier = identifier;
    this.type = "text";
  }

  public Parameter(final String identifier, final String type, final String value) {
    this(identifier);
    this.type = type == null ? "text" : type;
    this.value = value;
  }

  public Parameter(final String identifier, final String type, final String value,
      final String title) {
    this(identifier, type, value);
    this.title = title;
  }

  /**
   * @return the identifier
   */
  @JsonProperty("name")
  public String getIdentifier() {
    return identifier;
  }

  /**
   * @return the title
   */
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  /**
   * @return the natures
   */
  @JsonProperty("class")
  public String[] getNatures() {
    return natures;
  }

  /**
   * @return the type
   */
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

}
