/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.json.JsonDeserializer;
import java.net.URI;
import java.net.http.HttpClient;

/**
 * Factory for creating {@link AuthorizationCodeTokenRequest}s.
 *
 */
public class AuthorizationCodeTokenRequestFactory extends AbstractTokenRequestFactory<
        AuthorizationCodeConfig,
        AuthorizationCodeRequest,
        AuthorizationCodeResponse,
        AuthorizationCodeTokenRequest
> {

    /** HTTP-client. */
    private final HttpClient httpClient;

    /** JSON-deserializer. */
    private final JsonDeserializer jsonDeserializer;

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param httpClient -
     * @param jsonDeserializer -
     */
    public AuthorizationCodeTokenRequestFactory(
            final AuthorizationCodeConfig config,
            final HttpClient httpClient,
            final JsonDeserializer jsonDeserializer
    ) {
        super(config);
        this.httpClient = httpClient;
        this.jsonDeserializer = jsonDeserializer;
    }

    /**
     * {@inheritDoc}
     *
     * @param requestBody -
     * @param state -
     * @param tokenEndpoint -
     * @return -
     */
    @Override
    protected AuthorizationCodeTokenRequest createRequest(
            final String requestBody,
            final String state,
            final URI tokenEndpoint
    ) {
        return new AuthorizationCodeTokenRequest(
                httpClient,
                jsonDeserializer,
                requestBody,
                state,
                tokenEndpoint
        );
    }

}
