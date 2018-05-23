package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@SqlResultSetMapping(
        name = "OfficerCorpsMapping",
        entities = @EntityResult(
                entityClass = OfficerCorps.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "appointedNumber", column = "serial_number1"),
                        @FieldResult(name = "dodabNumber", column = "serial_number2"),
                        @FieldResult(name = "appointedDate", column = "appointed_date"),
                        @FieldResult(name = "appointedUntil", column = "until"),
                        @FieldResult(name = "terminationCause", column = "termination_cause"),
                        @FieldResult(name = "personId", column = "person_id"),
                        @FieldResult(name = "rankName", column = "rank_name")}))
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Officer.findAll",
                query = "SELECT * FROM officer",
                resultClass = Officer.class
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Officer.officersWithLastRank",
                query = "SELECT * FROM officer WHERE id IN (SELECT is_promoted.officer_id FROM is_promoted GROUP BY is_promoted.officer_id HAVING MAX(is_promoted.rank_id) = :rank_id)",
                resultClass = Officer.class
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Officer.officersActiveAtDate",
                query = "SELECT officer.*, rank.rank_name, person.given_name, person.surname, person.date_of_birth, person.gender "+
                        "FROM officer "+
                        "JOIN is_promoted ON officer.id = is_promoted.officer_id " +
                        "JOIN rank ON is_promoted.rank_id = rank.id " +
                        "JOIN person ON officer.person_id = person.id "+
                        "WHERE :date BETWEEN officer.appointed_date AND officer.appointed_until " +
                        "AND is_promoted.id = ( " +
                        "  SELECT is_promoted.id " +
                        "  FROM is_promoted " +
                        "  WHERE is_promoted.date_of_promotion <= :date " +
                        "    AND officer.id = is_promoted.officer_id " +
                        "ORDER BY is_promoted.date_of_promotion DESC " +
                        "LIMIT 1)"
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Officer.firstRank",
                query = "SELECT * FROM rank WHERE rank.id ORDER BY rank.id ASC LIMIT 1",
                resultClass = Rank.class
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Officer.setAppointedDate",
                query = "UPDATE officer SET appointed_date = :appointed_date WHERE id = :id"
        )
})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Table(name = "officer")
public class Officer {

    public enum TerminationCause { Afsked,Dræbt_i_tjeneste,Dødsulykke,Invaliditet }

    @ApiModelProperty(value = "The unique ID of the officer", example = "1", required = true)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The appointed number of the officer", example = "12", required = true)
    @Column(name = "serial_number1")
    @Getter
    @JsonProperty
    @Setter
    private Long appointedNumber;

    @ApiModelProperty(value = "The officer's Dodab number", example = "123", required = true)
    @Column(name = "serial_number2")
    @Getter
    @JsonProperty
    @Setter
    private Long dodabNumber;

    @ApiModelProperty(value = "The date from which the officer title is valid", example = "01/01/2018")
    @Column(name = "appointed_date", updatable=false)
    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date appointedDate;

    @ApiModelProperty(value = "The date from which the officer title is invalid", example = "01/01/2018")
    @Column(name = "appointed_until")
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
    private TerminationCause terminationCause;

    @Getter
    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "officer")
    private List<Promotion> promotions;

    @ApiModelProperty(value = "The person associated with the officer", required = true)
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
