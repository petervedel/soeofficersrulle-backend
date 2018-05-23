package com.nordkern.soeofficer;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.nordkern.soeofficer.api.AuthenticatedUser;
import com.nordkern.soeofficer.core.AppBasicAuthenticator;
import com.nordkern.soeofficer.core.JSon.CustomJSonProcessingExceptionMapper;
import com.nordkern.soeofficer.hbm.HbnBundle;
import com.nordkern.soeofficer.hbm.HbnModule;
import com.nordkern.soeofficer.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.jersey.validation.JerseyViolationExceptionMapper;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import java.text.SimpleDateFormat;

public class SoeofficerApplication extends Application<SoeofficerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SoeofficerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Soeofficer";
    }

    @Override
    public void initialize(final Bootstrap<SoeofficerConfiguration> bootstrap) {
        final HbnBundle hibernate = new HbnBundle();
        // enable hibernate
        bootstrap.addBundle(hibernate);

        // enable Guice
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig("com.nordkern.soeofficer")
                .modules(new HbnModule(hibernate))
                .build());

        // enable reading of environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor()
                )
        );

        // enable Liquibase
        bootstrap.addBundle(new MigrationsBundle<SoeofficerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(SoeofficerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final SoeofficerConfiguration configuration,
                    final Environment environment) {
        AbstractServerFactory sf = (AbstractServerFactory) configuration.getServerFactory();
        // disable all default exception mappers
        sf.setRegisterDefaultExceptionMappers(false);
        environment.jersey().register(new LoggingExceptionMapper<Throwable>() {});
        environment.jersey().register(new CustomJSonProcessingExceptionMapper());
        environment.jersey().register(new EarlyEofExceptionMapper());
        environment.jersey().register(new RuntimeExceptionMapper());
        environment.jersey().register(new JerseyViolationExceptionMapper());

        environment.getObjectMapper().setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleModule module = new SimpleModule();
        module.addSerializer(java.sql.Date.class, new DateSerializer());

        environment.getObjectMapper().registerModule(module);
        environment.jersey().register(new UserResource());
        environment.jersey().register(new PersonResource());
        environment.jersey().register(new OfficerResource());
        environment.jersey().register(new RankResource());
        environment.jersey().register(new RelationsResource());

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<AuthenticatedUser>()
                .setAuthenticator(new AppBasicAuthenticator(configuration))
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AuthenticatedUser.class));
    }
}