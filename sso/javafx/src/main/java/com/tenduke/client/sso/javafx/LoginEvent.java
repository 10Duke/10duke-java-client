/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Base class for login events.
 *
 */
public class LoginEvent extends Event {

    private static final long serialVersionUID = 1L;

    /** Root event type for {@link LoginEvent}s. */
    public static final EventType<LoginEvent> LOGIN_ALL = new EventType<>("XD_LOGIN_ALL");

    /**
     * Constructs new instance.
     *
     * @param eventType -
     */
    public LoginEvent(final EventType<? extends Event> eventType) {
        super(eventType);
    }

    /**
     * Constructs new instance.
     *
     * @param source -
     * @param target -
     * @param eventType -
     */
    public LoginEvent(final Object source, final EventTarget target, final EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }

}
