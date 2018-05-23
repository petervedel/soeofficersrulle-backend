package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UserSearch {

    @Getter
    @JsonProperty
    @Setter
    private String username;

    @Getter
    @JsonProperty
    @Setter
    private String email;

}
