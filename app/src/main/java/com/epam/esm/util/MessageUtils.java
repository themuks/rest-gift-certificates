package com.epam.esm.util;

import lombok.Setter;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * MessageUtils class is responsible for getting messages of locale, based on locale field, from messages bundle.
 */
@Setter
public class MessageUtils {
    private static final String BUNDLE_NAME = "messages";
    private static final String ENGLISH = "en";
    private static Locale locale = new Locale(ENGLISH);

    private MessageUtils() {
    }

    public static String getMessageForLocale(String messageKey) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(messageKey);
    }
}
