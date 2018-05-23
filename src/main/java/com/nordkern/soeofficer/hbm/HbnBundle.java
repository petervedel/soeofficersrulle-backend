package com.nordkern.soeofficer.hbm;

import com.nordkern.soeofficer.SoeofficerConfiguration;
import com.nordkern.soeofficer.api.*;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

/**
 * Created by mortenfrank on 14/12/2017.
 */
public class HbnBundle extends HibernateBundle<SoeofficerConfiguration> {

    public HbnBundle() {
        super(User.class,Person.class,Officer.class,Rank.class,Promotion.class,Relation.class);
    }

    @Override
    public PooledDataSourceFactory getDataSourceFactory(SoeofficerConfiguration configuration) {
        return configuration.getDataSourceFactory();
    }
}