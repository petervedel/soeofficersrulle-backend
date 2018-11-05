package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class RelationsResponse {

    @ApiModelProperty(value = "The parents of the relation", example = "list", required = true)
    @Getter
    @JsonProperty
    @Setter
    private List<PersonRelation> parents;

    @ApiModelProperty(value = "The children of the relation", example = "list", required = true)
    @Getter
    @JsonProperty
    @Setter
    private List<PersonRelation> children;
}
