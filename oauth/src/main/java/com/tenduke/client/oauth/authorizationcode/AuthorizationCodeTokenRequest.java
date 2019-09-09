/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.exceptions.OAuthException;
import java.net.URI;
import java.net.http.HttpClient;

/**
 * Token request for Authorization Code flow.
 *
 */
public class AuthorizationCodeTokenRequest extends AbstractTokenRequest<AuthorizationCodeResponse> {

    /**
     * Constructs new instance.
     *
     * @param httpClient -
     * @param json -
     * @param request -
     * @param state -
     * @param tokenEndpoint -
     */
    public AuthorizationCodeTokenRequest(
            final HttpClient httpClient,
            final JsonDeserializer json,
            final String request,
            final String state,
            final URI tokenEndpoint
    ) {
        super(httpClient, json, request, state, tokenEndpoint);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected AuthorizationCodeResponse deserializeTokenResponse(final String body) throws JsonDeserializationException, OAuthException {
        return super.deserialize(body, AuthorizationCodeResponse.class);
    }

}
