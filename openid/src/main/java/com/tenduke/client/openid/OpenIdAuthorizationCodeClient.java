/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.json.DefaultJsonDeserializer;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.jwt.DefaultJwtParserFactory;
import com.tenduke.client.oauth.OAuthClient;
import static com.tenduke.client.oauth.authorizationcode.AuthorizationCodeClient.DEFAULT_HTTP_CLIENT;
import com.tenduke.client.oauth.exceptions.OAuthException;
import java.net.http.HttpClient;

/**
 * Client for OpenId Connect with Authorization Code flow.
 *
 * <p>
 * NOTE: Client automatically adds the {@code openid} scope to the request.
 *
 * <p>
 * NOTE: Client automatically adds {@code nonce} to the request, if none is provided. Caller can provide {@code nonce}
 * either by passing it as a parameter or using method {@link OpenIdAuthorizationCodeFlowBuilder#nonce(java.lang.String) }.
 *
 */
public class OpenIdAuthorizationCodeClient implements OAuthClient {

    /** Configuration. */
    private final OpenIdAuthorizationCodeConfig config;

    /** ID-token validator. */
    private final IdTokenValidator idTokenValidator;

    /** Creates token requests. */
    private final OpenIdAuthorizationCodeTokenRequestFactory tokenRequestFactory;

    /**
     * Constructs new instance.
     *
     * @param config -
     */
    public OpenIdAuthorizationCodeClient(final OpenIdAuthorizationCodeConfig config) {
        this(
                config,
                DEFAULT_HTTP_CLIENT,
                new IdTokenParser(DefaultJwtParserFactory.INSTANCE.create(config.getSignatureVerificationKey())),
                new IdTokenValidator(config.getClientId(), config.getIssuer()),
                DefaultJsonDeserializer.INSTANCE.get()
        );
    }

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param httpClient -
     * @param idtokenParser -
     * @param idTokenValidator -
     * @param jsonDeserializer -
     */
    public OpenIdAuthorizationCodeClient(
            final OpenIdAuthorizationCodeConfig config,
            final HttpClient httpClient,
            final IdTokenParser idtokenParser,
            final IdTokenValidator idTokenValidator,
            final JsonDeserializer jsonDeserializer
    ) {
        this.config = config;
        this.idTokenValidator = idTokenValidator;

        this.tokenRequestFactory = new OpenIdAuthorizationCodeTokenRequestFactory(config, httpClient, jsonDeserializer, idtokenParser);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public OpenIdAuthorizationCodeFlowBuilder request() {
        return new OpenIdAuthorizationCodeFlowBuilder(
                config,
                idTokenValidator,
                tokenRequestFactory
        );
    }

    /**
     * Refreshes the access token with refresh token.
     *
     * @param refreshToken -
     * @return -
     * @throws InterruptedException -
     * @throws OAuthException -
     */
    public OpenIdAuthorizationCodeResponse refresh(final String refreshToken) throws InterruptedException, OAuthException {
        return tokenRequestFactory.refresh(refreshToken, "refresh-" + System.currentTimeMillis()).call();
    }

}
