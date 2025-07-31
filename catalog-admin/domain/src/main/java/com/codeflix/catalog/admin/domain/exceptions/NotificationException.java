package com.codeflix.catalog.admin.domain.exceptions;

import com.codeflix.catalog.admin.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, Notification aNotification) {
        super(aMessage, aNotification.getErrors());
    }

}
