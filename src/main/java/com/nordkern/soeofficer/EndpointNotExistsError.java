package com.nordkern.soeofficer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Objects;

/**
 * Created by mortenfrank on 18/12/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndpointNotExistsError {
    private final int code;
    private final String message;
    private final String documentationLink;
    private final List<Endpoint> availableEndpoints;

    public EndpointNotExistsError(String message) {
        this(500, message);
    }

    public EndpointNotExistsError(int code, String message) {
        this(code, message, (String)null, (List)null);
    }

    @JsonCreator
    public EndpointNotExistsError(@JsonProperty("code") int code, @JsonProperty("message") String message, @JsonProperty("documentation_link") String documentationLink, @JsonProperty("available_endpoints") List<Endpoint> availableEndpoints) {
        this.code = code;
        this.message = message;
        this.documentationLink = documentationLink;
        this.availableEndpoints = availableEndpoints;
    }

    @JsonProperty("code")
    public Integer getCode() {
        return Integer.valueOf(this.code);
    }

    @JsonProperty("message")
    public String getMessage() {
        return this.message;
    }

    @JsonProperty("documentation_link")
    public String getDocumentationLink() {
        return this.documentationLink;
    }

    @JsonProperty("available_endpoints")
    public List<Endpoint> getAvailableEndpoints() {
        return this.availableEndpoints;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj != null && this.getClass() == obj.getClass()) {
            EndpointNotExistsError other = (EndpointNotExistsError)obj;
            return Objects.equals(Integer.valueOf(this.code), Integer.valueOf(other.code)) && Objects.equals(this.message, other.message) && Objects.equals(this.documentationLink, other.documentationLink);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.code), this.message, this.documentationLink, this.availableEndpoints});
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("code", this.code).add("message", this.message).add("documentation_link",this.documentationLink).add("available_endpoints",this.availableEndpoints).toString();
    }

}
