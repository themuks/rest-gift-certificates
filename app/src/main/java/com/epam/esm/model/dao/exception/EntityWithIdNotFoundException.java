package com.epam.esm.model.dao.exception;

import com.epam.esm.util.MessageUtils;

/**
 * Thrown to indicate that entity with id wasn't found
 */
public class EntityWithIdNotFoundException extends DaoException {
    private final long id;
    private String messageKey = "message.exception.dao.not_found";

    public EntityWithIdNotFoundException(long id, String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
        this.id = id;
    }

    public EntityWithIdNotFoundException(long id, String message, Throwable cause) {
        super(message, cause);
        this.id = id;
    }

    public EntityWithIdNotFoundException(long id, Throwable cause) {
        super(cause);
        this.id = id;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format(MessageUtils.getMessageForLocale(messageKey), id);
    }
}
