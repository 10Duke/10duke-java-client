/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.oauth.AbstractOAuthFlow;
import com.tenduke.client.oauth.OAuthErrorResponse;
import com.tenduke.client.oauth.exceptions.OAuthErrorException;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base-implementation for Authorization Code Flow.
 *
 * @param <C> type of the configuration for the flow
 * @param <RQ> type of the request
 * @param <RS> type of successful response
 */
public abstract class AbstractAuthorizationCodeFlow<
        C extends AuthorizationCodeConfig,
        RQ extends AbstractAuthorizationCodeRequest<C>,
        RS extends AuthorizationCodeResponse
> extends AbstractOAuthFlow<C, RQ, RS> {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractAuthorizationCodeFlow.class);

    /**
     * Constructs new instance.
     *
     * @param request -
     */
    protected AbstractAuthorizationCodeFlow(
            final RQ request
    ) {
        super(request);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public @Nullable RS process(final String uri) throws InterruptedException, OAuthException {
        LOG.debug("[state={}] Parsing redirect uri: {}", state(), uri);

        @Nullable final Map<String, List<String>> response = parseUri(uri);

        if (response == null) {
            LOG.debug("[state={}] Not redirect uri", state());

            return null;
        }

        LOG.debug("[state={}] Redirect-response from authorization server", state());

        verifyState(response);

        if (response.containsKey("error")) {
            final OAuthErrorResponse error = new OAuthErrorResponse(
                    decode(getFirst(response, "error")),
                    decode(getFirst(response, "error_description")),
                    decode(getFirst(response, "error_uri"))
            );

            LOG.error(
                    "[state={}] OAuth server responds with error: \"{}\": \"{}\" ",
                    state(),
                    error.getError(),
                    error.getErrorDescription()
            );

            throw new OAuthErrorException(
                    "OAuth server returned error response: \"" + error.getError() + "\": \"" + error.getErrorDescription() + "\"",
                    error
            );
        }

        if (response.containsKey("code")) {
            //
            final String authorizationCode = getFirst(response, "code");

            LOG.debug("[state={}][code={}] Response contains the authorization code", state(), authorizationCode);

            return exchangeCodeToToken(authorizationCode);
        }

        LOG.error("Error: Redirect-uri does not contain required parameters: {}", uri);

        throw new OAuthServerException("Redirect-uri does not contain required parameter \"code\":" + uri);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected String extractResponse(final URI uri) {
        return uri.getRawQuery();
    }

    /**
     * Exchanges the authorization code to token.
     *
     * @param authorizationCode -
     * @return -
     * @throws InterruptedException -
     * @throws OAuthException -
     */
    protected abstract RS exchangeCodeToToken(String authorizationCode) throws InterruptedException, OAuthException;

}
