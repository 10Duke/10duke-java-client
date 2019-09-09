/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import com.tenduke.client.oauth.exceptions.OAuthException;
import javax.annotation.Nullable;

/**
 * Represents an OAuth flow.
 *
 * @param <R> type of the response in success case
 */
public interface OAuthFlow<R extends OAuthResponse> {

    /**
     * Process an uri.
     *
     * <p>
     * Typically the caller listens for URL changes in the browser. Caller can then parse the new URL to see if the URL is the
     * success/failure redirection and extract the response from URL parameters.
     *
     * @param uri the URI to process
     * @return the response. If {@code}, then the URI is not redirect-URI
     * @throws InterruptedException if processing of the URI was interrupted.
     * @throws OAuthException -
     */
    @Nullable R process(String uri) throws InterruptedException, OAuthException;
}
