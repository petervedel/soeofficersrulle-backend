package com.nordkern.soeofficer.core.JSon;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by mortenfrank on 15/12/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonParseFailureErrorMessage {
    private final int code;
    private final String message;
    private final String documentationLink;
    private final Object object;

    public JsonParseFailureErrorMessage(String message) {
        this(500, message);
    }

    public JsonParseFailureErrorMessage(int code, String message) {
        this(code, message, (String)null, (Object)null);
    }

    @JsonCreator
    public JsonParseFailureErrorMessage(@JsonProperty("code") int code, @JsonProperty("message") String message, @JsonProperty("documentation_link") String documentationLink, @JsonProperty("example_object") Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
        this.documentationLink = documentationLink;
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

    @JsonProperty("example_object")
    public Object getObject() {
        return this.object;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj != null && this.getClass() == obj.getClass()) {
            JsonParseFailureErrorMessage other = (JsonParseFailureErrorMessage)obj;
            return Objects.equals(Integer.valueOf(this.code), Integer.valueOf(other.code)) && Objects.equals(this.message, other.message) && Objects.equals(this.object, other.object) && Objects.equals(this.documentationLink, other.documentationLink);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.code), this.message, this.object, this.documentationLink});
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("code", this.code).add("message", this.message).add("object", this.object).add("documentation_link",this.documentationLink).toString();
    }

}
