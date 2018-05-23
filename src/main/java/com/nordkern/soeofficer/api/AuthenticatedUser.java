package com.nordkern.soeofficer.api;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;

/**
 * Created by mortenfrank on 15/03/2018.
 */
public class AuthenticatedUser implements Principal {
    @Setter
    @Getter
    private String name = null;

}
