package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PersonSearch {

    public enum Gender { Mand,Kvinde,Ukendt }

    @Getter
    @JsonProperty
    @Setter
    private String givenName;

    @Getter
    @JsonProperty
    @Setter
    private String surname;

    @Getter
    @Setter
    @JsonProperty
    @Max(2018)
    @Min(1000)
    private Integer yearOfBirthFrom;

    @Getter
    @Setter
    @JsonProperty
    @Max(2018)
    @Min(1000)
    private Integer yearOfBirthTo;

    @Getter
    @JsonProperty
    @Setter
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
