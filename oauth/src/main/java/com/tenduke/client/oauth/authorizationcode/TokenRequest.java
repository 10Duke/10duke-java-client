/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.exceptions.OAuthException;
import java.util.concurrent.Callable;

/**
 * A token request for acquiring or refreshing access token.
 *
 * @param <RS> type of the response
 */
public interface TokenRequest<RS extends AuthorizationCodeResponse> extends Callable<RS> {

    /**
     * {@inheritDoc}
     *
     */
    @Override
    RS call() throws InterruptedException, OAuthException;

}
