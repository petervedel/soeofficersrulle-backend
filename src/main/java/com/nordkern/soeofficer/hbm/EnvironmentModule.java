package com.nordkern.soeofficer.hbm;

import com.nordkern.soeofficer.SoeofficerConfiguration;
import com.google.inject.AbstractModule;
import io.dropwizard.setup.Environment;

/**
 * Created by mortenfrank on 19/12/2017.
 */
public class EnvironmentModule extends AbstractModule {

    private Environment env;
    private SoeofficerConfiguration configuration;

    public  EnvironmentModule(SoeofficerConfiguration configuration, Environment env)
    {
        this.configuration = configuration;
        this.env= env;
    }

    protected void configure()
    {
        bind(Environment.class).toInstance(env);
    }
}