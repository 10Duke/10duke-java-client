/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.OAuthRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Base for Authorization Code requests.
 *
 * @param <C> type of the OAuth configuration
 */
public class AbstractAuthorizationCodeRequest<C extends AuthorizationCodeConfig> extends OAuthRequest<C> {

    /** Base64-encoder for PKCE code challenge. */
    private static final Base64.Encoder CODE_CHALLENGE_BASE64 = Base64.getUrlEncoder().withoutPadding();

    /** PKCE code verifier (see RFC 7636). If {@code null}, not in use. */
    private @Nullable final String codeVerifier;

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param parameters -
     * @param scopes -
     * @param state -
     */
    public AbstractAuthorizationCodeRequest(
            final C config,
            final Map<String, String> parameters,
            final Set<String> scopes,
            final String state
    ) {
        this(config, parameters, scopes, state, null);
    }

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param parameters -
     * @param scopes -
     * @param state -
     * @param codeVerifier -
     */
    public AbstractAuthorizationCodeRequest(
            final C config,
            final Map<String, String> parameters,
            final Set<String> scopes,
            final String state,
            @Nullable final String codeVerifier
    ) {
        super(config, parameters, "code", scopes, state);

        this.codeVerifier = codeVerifier;
    }

    /** Returns the code verifier.
     *
     * @return -
     */
    public String getCodeVerifier() {
        return codeVerifier;
    }


    /** Checks if this is PKCE-request.
     *
     * @return -
     */
    public boolean isPKCERequest() {
        return (codeVerifier != null);
    }


    /** {inheritDoc}
     *
     * <p>
     * Adds PKCE code challenge, if used, to the authorization request query parameters.
     *
     * @return -
     */
    @Override
    protected StringBuilder buildQuery() {
        final StringBuilder query = super.buildQuery();

        // PKCE in use:
        if (isPKCERequest()) {
            query.append("&code_challenge_method=S256");
            query.append("&code_challenge=").append(encode(computeCodeChallenge()));
        }

        return query;
    }

    /** Computes PKCE code challenge.
     *
     * @return -
     */
    protected String computeCodeChallenge() {
        final MessageDigest md = createDigest("SHA-256");
        final byte[] verifierBytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        final byte[] codeChallenge = md.digest(verifierBytes);

        return CODE_CHALLENGE_BASE64.encodeToString(codeChallenge);
    }

    /** Creates new {@link MessageDigest} instance.
     *
     * @param algorithm -
     * @return -
     */
    protected static MessageDigest createDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithtm", e);
        }
    }

}
