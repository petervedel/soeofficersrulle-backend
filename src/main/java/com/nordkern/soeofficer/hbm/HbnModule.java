package com.nordkern.soeofficer.hbm;

import com.google.inject.AbstractModule;
import org.hibernate.SessionFactory;


/**
 * Created by mortenfrank on 14/12/2017.
 */
public class HbnModule extends AbstractModule {

    private final HbnBundle hbnBundle;

    public HbnModule(HbnBundle hbnBundle) {
        this.hbnBundle = hbnBundle;
    }

    @Override
    protected void configure() {
        bind(SessionFactory.class).toInstance(hbnBundle.getSessionFactory());
    }
}
