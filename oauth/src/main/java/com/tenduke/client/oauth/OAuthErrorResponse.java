/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import javax.annotation.Nullable;

/**
 * Error-response from server.
 *
 * <p>
 * Instances are immutable.
 *
 */
public class OAuthErrorResponse {

    // NOTE: Do not modify the field names

    /** Error code as provided by the server. */
    private final String error;

    /** Description of the error, optional. */
    @Nullable private final String errorDescription;

    /** URI describing the error, optional. */
    @Nullable private final String errorUri;

    /**
     * No-arg constructor.
     *
     * <p>
     * This is mostly for JSON-serialization.
     */
    protected OAuthErrorResponse() {
        this(null, null, null);
    }

    /** Constructs new instance.
     *
     * @param error -
     * @param errorDescription -
     * @param errorUri -
     */
    public OAuthErrorResponse(
            final String error, final String errorDescription, final String errorUri
    ) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public String getError() {
        return error;
    }

    public @Nullable String getErrorDescription() {
        return errorDescription;
    }

    public @Nullable String getErrorUri() {
        return errorUri;
    }

    // CSON: JavadocMethod
    // </editor-fold>

}
