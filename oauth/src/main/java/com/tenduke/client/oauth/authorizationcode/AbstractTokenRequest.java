/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.OAuthErrorResponse;
import com.tenduke.client.oauth.exceptions.OAuthErrorException;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.oauth.exceptions.OAuthNetworkException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract token request.
 *
 * @param <RS> type of the token response returned.
 */
public abstract class AbstractTokenRequest<RS extends AuthorizationCodeResponse> implements TokenRequest<RS> {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTokenRequest.class);

    /** Low-end (inclusive) of HTTP-status codes regarded as "successful". */
    private static final int HTTP_OK_RANGE_LO = 200;

    /** High-end (exclusive) of HTTP-status codes regarded as "successful". */
    private static final int HTTP_OK_RANGE_HI = 300;

    /** Character set for request body encoding. */
    private final Charset charset = UTF_8;

    /** HTTP-client. */
    private final HttpClient httpClient;

    /** JSON-deserializer for unmarshalling the token. */
    private final JsonDeserializer json;

    /** Token-request body. */
    private final String request;

    /** OAuth-state. Used for logging. */
    private final String state;

    /** Token endpoint. */
    private final URI tokenEndpoint;

    /**
     * Constructs new instance.
     *
     * @param httpClient -
     * @param json -
     * @param request -
     * @param state -
     * @param tokenEndpoint -
     */
    public AbstractTokenRequest(
            final HttpClient httpClient,
            final JsonDeserializer json,
            final String request,
            final String state,
            final URI tokenEndpoint
    ) {
        this.httpClient = httpClient;
        this.json = json;
        this.request = request;
        this.state = state;
        this.tokenEndpoint = tokenEndpoint;
    }

    /** {@inheritDoc}
     *
     * @return -
     * @throws InterruptedException -
     * @throws OAuthException -
     */
    @Override
    public RS call() throws InterruptedException, OAuthException {
        //
        final HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(tokenEndpoint)
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(request, charset))
                .build();

        final HttpResponse<String> response;

        LOG.info("[state={}] Executing token request...", state);

        try {
            response = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        } catch (final IOException e) {
            LOG.info("[state={}] ... ERROR: Failed with {}: {}", state, e.getClass().getSimpleName(), e.getMessage());
            throw new OAuthNetworkException("Token request failed", e);
        }

        final int statusCode = response.statusCode();

        LOG.debug("[state={}][status={}] ... got HTTP-response ...", state, statusCode);

        final String body = response.body();

        LOG.debug("[state={}] response body: {}", state, body);

        if (isSuccessfulResponse(statusCode)) {

            try {
                final RS token = deserializeTokenResponse(body);

                LOG.info("[state={}] ... SUCCESS: Token received", state);

                return token;
            } catch (final JsonDeserializationException e) {
                LOG.error("[state={}] ... ERROR: Token is not valid Json: {}", state, e.getMessage());

                throw new OAuthServerException("Token response is not valid Json", e);
            }
        } else {

            try {
                final OAuthErrorResponse error = deserialize(body, OAuthErrorResponse.class);

                LOG.error("[state={}] ... ERROR: Error code: \"{}\"", state, error.getError());

                throw new OAuthErrorException("Token request failed with error", error);
            } catch (final JsonDeserializationException e) {
                LOG.error("[state={}] ... ERROR: Server reported error and response body is not valid JSON", state);

                throw new OAuthServerException("Error response is not valid Json", e);
            }
        }
    }

    /**
     * De-serializes JSON to Java object.
     *
     * @param <T> -
     * @param value -
     * @param type -
     * @return -
     * @throws JsonDeserializationException -
     */
    protected <T> T deserialize(final String value, final Class<T> type) throws JsonDeserializationException {
        return json.deserialize(value, type);
    }

    /**
     * De-serializes the token response.
     *
     * @param body HTTP response body
     * @return -
     * @throws JsonDeserializationException -
     * @throws OAuthException if the response is invalid json
     */
    protected abstract RS deserializeTokenResponse(String body) throws JsonDeserializationException, OAuthException;

    /**
     * Checks if the HTTP statuscode indicates success.
     *
     * @param httpStatusCode -
     * @return -
     */
    protected boolean isSuccessfulResponse(final int httpStatusCode) {
        return (httpStatusCode >= HTTP_OK_RANGE_LO && httpStatusCode < HTTP_OK_RANGE_HI);
    }

}
