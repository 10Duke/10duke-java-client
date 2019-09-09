/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

/**
 * Exception thrown if the backend does something unexpected.
 *
 *
 */
public class OAuthServerException extends OAuthException {

    private static final long serialVersionUID = 1L;

    /**
     * No-arg constructor.
     *
     */
    public OAuthServerException() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     */
    public OAuthServerException(final String message) {
        super(message);
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public OAuthServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs new instance.
     *
     * @param cause -
     */
    public OAuthServerException(final Throwable cause) {
        super(cause);
    }

}
