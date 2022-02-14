/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.json.DefaultJsonDeserializer;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.OAuthClient;
import com.tenduke.client.oauth.exceptions.OAuthException;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Client for initiating Authorization Code flow.
 *
 */
public class AuthorizationCodeClient implements OAuthClient {

    /** The default HTTP-client, used if no other HTTP-client provided. */
    public static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

    /** Configuration. */
    private final AuthorizationCodeConfig config;

    /** Creates token requests. */
    private final AuthorizationCodeTokenRequestFactory tokenRequestFactory;

    /** Random number generator. */
    private final Random random;

    /**
     * Constructs new instance.
     *
     * @param config -
     */
    public AuthorizationCodeClient(final AuthorizationCodeConfig config) {
        this(
                config,
                DEFAULT_HTTP_CLIENT,
                DefaultJsonDeserializer.INSTANCE.get()
        );
    }

    /**
     * Constructs new instance.
     *
     * <p>
     * This constructor allows configuring the used components.
     *
     * @param config -
     * @param httpClient -
     * @param jsonDeserializer -
     */
    public AuthorizationCodeClient(
            final AuthorizationCodeConfig config,
            final HttpClient httpClient,
            final JsonDeserializer jsonDeserializer
    ) {
        this(config, httpClient, jsonDeserializer, new SecureRandom());
    }

    /**
     * Constructs new instance.
     *
     * <p>
     * This constructor allows configuring the used components.
     *
     * @param config -
     * @param httpClient -
     * @param jsonDeserializer -
     * @param random -
     */
    public AuthorizationCodeClient(
            final AuthorizationCodeConfig config,
            final HttpClient httpClient,
            final JsonDeserializer jsonDeserializer,
            final Random random
    ) {
        this.config = config;
        this.random = random;
        this.tokenRequestFactory = new AuthorizationCodeTokenRequestFactory(config, httpClient, jsonDeserializer);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public AuthorizationCodeFlowBuilder request() {
        return new AuthorizationCodeFlowBuilder(
                config,
                tokenRequestFactory,
                random
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
    public AuthorizationCodeResponse refresh(final String refreshToken) throws InterruptedException, OAuthException {
        return tokenRequestFactory.refresh(refreshToken, "refresh-" + System.currentTimeMillis()).call();
    }

}
