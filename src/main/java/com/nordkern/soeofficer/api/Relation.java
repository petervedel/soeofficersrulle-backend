package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Relations.findParents",
                query = "SELECT person.*, officer.id AS officer_id, relation.id AS relation_id " +
                        "FROM person " +
                        "LEFT JOIN officer " +
                        "  ON officer.person_id = person.id " +
                        "JOIN relation ON person.id = relation.parent_id " +
                        "WHERE relation.child_id = :id"
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.Relations.findChildren",
                query = "SELECT person.*, officer.id AS officer_id, relation.id AS relation_id " +
                        "FROM person " +
                        "LEFT JOIN officer " +
                        "  ON officer.person_id = person.id " +
                        "JOIN relation ON person.id = relation.child_id " +
                        "WHERE relation.parent_id = :id"
        )
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Table(name = "relation")
public class Relation {

    @ApiModelProperty(value = "The unique ID of the relation", example = "1", required = true)
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The parent of the relation", example = "1", required = true)
    @Getter
    @JsonProperty
    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Person parent;

    @ApiModelProperty(value = "The parent of the relation", example = "1", required = true)
    @Getter
    @JsonProperty
    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Person child;
}
