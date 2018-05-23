package com.nordkern.soeofficer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by mortenfrank on 19/12/2017.
 */
@Produces(MediaType.APPLICATION_JSON)
public class Endpoint {
    @Getter
    @JsonProperty
    @Setter
    private String type;

    @Getter
    @JsonProperty
    @Setter
    private String endpoint;
}
