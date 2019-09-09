/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

/**
 * Exception thrown if problems with IO.
 *
 */
public class OAuthNetworkException extends OAuthException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public OAuthNetworkException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
