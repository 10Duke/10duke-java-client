/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.authorizationcode.AbstractTokenRequestFactory;
import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeConfig;
import java.net.URI;
import java.net.http.HttpClient;

/**
 * Creates token requests.
 *
 */
public class OpenIdAuthorizationCodeTokenRequestFactory extends AbstractTokenRequestFactory<
        OpenIdAuthorizationCodeResponse,
        OpenIdAuthorizationCodeTokenRequest
        > {

    /** HTTP-client. */
    private final HttpClient httpClient;

    /** Parses ID-token. */
    private final IdTokenParser idTokenParser;

    /** Deserializes JSON. */
    private final JsonDeserializer jsonDeserializer;

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param httpClient -
     * @param jsonDeserializer -
     * @param idTokenParser -
     */
    public OpenIdAuthorizationCodeTokenRequestFactory(
            final AuthorizationCodeConfig config,
            final HttpClient httpClient,
            final JsonDeserializer jsonDeserializer,
            final IdTokenParser idTokenParser
    ) {
        super(config);
        this.httpClient = httpClient;
        this.idTokenParser = idTokenParser;
        this.jsonDeserializer = jsonDeserializer;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected OpenIdAuthorizationCodeTokenRequest createRequest(
            final String requestBody,
            final String state,
            final URI tokenEndpoint
    ) {
        return new OpenIdAuthorizationCodeTokenRequest(
                httpClient,
                jsonDeserializer,
                requestBody,
                state,
                tokenEndpoint,
                idTokenParser,
                true
        );
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected OpenIdAuthorizationCodeTokenRequest createRefreshTokenRequest(
            final String requestBody,
            final String state,
            final URI tokenEndpoint
    ) {
        return new OpenIdAuthorizationCodeTokenRequest(
                httpClient,
                jsonDeserializer,
                requestBody,
                state,
                getTokenEndpoint(),
                idTokenParser,
                false
        );
    }

}
