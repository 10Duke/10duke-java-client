/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

/**
 * Login exception, thrown by {@link LoginDialog} when login-related error occurs.
 *
 */
public class LoginException extends Exception {

    private static final long serialVersionUID = 1L;

    /** The event which caused the exception. */
    private final LoginErrorEvent event;

    /**
     * Constructs new instance.
     *
     * @param event -
     */
    public LoginException(final LoginErrorEvent event) {
        super();
        this.event = event;
    }

    /**
     * Returns the event which caused the exception.
     *
     * @return -
     */
    public LoginErrorEvent getEvent() {
        return event;
    }

}
