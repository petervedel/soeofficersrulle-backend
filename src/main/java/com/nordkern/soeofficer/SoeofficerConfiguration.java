package com.nordkern.soeofficer;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class SoeofficerConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("apiUsername")
    private String apiUsername;

    @Valid
    @NotNull
    @JsonProperty("apiPassword")
    private String apiPassword;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public String getApiPassword() {
        return this.apiPassword;
    }

    public String getApiUsername() {
        return this.apiUsername;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

}
