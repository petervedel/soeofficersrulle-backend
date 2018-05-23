package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@NamedNativeQuery(
        name = "com.nordkern.soeofficer.api.Rank.findAll",
        query = "SELECT * FROM rank",
        resultClass = Rank.class
)
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Table(name = "rank")
public class Rank {

    @ApiModelProperty(value = "The unique ID of the rank", example = "1", required = true)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The nato code of the rank", example = "1234", required = true)
    @Column(name = "nato_code")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    private Long natoCode;

    @ApiModelProperty(value = "The name of the rank", example = "Officer", required = true)
    @Column(name = "rank_name")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    private String rankName;

    @ApiModelProperty(value = "The date from which the rank is valid", example = "01/01/2018", required = true)
    @Column(name = "rank_valid_from")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date rankValidFrom;

    @ApiModelProperty(value = "The date from which the rank is invalid", example = "01/01/2018", required = true)
    @Column(name = "rank_valid_until")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date rankValidUntil;
}
