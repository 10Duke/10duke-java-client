/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Base OAuth 2 request.
 *
 * @param <C> type of the OAuth configuration
 */
public class OAuthRequest<C extends OAuthConfig> {

    // <editor-fold desc="Fields">

    /** Character encoding. */
    private final Charset charset = UTF_8;

    /** Configuration. */
    private final C config;

    /** Response-type parameter, e.g.&nbsp;"code". */
    private final String responseType;

    /** Additional parameters, provided by user.*/
    private final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();

    /** Requested OAuth scopes. */
    private final LinkedHashSet<String> scopes = new LinkedHashSet<>();

    /** OAuth state. */
    private final String state;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">

    /**
     * Constructs new instance.
     *
     * @param config -
     * @param parameters -
     * @param responseType -
     * @param scopes -
     * @param state -
     */
    public OAuthRequest(
            final C config,
            final Map<String, String> parameters,
            final String responseType,
            final Set<String> scopes,
            final String state
    ) {
        this.config = config;
        this.parameters.putAll(parameters);
        this.responseType = responseType;
        this.scopes.addAll(scopes);
        this.state = state;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    // CSOFF: JavadocMethod
    public @Nullable String getState() {
        return state;
    }

    public String getClientId() {
        return config.getClientId();
    }

    /**
     * Returns the configuration.
     *
     * @return -
     */
    protected C getConfig() {
        return config;
    }

    public URI getRedirectUri() {
        return config.getRedirectUri();
    }
    // CSON: JavadocMethod
    // </editor-fold>

    /**
     * Checks if given URI is configured redirect URI.
     *
     * @param uri -
     * @return -
     */
    public boolean izRedirectUri(final String uri) {
        return uri.startsWith(config.getRedirectUri().toString());
    }

    /**
     * Builds OAuth request URL.
     *
     * @return -
     */
    public URL toUrl() {
        final StringBuilder url = new StringBuilder();

        url.append(config.getAuthorizationEndpoint().toString());

        final StringBuilder query = buildQuery();

        if (query.length() > 0) {
            url.append('?').append(query);
        }

        return createUrlFrom(url.toString());
    }

    protected URL createUrlFrom(final String url) throws IllegalArgumentException {
        try {
            return new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Builds the query-part of the request URL.
     *
     * @return -
     */
    protected StringBuilder buildQuery() {
        final StringBuilder query = new StringBuilder();

        query.append("response_type=").append(encode(responseType));
        query.append("&redirect_uri=").append(encode(config.getRedirectUri().toString()));
        query.append("&client_id=").append(encode(config.getClientId()));
        query.append("&state=").append(encode(state));

        final LinkedHashSet<String> effetiveScopes = gimmeScopes();

        if (!effetiveScopes.isEmpty()) {
            query.append("&scope=").append(encode(String.join(" ", effetiveScopes)));
        }

        final LinkedHashMap<String, String> effectiveParameters = gimmeParameters();

        if (!effectiveParameters.isEmpty()) {
            for (final Map.Entry<String, String> entry : effectiveParameters.entrySet()) {
                query
                        .append('&')
                        .append(encode(entry.getKey()))
                        .append('=')
                        .append(encode(entry.getValue()));
            }
        }

        return query;
    }

    /**
     * URL-encodes given string.
     *
     * @param string -
     * @return -
     */
    protected String encode(final String string) {
        return URLEncoder.encode(string, charset);
    }

    /**
     * Returns parameters.
     *
     * @return -
     */
    protected LinkedHashMap<String, String> gimmeParameters() {
        return parameters;
    }

    /**
     * Returns named parameter.
     *
     * @param parameterName -
     * @return -
     */
    public @Nullable String gimmeParameter(final String parameterName) {
        return parameters.get(parameterName);
    }

    /**
     * Returns requested scopes.
     *
     * @return -
     */
    protected LinkedHashSet<String> gimmeScopes() {
        return scopes;
    }

}
