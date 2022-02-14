/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.AbstractOAuthFlowBuilder;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Builds Authorization Code flow.
 *
 */
public class AuthorizationCodeFlowBuilder extends AbstractOAuthFlowBuilder<AuthorizationCodeFlowBuilder> {

    /** Length of generated PKCE code verifier in bytes. */
    private static final int PKCE_CODE_VERIFIER_LENGTH_B = 48;

    /** Configuration. */
    private final AuthorizationCodeConfig config;

    /** Creates token requests. */
    private AuthorizationCodeTokenRequestFactory tokenRequestFactory;

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param tokenRequestFactory -
     */
    public AuthorizationCodeFlowBuilder(
            final AuthorizationCodeConfig config,
            final AuthorizationCodeTokenRequestFactory tokenRequestFactory
    ) {
        this(config, tokenRequestFactory, new SecureRandom());
    }

    /**
     * Constructs new instance.
     *
     * @param random -
     * @param tokenRequestFactory -
     * @param config -
     */
    public AuthorizationCodeFlowBuilder(
            final AuthorizationCodeConfig config,
            final AuthorizationCodeTokenRequestFactory tokenRequestFactory,
            final Random random
    ) {
        super(random);

        this.config = config;
        this.tokenRequestFactory = tokenRequestFactory;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public AuthorizationCodeFlow start() {
        final AuthorizationCodeRequest request;

        if (config.isUsePKCE()) {
            request = new AuthorizationCodeRequest(
                    config,
                    getParameters(),
                    getScopes(),
                    getState(),
                    generatePKCECodeVerifier()
            );
        } else {
            request = new AuthorizationCodeRequest(
                    config,
                    getParameters(),
                    getScopes(),
                    getState()
            );
        }

        return new AuthorizationCodeFlow(
                request,
                tokenRequestFactory
        );
    }

    /** Generates PKCE code verifier.
     *  See RFC 7636.
     *
     *  @return the PKCE code verifier
     */
    protected String generatePKCECodeVerifier() {
        return super.generateRandomString(PKCE_CODE_VERIFIER_LENGTH_B);
    }

}
