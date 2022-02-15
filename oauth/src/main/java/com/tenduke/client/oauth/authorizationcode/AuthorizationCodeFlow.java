/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.exceptions.OAuthException;

/**
 * Implements OAuth 2.0 Authorization Code flow.
 *
 */
public class AuthorizationCodeFlow extends AbstractAuthorizationCodeFlow<
        AuthorizationCodeConfig,
        AuthorizationCodeRequest,
        AuthorizationCodeResponse
> {
    /** Creates token requests. */
    private final AuthorizationCodeTokenRequestFactory tokenRequest;

    /**
     * Constructs new instance.
     *
     * @param request -
     * @param tokenRequestFactory -
     */
    public AuthorizationCodeFlow(
            final AuthorizationCodeRequest request,
            final AuthorizationCodeTokenRequestFactory tokenRequestFactory
    ) {
        super(request);

        this.tokenRequest = tokenRequestFactory;
    }

    /**
     * {@inheritDoc}
     *
     * @param authorizationCode -
     * @return -
     * @throws InterruptedException -
     * @throws OAuthException -
     */
    @Override
    protected AuthorizationCodeResponse exchangeCodeToToken(final String authorizationCode) throws InterruptedException, OAuthException {
        return tokenRequest.create(authorizationCode, getRequest()).call();
    }

}
