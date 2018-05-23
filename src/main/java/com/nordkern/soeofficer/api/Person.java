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

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Person.findAll",
                query = "SELECT * FROM person",
                resultClass = Person.class
        )
})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Table(name = "person")
public class Person {

    public enum CauseOfDeath { Dræbt,Ulykke,Sygdom,Andet }
    public enum Gender { Mand,Kvinde,Ukendt }

    @ApiModelProperty(value = "The unique ID of the person", example = "1", required = true)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The given name of the person", example = "John", required = true)
    @Column(name = "given_name")
    @Getter
    @JsonProperty
    @Length(max=25)
    @Setter
    private String givenName;

    @ApiModelProperty(value = "The surname of the person", example = "Doe", required = true)
    @Column(name = "surname")
    @Getter
    @JsonProperty
    @Length(max=50)
    @Setter
    private String surname;

    @ApiModelProperty(value = "The person's date of birth. Format is: DD/MM/YYYY", example = "01/01/2018", required = true)
    @Column(name = "date_of_birth")
    @Getter
    @JsonProperty
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date dateOfBirth;

    @ApiModelProperty(value = "The gender of the person. Accepted values are from the set {Mand,Kvinde,Ukendt}", example = "Mand", required = true)
    @Column(name = "gender")
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
}
