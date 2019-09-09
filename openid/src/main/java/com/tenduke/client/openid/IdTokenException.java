/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.exceptions.OAuthSecurityException;

/**
 * Thrown when Id-token processing failed.
 *
 */
public class IdTokenException extends OAuthSecurityException {
    private static final long serialVersionUID = 1L;

    /**
     * No-arg constructor.
     *
     */
    public IdTokenException() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     */
    public IdTokenException(final String message) {
        super(message);
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public IdTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
