/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

import com.tenduke.client.oauth.OAuthErrorResponse;

/**
 * Exception thrown when OAuth server returns error-response.
 *
 */
public class OAuthErrorException extends OAuthException {

    private static final long serialVersionUID = 1L;

    /** The error response. */
    private final OAuthErrorResponse error;

    /** Constructs new instance.
     *
     * @param message -
     * @param error -
     */
    public OAuthErrorException(
            final String message,
            final OAuthErrorResponse error
    ) {
        super(message);

        this.error = error;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters ">
    // CSOFF: JavadocMethod

    public OAuthErrorResponse getError() {
        return error;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
