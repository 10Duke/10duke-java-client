/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.AbstractOAuthFlowBuilder;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Random;
import javax.annotation.Nullable;

/**
 * Builds flow for OpenId Connect with Authorization Code.
 *
 * <p>
 * NOTE: The builder automatically adds the {@code openid} scope to the request.
 *
 * <p>
 * NOTE: The builder automatically adds {@code nonce} to the request, if none is provided. Caller can provide {@code nonce}
 * either by passing it as a parameter or using method {@link #nonce(java.lang.String) }.
 */
public class OpenIdAuthorizationCodeFlowBuilder extends AbstractOAuthFlowBuilder<OpenIdAuthorizationCodeFlowBuilder> {

    /** Default length of nonce in bytes. */
    private static final int DEFAULT_NONCE_LENGTH_B = 16;

    /** Configuration. */
    private final OpenIdAuthorizationCodeConfig config;

    /** Generated nonce, in case user has not provided one. */
    private @Nullable String generatedNonce;

    /** ID-token validator. */
    private final IdTokenValidator idTokenValidator;

    /** Nonce. */
    private @Nullable String nonce;

    /** Creates OpenID Connect token requests. */
    private final OpenIdAuthorizationCodeTokenRequestFactory tokenRequestFactory;

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param idTokenValidator -
     * @param tokenRequestFactory -
     */
    public OpenIdAuthorizationCodeFlowBuilder(
            final OpenIdAuthorizationCodeConfig config,
            final IdTokenValidator idTokenValidator,
            final OpenIdAuthorizationCodeTokenRequestFactory tokenRequestFactory
    ) {
        this(config, idTokenValidator, tokenRequestFactory, new SecureRandom());
    }

    /**
     * Constructs new instance.
     *
     * @param random -
     * @param config -
     * @param idTokenValidator -
     * @param tokenRequestFactory -
     */
    public OpenIdAuthorizationCodeFlowBuilder(
            final OpenIdAuthorizationCodeConfig config,
            final IdTokenValidator idTokenValidator,
            final OpenIdAuthorizationCodeTokenRequestFactory tokenRequestFactory,
            final Random random
    ) {
        super(random);
        this.config = config;
        this.idTokenValidator = idTokenValidator;
        this.tokenRequestFactory = tokenRequestFactory;
    }

    /**
     * Sets nonce.
     *
     * <p>
     * <b>IMPORTANT NOTE:</b> Nonce can also be set as a parameter. If set as parameter, it overrides this.
     *
     * @param nonce -
     * @return -
     */
    public OpenIdAuthorizationCodeFlowBuilder nonce(final String nonce) {
        this.nonce = nonce;

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return -
     */
    @Override
    public OpenIdAuthorizationCodeFlow start() {
        final LinkedHashMap<String, String> effectiveParameters = new LinkedHashMap<>(getParameters());

        effectiveParameters.put("nonce", determineNonce());

        final LinkedHashSet<String> originalScopes = getScopes();
        final LinkedHashSet<String> effectiveScopes = new LinkedHashSet<>(originalScopes.size() + 1);

        effectiveScopes.add("openid");
        effectiveScopes.addAll(originalScopes);


        final OpenIdAuthorizationCodeRequest request;

        if (config.isUsePKCE()) {
            request = new OpenIdAuthorizationCodeRequest(
                    config,
                    effectiveParameters,
                    effectiveScopes,
                    getState(),
                    generatePKCECodeVerifier()
            );
        } else {
            request = new OpenIdAuthorizationCodeRequest(
                    config,
                    effectiveParameters,
                    effectiveScopes,
                    getState()
            );
        }

        return new OpenIdAuthorizationCodeFlow(
                request,
                idTokenValidator,
                tokenRequestFactory
        );
    }

    /**
     * Determines nonce to use: If user has provided nonce, return that. Otherwise, generate random nonce and return it.
     *
     * @return -
     */
    protected String determineNonce() {
        @Nullable final String nonceParameter = getParameters().get("nonce");

        // If user has provided nonce as parameter, return that
        if (nonceParameter != null) {
            return nonceParameter;
        }

        // If no nonce configured, generate random nonce
        if (nonce == null) {
            if (generatedNonce == null) {
                generatedNonce = generateRandomString(DEFAULT_NONCE_LENGTH_B);
            }
            return generatedNonce;
        }

        // Otherwise return the nonce
        return nonce;
    }

}
