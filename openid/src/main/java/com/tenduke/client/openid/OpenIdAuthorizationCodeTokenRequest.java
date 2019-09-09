/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.authorizationcode.AbstractTokenRequest;
import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeResponse;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import java.net.URI;
import java.net.http.HttpClient;
import javax.annotation.Nullable;

/**
 * OpenID-specific token request.
 *
 */
public class OpenIdAuthorizationCodeTokenRequest extends AbstractTokenRequest<OpenIdAuthorizationCodeResponse> {

    /** ID-token parser. */
    private final IdTokenParser parseIdToken;

    /** Does ID-token have to be present in the response? In access-token requests, yes, with refresh requests no. */
    private final boolean requireIdToken;

    /**
     * Constructs new instance.
     *
     * @param httpClient -
     * @param json -
     * @param request -
     * @param state -
     * @param tokenEndpoint -
     * @param idTokenParser -
     * @param requireIdToken -
     */
    public OpenIdAuthorizationCodeTokenRequest(
            final HttpClient httpClient,
            final JsonDeserializer json,
            final String request,
            final String state,
            final URI tokenEndpoint,
            final IdTokenParser idTokenParser,
            final boolean requireIdToken
    ) {
        super(httpClient, json, request, state, tokenEndpoint);

        this.parseIdToken = idTokenParser;
        this.requireIdToken = requireIdToken;
    }

    /**
     * De-serializes the token response.
     *
     * <p>
     * This de-serializes the response as AuthorizationCodeResponse and then transforms that
     * to OpenIdAuthorizationCodeResponse. This is to avoid custom tweaking the JSON-serializers.
     *
     * @param body -
     * @return -
     * @throws JsonDeserializationException -
     * @throws OAuthException -
     *
     */
    @Override
    protected OpenIdAuthorizationCodeResponse deserializeTokenResponse(final String body)
            throws JsonDeserializationException, OAuthException {

        // De-serialize the response as AuthorizationCodeResponse
        final AuthorizationCodeResponse response = super.deserialize(body, AuthorizationCodeResponse.class);

        // Extract the id-token:
        @Nullable final Object idTokenProperty = response.gimmeAdditionalProperty("id_token");

        if (idTokenProperty == null) {
            if (requireIdToken) {
                throw new OAuthServerException("Token response does not contain ID-token");
            }
            return new OpenIdAuthorizationCodeResponse(response, null);
        }

        // Parse the id-token:
        final IdToken idToken = parseIdToken.from(idTokenProperty.toString());

        return new OpenIdAuthorizationCodeResponse(response, idToken);
    }

}
