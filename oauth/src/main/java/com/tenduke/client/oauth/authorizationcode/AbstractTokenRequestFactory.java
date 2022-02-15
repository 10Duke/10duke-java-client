/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import javax.annotation.Nullable;

/**
 * Base class for token-request factories.
 *
 * @param <C> type of configuration
 * @param <RQ> type of request
 * @param <RS> type of the response
 * @param <TR> type of the token request
 */
public abstract class AbstractTokenRequestFactory<
        C extends AuthorizationCodeConfig,
        RQ extends AbstractAuthorizationCodeRequest<C>,
        RS extends AuthorizationCodeResponse,
        TR extends TokenRequest<RS>
> {

    /** Character-set in which the request body is encoded. */
    private final Charset encoding = UTF_8;

    /** Config. */
    private final AuthorizationCodeConfig config;

    /**
     * Constructs new instance.
     *
     * @param config -
     */
    protected AbstractTokenRequestFactory(final AuthorizationCodeConfig config) {
        this.config = config;
    }

    /**
     * Creates new token-request.
     *
     * <p>
     * NOTE: This method is deprecated: It does not support PKCE.
     * Use {@link #create(java.lang.String, com.tenduke.client.oauth.authorizationcode.AbstractAuthorizationCodeRequest) } instead.
     *
     * @param authorizationCode -
     * @param state -
     * @return -
     */
    @Deprecated
    public TR create(final String authorizationCode, final String state) {
        return createRequest(buildTokenRequestBody(authorizationCode).toString(), state, getTokenEndpoint());
    }

    /**
     * Creates new token-request.
     *
     * @param authorizationCode -
     * @param request -
     * @return -
     */
    public TR create(final String authorizationCode, final RQ request) {
        return createRequest(
                buildTokenRequestBody(authorizationCode, request).toString(),
                request.getState(),
                getTokenEndpoint()
        );
    }

    /**
     * Refreshes access token with refresh token.
     *
     * @param refreshToken -
     * @param state -
     * @return -
     */
    public TR refresh(final String refreshToken, final String state) {
        final StringBuilder requestBody = new StringBuilder();

        appendParameter(requestBody, "grant_type", "refresh_token");
        appendParameter(requestBody, "refresh_token", refreshToken);
        appendParameter(requestBody, "client_id", getClientId());
        appendParameter(requestBody, "client_secret", getClientSecret());

        return createRefreshTokenRequest(requestBody.toString(), state, getRefreshTokenEndpoint());
    }

    /** Builds token request body.
     *
     * @param authorizationCode -
     * @param request -
     * @return -
     */
    protected StringBuilder buildTokenRequestBody(final String authorizationCode, final RQ request) {
        final StringBuilder requestBody = buildTokenRequestBody(authorizationCode);

        if (request.isPKCERequest()) {
            appendParameter(requestBody, "code_verifier", request.getCodeVerifier());
        }

        return requestBody;
    }

    /** Builds token request body.
     *
     * @param authorizationCode -
     * @return -
     */
     protected StringBuilder buildTokenRequestBody(final String authorizationCode) {
        final StringBuilder requestBody = new StringBuilder();

        appendParameter(requestBody, "grant_type", "authorization_code");
        appendParameter(requestBody, "code", authorizationCode);
        appendParameter(requestBody, "redirect_uri", getRedirectUri());
        appendParameter(requestBody, "client_id", getClientId());
        appendParameter(requestBody, "client_secret", getClientSecret());

        return requestBody;
    }

    /**
     * Creates the actual access-token request.
     *
     * @param requestBody -
     * @param state -
     * @param tokenEndpoint -
     * @return -
     */
    protected abstract TR createRequest(String requestBody, String state, URI tokenEndpoint);

    /**
     * Creates the actual refresh-token request. This implementation calls
     * {@link #createRequest(java.lang.String, java.lang.String, java.net.URI) }.
     *
     * @param requestBody -
     * @param state -
     * @param tokenEndpoint -
     * @return -
     */
    protected TR createRefreshTokenRequest(final String requestBody, final String state, final URI tokenEndpoint) {
        return createRequest(requestBody, state, tokenEndpoint);
    }

    protected void appendParameter(
            final StringBuilder builder,
            final String parameterName,
            @Nullable final String parameterValue
    ) {
        if (parameterValue != null) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(encode(parameterName)).append('=').append(encode(parameterValue));
        }
    }

    /**
     * URL-encodes given string.
     *
     * @param string -
     * @return -
     */
    protected String encode(final String string) {
        return URLEncoder.encode(string, encoding);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod

    protected String getClientId() {
        return config.getClientId();
    }

    protected String getClientSecret() {
        return config.getClientSecret();
    }

    protected String getRedirectUri() {
        return config.getRedirectUri().toString();
    }

    protected URI getTokenEndpoint() {
        return config.getTokenEndpoint();
    }

    protected URI getRefreshTokenEndpoint() {
        return config.getTokenEndpoint();
    }

    // CSON: JavadocMethod
    // </editor-fold>
}
