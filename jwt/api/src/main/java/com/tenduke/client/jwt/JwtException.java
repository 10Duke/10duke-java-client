/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

/**
 * JWT parsing exception.
 *
 */
public class JwtException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * No-arg constructor.
     *
     */
    public JwtException() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     */
    public JwtException(final String message) {
        super(message);
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public JwtException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs new instance.
     *
     * @param cause -
     */
    public JwtException(final Throwable cause) {
        super(cause);
    }

}
