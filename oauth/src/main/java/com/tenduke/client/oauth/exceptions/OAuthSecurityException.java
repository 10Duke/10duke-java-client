/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

/**
 * OAuth security related exception, e.g.&nbsp;state does not match.
 *
 */
public class OAuthSecurityException extends OAuthException {

    private static final long serialVersionUID = 1L;

    /**
     * No-arg constructor.
     *
     */
    public OAuthSecurityException() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     */
    public OAuthSecurityException(final String message) {
        super(message);
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public OAuthSecurityException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
