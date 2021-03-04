package com.epam.esm.model.dao;

import com.epam.esm.util.MessageUtil;

/**
 * Thrown to indicate that data object layer has errors
 */
public class DaoException extends Exception {
    private String messageKey = "message.exception.dao.default";

    public DaoException(String messageKey) {
        this.messageKey = messageKey;
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return MessageUtil.getMessageForLocale(messageKey);
    }
}
