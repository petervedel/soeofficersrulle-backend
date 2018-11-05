package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Created by mortenfrank on 24/11/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PersonRelation {

    public enum Gender { Male,Female,Unknown }
    public enum Type { Mother,Father }

    @ApiModelProperty(value = "The unique ID of the person", example = "1", required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The given name of the person", example = "John", required = true)
    @Getter
    @JsonProperty
    @Length(max=25)
    @Setter
    private String givenName;

    @ApiModelProperty(value = "The surname of the person", example = "Doe", required = true)
    @Getter
    @JsonProperty
    @Length(max=50)
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
    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ApiModelProperty(value = "The date of death. Format is: DD/MM/YYYY", example = "01/01/2018")
    @Column(name = "date_of_death")
    @Getter
    @JsonProperty
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date dateOfDeath;

    @ApiModelProperty(value = "The type of parent", example = "{mother,father}")
    @Getter
    @JsonProperty
    @Setter
    @Enumerated(EnumType.STRING)
    private Type type;

    @ApiModelProperty(value = "The unique ID of the officer", example = "1")
    @Getter
    @JsonProperty
    @Setter
    private Long officerId;

    @ApiModelProperty(value = "The id of the relation", example = "true / false")
    @Column(name = "relation_id")
    @Getter
    @Setter
    @JsonProperty
    private Long relationID;

}
