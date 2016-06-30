package au.com.mountainpass.hyperstate.client;

import java.net.URI;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import au.com.mountainpass.hyperstate.core.Link;
import au.com.mountainpass.hyperstate.core.MediaTypes;

public class RestLink extends Link {

  URI address;

  @Nullable
  MediaType representationFormat = MediaTypes.SIREN_JSON;

  private RestTemplate restTemplate;

  public RestLink() {
  }

  public RestLink(final URI address) {
    this.address = address;
  }

  public RestLink(final URI address, final String label) {
    super(label);
    this.address = address;
  }

  public RestLink(final URI address, final String label, final Set<String> natures) {
    super(label, natures);
    this.address = address;
  }

  @Override
  @JsonProperty("href")
  public URI getAddress() {
    return address;
  }

  @Override
  @JsonIgnore
  public String getPath() {
    return address.toString();
  }

  @Override
  public MediaType getRepresentationFormat() {
    return representationFormat == null ? MediaTypes.SIREN_JSON : representationFormat;
  }

  @Override
  public <T> T resolve(final Class<T> type) {
    final RequestEntity<Void> request = RequestEntity.get(address).accept(getRepresentationFormat())
        .build();
    final ResponseEntity<T> response = restTemplate.exchange(address, HttpMethod.GET, request,
        type);
    return response.getBody();
  }

  @Override
  public <T> T resolve(final ParameterizedTypeReference<T> type) {
    final RequestEntity<Void> request = RequestEntity.get(address).accept(getRepresentationFormat())
        .build();
    final ResponseEntity<T> response = restTemplate.exchange(address, HttpMethod.GET, request,
        type);
    return response.getBody();
  }

  public void setAddress(final URI address) {
    this.address = address;
  }

  @Autowired
  public void setRestTemplate(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

}
