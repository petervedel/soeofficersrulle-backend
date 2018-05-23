package com.nordkern.soeofficer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Created by mortenfrank on 24/11/2017.
 */
@SqlResultSetMappings( {
        @SqlResultSetMapping(name="updateResult", columns = { @ColumnResult(name = "count")})
})
@NamedNativeQueries({
        @NamedNativeQuery(
            name = "com.nordkern.soeofficer.api.User.findAll",
            query = "SELECT * FROM user",
            resultClass = User.class
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.User.setPassword",
                query = "UPDATE user SET password = :password WHERE id = :id",
                resultSetMapping = "updateResult"
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.User.getPassword",
                query = "SELECT password FROM user WHERE username = :username OR email :email"
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.User.gerUserByName",
                query = "SELECT * FROM user WHERE username = :username",
                resultClass = User.class
        ),
        @NamedNativeQuery(
                name = "com.nordkern.soeofficer.api.User.gerUserByEmail",
                query = "SELECT * FROM user WHERE email = :email",
                resultClass = User.class
        )
})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Table(name = "user")
public class User {

    public enum Role { contributor,administrator,read,privileged_read }

    @ApiModelProperty(value = "The unique ID of the user", example = "1", required = true)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    @JsonProperty
    @Setter
    private Long id;

    @ApiModelProperty(value = "The unique username of the user", example = "john_doe", required = true)
    @Column(name = "username", unique = true)
    @Getter
    @JsonProperty
    @Length(max=25)
    @NotBlank
    @Setter
    private String username;

    @ApiModelProperty(value = "The password for the user", example = "mySecretPassword", required = true)
    @Column(name = "password")
    @Getter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(max=250)
    @Setter
    private String password;

    @ApiModelProperty(value = "The unique email of the user", example = "john@doe.com", required = true)
    @Column(name = "email", unique = true)
    @Email(message = "the provided email is not valid")
    @Getter
    @JsonProperty
    @Length(max=50)
    @NotBlank
    @Setter
    private String email;

    @ApiModelProperty(value = "The date from which the user is valid. Format is: DD/MM/YYYY", example = "01/01/2018", required = true)
    @Column(name = "from_date")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @Temporal(value= TemporalType.DATE)
    private Date from;

    @ApiModelProperty(value = "The date on which to user is invalid. Format is: DD/MM/YYYY", example = "31/12/9999", required = true)
    @Column(name = "until_date")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @Temporal(value= TemporalType.DATE)
    private Date until;

    @ApiModelProperty(value = "The system-role of the user", example = "administrator", required = true)
    @Column(name = "role")
    @Getter
    @JsonProperty
    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;
}
