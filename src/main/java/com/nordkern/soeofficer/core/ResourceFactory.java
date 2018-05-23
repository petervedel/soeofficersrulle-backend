package com.nordkern.soeofficer.core;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by mortenfrank on 14/12/2017.
 */
public class ResourceFactory {

    public static ResourceBundle getPropertiesResource(String criteria) {
        return ResourceBundle.getBundle(criteria,Locale.getDefault());
    }
}
