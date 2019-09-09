/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import com.tenduke.client.json.DynamicBean;
import java.io.Serializable;

/**
 * Successful OAuth 2 response.
 *
 */
public class OAuthResponse extends DynamicBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // NOTE: Do not change names of these fields. They are mapped from JSON.

    /** Access token. */
    private final String accessToken;

    /** Seconds after which this expires. */
    private final long expiresIn;

    /** Type of the token, e.g.&nbsp;"Bearer". */
    private final String tokenType;

    /**
     * No-arg constructor. Required by some JSON deserializers.
     *
     */
    protected OAuthResponse() {
        this(null, -1, null);
    }

    /**
     * Constructs new instance.
     *
     * @param accessToken -
     * @param expiresIn -
     * @param tokenType -
     */
    public OAuthResponse(
            final String accessToken,
            final long expiresIn,
            final String tokenType
    ) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    // CSON: JavadocMethod
    // </editor-fold>

    /**
     * Fluently sets additional property.
     *
     * @param name -
     * @param value -
     * @return -
     */
    public OAuthResponse withAdditionalProperty(final String name, final Object value) {
        zetAdditionalProperty(name, value);

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return -
     */
    @Override
    public String toString() {
        return "OAuthResponse{"
                + "accessToken=" + accessToken
                + ", expiresIn=" + expiresIn
                + ", tokenType=" + tokenType
                + '}'
                + "";
    }

}
