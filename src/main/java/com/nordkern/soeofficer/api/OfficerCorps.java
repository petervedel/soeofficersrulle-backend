package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OfficerCorps {

    @ApiModelProperty(value = "The unique ID of the officer", example = "1", required = true)
    @Getter
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The appointed number of the officer", example = "12", required = true)
    @Getter
    @JsonProperty
    @Setter
    private Long appointedNumber;

    @ApiModelProperty(value = "The officer's Dodab number", example = "123", required = true)
    @Getter
    @JsonProperty
    @Setter
    private Long dodabNumber;

    @ApiModelProperty(value = "The date from which the officer title is valid", example = "01/01/2018", required = true)
    @Getter
    @JsonProperty
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date appointedDate;

    @ApiModelProperty(value = "The date from which the officer title is invalid", example = "01/01/2018", required = true)
    @Getter
    @JsonProperty
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date appointedUntil;

    @ApiModelProperty(value = "The cause of termination", example = "Dødsulykke", required = true)
    @Column(name = "termination_cause")
    @Getter
    @JsonProperty
    @Setter
    @Enumerated(EnumType.STRING)
    private Officer.TerminationCause terminationCause;

    @ApiModelProperty(value = "The person associated with the officer", required = true)
    @Getter
    @JsonProperty
    @Setter
    private Long personId;

    @ApiModelProperty(value = "The rank associated with the officer", required = true)
    @Getter
    @JsonProperty
    @Setter
    private String rankName;

    @ApiModelProperty(value = "The given name of the person", example = "John", required = true)
    @Getter
    @JsonProperty
    @Setter
    private String givenName;

    @ApiModelProperty(value = "The surname of the person", example = "Doe", required = true)
    @Getter
    @JsonProperty
    @Setter
    private String surname;

    @ApiModelProperty(value = "The person's date of birth. Format is: DD/MM/YYYY", example = "01/01/2018", required = true)
    @Getter
    @JsonProperty
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date dateOfBirth;

    @ApiModelProperty(value = "The gender of the person. Accepted values are from the set {Mand,Kvinde,Ukendt}", example = "Mand", required = true)
    @Getter
    @JsonProperty
    @Setter
    @Enumerated(EnumType.STRING)
    private Person.Gender gender;

}
