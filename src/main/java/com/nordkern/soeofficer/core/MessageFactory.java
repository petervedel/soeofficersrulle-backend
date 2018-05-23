package com.nordkern.soeofficer.core;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Created by mortenfrank on 14/12/2017.
 */
public class MessageFactory {

    public static String getErrorMessage(String key) {
        return getErrorMessage(key, new String[]{});
    }

    public static String getErrorMessage(String key, String arg) {
        return getErrorMessage(key, new String[]{arg});
    }

    public static String getErrorMessage(String key, String[] args) {
        String message = ResourceFactory.getPropertiesResource("properties/ErrorMessages").getString(key);

        if (!Objects.isNull(args)) {
            MessageFormat mf = new MessageFormat("");
            message = mf.format(message,args);
        }

        return message;
    }
}
