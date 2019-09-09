/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import com.tenduke.client.oauth.exceptions.OAuthSecurityException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link OAuthFlow} providing base logic common to all flows.
 *
 * @param <C> type of the configuration
 * @param <RQ> type of the OAuth request
 * @param <RS> type of the OAuth response
 */
public abstract class AbstractOAuthFlow<C extends OAuthConfig, RQ extends OAuthRequest<C>, RS extends OAuthResponse>
        implements OAuthFlow<RS> {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractOAuthFlow.class);

    /**
     * Charset.
     */
    private final Charset charset = UTF_8;

    /**
     * The request that initiates this flow.
     */
    private final RQ request;

    /**
     * Constructs new instance.
     *
     * @param request -
     */
    protected AbstractOAuthFlow(final RQ request) {
        this.request = request;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters">

    /**
     * Returns the request that initiated this flow.
     *
     * @return -
     */
    public RQ getRequest() {
        return request;
    }

    /**
     * Returns the OAuth-state for the flow.
     *
     * @return -
     */
    public String state() {
        return request.getState();
    }

    // </editor-fold>

    /**
     * URL-decodes a String.
     *
     * @param string -
     * @return -
     */
    protected @Nullable String decode(final String string) {
        return (string == null ? null : URLDecoder.decode(string, charset));
    }

    /**
     * Extracts response from the URI.
     *
     * @param uri -
     * @return -
     */
    protected abstract @Nullable String extractResponse(URI uri);

    /**
     * Parses "query-string", URL-encoded key-value pairs.
     *
     * @param queryString -
     * @return -
     */
    protected Map<String, List<String>> parseQuery(final String queryString) {
        return new QueryParser(charset).from(queryString);
    }

    /**
     * Parses current URI provided by (typically embedded) browser.
     *
     * @param providedUri the URI to parse
     * @return the "response" as key-value pairs, parsed from the URI. Returns {@code null} if this URI is NOT a redirect-URI
     * @throws OAuthServerException if the URI is not invalid (i.e. the server provided invalid URI)
     */
    protected @Nullable Map<String, List<String>> parseUri(final String providedUri) throws OAuthServerException {

        if (request.izRedirectUri(providedUri)) {
            final URI uri;
            try {
                uri = new URI(providedUri);
            } catch (final URISyntaxException e) {
                throw new OAuthServerException("Redirection uri is invalid: " + providedUri);
            }

            @Nullable final String response = extractResponse(uri);

            if (response != null) {
                return parseQuery(response);
            }
        }

        return null;
    }


    /**
     * Verifies state.
     *
     * @param response -
     * @throws OAuthSecurityException -
     */
    protected void verifyState(final Map<String, List<String>> response) throws OAuthSecurityException {
        final String receivedState = getFirst(response, "state");

        if (!request.getState().equals(receivedState)) {
            LOG.error("[state={}][received={}] State does not match", request.getState(), receivedState);

            throw new OAuthSecurityException("State provided does not match to state received");
        }
    }

    /**
     * Returns first value from a "multimap".
     *
     * @param params -
     * @param key -
     * @return -
     */
    public static @Nullable  String getFirst(final Map<String, List<String>> params, final String key) {
        @Nullable final List<String> values = params.get(key);

        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.get(0);
    }

}
