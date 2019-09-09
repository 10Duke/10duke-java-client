/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

/**
 * Base for OAuth exceptions.
 *
 */
public class OAuthException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * No-arg constructor.
     *
     */
    public OAuthException() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     */
    public OAuthException(final String message) {
        super(message);
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public OAuthException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs new instance.
     *
     * @param cause -
     */
    public OAuthException(final Throwable cause) {
        super(cause);
    }

}
