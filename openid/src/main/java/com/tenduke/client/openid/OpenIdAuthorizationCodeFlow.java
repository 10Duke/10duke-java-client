/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.authorizationcode.AbstractAuthorizationCodeFlow;
import com.tenduke.client.oauth.exceptions.OAuthException;

/**
 * OAuth 2 flow for OpenId Connect with Authorization Code.
 *
 */
public class OpenIdAuthorizationCodeFlow extends AbstractAuthorizationCodeFlow<
        OpenIdAuthorizationCodeConfig, OpenIdAuthorizationCodeRequest, OpenIdAuthorizationCodeResponse> {

    /** ID-token validator. */
    private final IdTokenValidator parsedIdToken;

    /** Creates token requests. */
    private final OpenIdAuthorizationCodeTokenRequestFactory tokenRequest;

    /**
     * Constructs new instance.
     *
     * @param request -
     * @param idTokenValidator -
     * @param tokenRequest -
     */
    public OpenIdAuthorizationCodeFlow(
            final OpenIdAuthorizationCodeRequest request,
            final IdTokenValidator idTokenValidator,
            final OpenIdAuthorizationCodeTokenRequestFactory tokenRequest
    ) {
        super(request);

        this.parsedIdToken = idTokenValidator;
        this.tokenRequest = tokenRequest;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected OpenIdAuthorizationCodeResponse exchangeCodeToToken(final String authorizationCode)
            throws InterruptedException, OAuthException {

        final OpenIdAuthorizationCodeResponse response = tokenRequest.create(authorizationCode, getRequest()).call();

        parsedIdToken.validate(
                response.getIdToken(),
                null,
                super.getRequest().gimmeParameter("nonce")
        );

        return response;
    }

}
