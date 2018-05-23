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


/**
 * Created by mortenfrank on 24/11/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UserLogin {

    @ApiModelProperty(value = "The unique username of the user", example = "john_doe")
    @Getter
    @JsonProperty
    @Length(max=25)
    @Setter
    private String username;

    @ApiModelProperty(value = "The password of the user", example = "john_doe", required = true)
    @Getter
    @JsonProperty
    @Length(max=250)
    @Setter
    @NotBlank
    private String password;

    @ApiModelProperty(value = "The unique email of the user", example = "john@doe.com")
    @Email(message = "the provided email is not valid")
    @Getter
    @JsonProperty
    @Length(max=50)
    @Setter
    private String email;
}
