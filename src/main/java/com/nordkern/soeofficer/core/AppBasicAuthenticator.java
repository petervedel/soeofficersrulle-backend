package com.nordkern.soeofficer.core;

/**
 * Created by mortenfrank on 15/03/2018.
 */

import com.nordkern.soeofficer.SoeofficerConfiguration;
import com.nordkern.soeofficer.api.AuthenticatedUser;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class AppBasicAuthenticator implements Authenticator<BasicCredentials, AuthenticatedUser> {

    private static String apiUsername;
    private static String apiPassword;

    public AppBasicAuthenticator(SoeofficerConfiguration soeofficerConfiguration){
        this.apiUsername = soeofficerConfiguration.getApiUsername();
        this.apiPassword = soeofficerConfiguration.getApiPassword();
    }

    @Override
    public Optional<AuthenticatedUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (apiUsername.equals(credentials.getUsername()) && apiPassword.equals(credentials.getPassword())) {
            return Optional.of(new AuthenticatedUser());
        }
        return Optional.empty();
    }
}