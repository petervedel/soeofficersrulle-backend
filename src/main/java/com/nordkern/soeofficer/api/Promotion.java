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


/**
 * Created by mortenfrank on 24/11/2017.
 */
@NamedNativeQueries({
        @NamedNativeQuery(
            name="com.nordkern.soeofficer.api.Officer.promotionsForOfficer",
            query = "SELECT * FROM is_promoted JOIN officer ON officer.id = is_promoted.officer_id WHERE officer.id = :me",
            resultClass = Promotion.class
        ),
        @NamedNativeQuery(
            name = "com.nordkern.soeofficer.api.Officer.nextRank",
            query = "(SELECT * FROM rank WHERE rank.id > (SELECT MAX(r.id) FROM rank r JOIN is_promoted ip ON r.id = ip.rank_id WHERE ip.officer_id = :officer_id) " +
                    "AND :date BETWEEN rank.rank_valid_from AND rank.rank_valid_until "+
                    "GROUP BY rank.id ASC LIMIT 1) "+
                    "UNION " +
                    "(SELECT * FROM rank WHERE NOT EXISTS( " +
                        "SELECT * " +
                        "FROM rank r  " +
                        "JOIN is_promoted ip " +
                        "ON r.id = ip.rank_id " +
                        "WHERE ip.officer_id = :officer_id) " +
                    "AND :date BETWEEN rank.rank_valid_from AND rank.rank_valid_until " +
                    "ORDER BY rank.id ASC LIMIT 1)",
            resultClass = Rank.class
        )
})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Table(name = "is_promoted")
public class Promotion {

    @ApiModelProperty(value = "The unique ID of the promotion", example = "1", required = true)
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The date from which the rank is valid", example = "1", required = true)
    @Column(name = "date_of_promotion")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(value= TemporalType.DATE)
    private Date dateOfPromotion;

    @ApiModelProperty(value = "The date from which the rank is invalid", example = "1", required = true)
    @Getter
    @JsonProperty
    @Setter
    @ManyToOne
    @JoinColumn(name = "rank_id", nullable = false)
    private Rank rank;

    @ApiModelProperty(value = "The id of the officer possessing the rank", example = "1", required = true)
    @Getter
    @JsonIgnore
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "officer_id", nullable = false)
    private Officer officer;
}
