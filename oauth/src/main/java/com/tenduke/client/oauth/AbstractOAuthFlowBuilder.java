/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

/**
 * Abstract base implementation of a {@link OAuthFlowBuilder}.
 *
 * @param <B> The type of the builder to return (for subclassing)
 */
public abstract class AbstractOAuthFlowBuilder<B extends AbstractOAuthFlowBuilder<B>> implements OAuthFlowBuilder {

    /** Length of generated state in bytes. */
    private static final int GENERATED_STATE_LENGTH_B = 16;

    /** Length of generated PKCE code verifier in bytes. */
    private static final int PKCE_CODE_VERIFIER_LENGTH_B = 48;

    /** Encodes generated values. */
    private static final Base64.Encoder BASE64 = Base64.getUrlEncoder().withoutPadding();

    /** Generated OAuth-state, used if state not provided by user. */
    private @Nullable String generatedState;

    /** Additional parameters. */
    private final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();

    /** RNG for generating state-parameter if not provided by user. */
    private final Random random;

    /** OAuth scopes to request. */
    private final LinkedHashSet<String> scopes = new LinkedHashSet<>();

    /** OAuth state. */
    private @Nullable String state;

    /**
     * Constructs new instance.
     *
     * @param random -
     */
    protected AbstractOAuthFlowBuilder(final Random random) {
        this.random = random;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public B parameter(final String name, final String value) {
        parameters.put(name, value);

        return self();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public B parameters(final Map<String, String> values) {
        parameters.putAll(values);

        return self();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public B scope(final String scope) {
        this.scopes.add(scope);

        return self();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public B scopes(final Collection<String> scopes) {
        this.scopes.addAll(scopes);

        return self();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public B scopes(final String... scopes) {
        if (scopes != null) {
            for (final String scope : scopes) {
                this.scopes.add(scope);
            }
        }

        return self();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public B state(final String state) {
        this.state = state;

        return self();
    }

    /**
     * Typecast-trick for returning the current builder instance.
     *
     * @return -
     */
    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

    /**
     * Returns the parameters.
     *
     * @return -
     */
    public LinkedHashMap<String, String> getParameters() {
        return parameters;
    }

    /**
     * Returns the scopes.
     *
     * @return -
     */
    public LinkedHashSet<String> getScopes() {
        return scopes;
    }

    /**
     * Returns the state.
     *
     * <p>
     * This implementation prefers {@code state} parameter, if set by user. Otherwise, it uses the state set with
     * {@link #state(java.lang.String)}. If that is {@code null}, it generates a random state of 16 bytes, encoded in Base-64.
     *
     * @return state either set as parameter, with method or generated. The returned value is never {@code null}.
     */
    public String getState() {
        @Nullable final String stateParameter = parameters.get("state");

        // Use "state"-parameter, if provided by user
        if (stateParameter != null) {
            return stateParameter;
        }

        // If state not set, generate a state and return that
        if (state == null) {

            if (generatedState == null) {
                generatedState = generateRandomString(GENERATED_STATE_LENGTH_B);
            }

            return generatedState;
        }

        return state;
    }

    /**
     * Generates URL-safe base-64 encoded string of random bytes. Note that parameter {@code lengthInBytes} is the length of generated
     * bytes, this gets expanded by base-64 encoding.
     *
     * @param lengthInBytes -
     * @return -
     */
    protected String generateRandomString(final int lengthInBytes) {
        final byte[] randomBytes = new byte[GENERATED_STATE_LENGTH_B];

        random.nextBytes(randomBytes);

        return BASE64.encodeToString(randomBytes);
    }

        /** Generates PKCE code verifier.
     *  See RFC 7636.
     *
     *  @return the PKCE code verifier
     */
    protected String generatePKCECodeVerifier() {
        return generateRandomString(PKCE_CODE_VERIFIER_LENGTH_B);
    }

}
