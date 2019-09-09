/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.oauth.exceptions.OAuthException;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javax.annotation.Nullable;

/**
 * Event emitted on login error.
 *
 */
public class LoginErrorEvent extends LoginEvent {

    private static final long serialVersionUID = 1L;

    /** Root event type for all login error events. */
    public static final EventType<LoginErrorEvent> LOGIN_ERROR_ALL = new EventType<>(LOGIN_ALL, "XD_LOGIN_ERROR_ALL");

    /** Event type for login error. */
    public static final EventType<LoginErrorEvent> ERROR = new EventType<>(LOGIN_ERROR_ALL, "XD_LOGIN_ERROR");

    /** Event type indicating the login was interrupted. */
    public static final EventType<LoginErrorEvent> INTERRUPTED = new EventType<>(LOGIN_ERROR_ALL, "XD_LOGIN_ERROR_INTERRUPTED");

    /** Event type indicating that a network error occurred in login flow. */
    public static final EventType<LoginErrorEvent> NETWORK_ERROR = new EventType<>(LOGIN_ERROR_ALL, "XD_LOGIN_ERROR_NETWORK");

    /** Event type indicating that a security error occurred in login flow. */
    public static final EventType<LoginErrorEvent> SECURITY_ERROR = new EventType<>(LOGIN_ERROR_ALL, "XD_LOGIN_ERROR_SECURITY");

    /** Event type indicating that an unexpected server error occurred in login flow. */
    public static final EventType<LoginErrorEvent> SERVER_ERROR = new EventType<>(LOGIN_ERROR_ALL, "XD_LOGIN_ERROR_SERVER");

    /** Cause, optional. */
    private @Nullable final OAuthException cause;

    /** Error message. */
    private final String message;

    /**
     * Constructs new instance.
     *
     * @param source -
     * @param target -
     * @param type -
     * @param message -
     * @param cause -
     */
    public LoginErrorEvent(
            final Object source,
            final EventTarget target,
            final EventType<? extends Event> type,
            final String message,
            @Nullable final OAuthException cause
    ) {
        super(source, target, type);

        this.message = message;

        this.cause = cause;
    }

    /**
     * Constructs new instance.
     *
     * @param source -
     * @param target -
     * @param type -
     * @param message -
     */
    public LoginErrorEvent(
            final Object source,
            final EventTarget target,
            final EventType<? extends Event> type,
            final String message
    ) {
        this(source, target, type, message, null);
    }

    /**
     * Constructs new instance.
     *
     * @param source -
     * @param target -
     * @param type -
     * @param cause -
     */
    public LoginErrorEvent(
            final Object source,
            final EventTarget target,
            final EventType<? extends Event> type,
            final OAuthException cause
    ) {
        this(source, target, type, cause.getMessage(), cause);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public @Nullable OAuthException getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
