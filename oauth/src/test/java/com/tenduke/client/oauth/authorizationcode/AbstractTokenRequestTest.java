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
import static com.tenduke.client.testutils.HttpClientTestUtil.stubHttp;
import static com.tenduke.client.testutils.HttpClientTestUtil.stubHttpToThrowIOException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractTokenRequestTest {

    private Impl request;
    private HttpClient http;
    private JsonDeserializer json;

    @Before
    public void beforeTest() {
        http = mock(HttpClient.class);
        json = mock(JsonDeserializer.class);
        request = new Impl(http, json, "rq", "alaska", URI.create("https://example.com/token"));
    }

    @Test
    public void shouldExchangeCodeToToken() throws Exception {
        final AuthorizationCodeResponse response = new AuthorizationCodeResponse("a-t", "r-t", 42, "t-t");

        stubHttp(http, "POST", "https://example.com/token", 200, "SIMULATED RESPONSE");

        when(json.deserialize("SIMULATED RESPONSE", AuthorizationCodeResponse.class)).thenReturn(response);

        assertThat(request.call()).isSameAs(response);
    }


    @Test
    public void tokenExchangeShouldWrapIOExceptionThrownByHttpClientToOAuthNetworkException() throws Exception {
        stubHttpToThrowIOException(http, "POST", "https://example.com/token");

        assertThatExceptionOfType(OAuthNetworkException.class).isThrownBy(() -> {
            request.call();
        })
                .withMessage("Token request failed")
                .withCauseInstanceOf(IOException.class);
    }

    @Test
    public void tokenExchangeShouldThrowOAuthServerExceptionIfTokenIsNotValidJson() throws Exception {
        stubHttp(http, "POST", "https://example.com/token", 200, "SIMULATED RESPONSE");

        when(json.deserialize("SIMULATED RESPONSE", AuthorizationCodeResponse.class)).thenThrow(new JsonDeserializationException("simulated exception"));

        assertThatExceptionOfType(OAuthServerException.class).isThrownBy(() -> {
            request.call();
        })
                .withMessage("Token response is not valid Json")
                .withCauseInstanceOf(JsonDeserializationException.class);
    }

    @Test
    public void tokenExchangeShouldThrowOAuthErrorExceptionIfHttpStatusCodeIndicatesError() throws Exception {
        stubHttp(http, "POST", "https://example.com/token", 400, "SIMULATED RESPONSE");

        final OAuthErrorResponse error = new OAuthErrorResponse("simulated failure", null, null);

        when(json.deserialize("SIMULATED RESPONSE", OAuthErrorResponse.class)).thenReturn(error);

        assertThatExceptionOfType(OAuthErrorException.class).isThrownBy(() -> {
            request.call();
        }).withMessage("Token request failed with error");
    }

    @Test
    public void tokenExchangeShouldThrowOAuthServerExceptionIfErrorResponseIsNotValidJson() throws Exception {
        stubHttp(http, "POST", "https://example.com/token", 400, "SIMULATED RESPONSE");

        when(json.deserialize("SIMULATED RESPONSE", OAuthErrorResponse.class)).thenThrow(new JsonDeserializationException("simulated exception"));

        assertThatExceptionOfType(OAuthServerException.class).isThrownBy(() -> {
            request.call();
        })
                .withMessage("Error response is not valid Json")
                .withCauseInstanceOf(JsonDeserializationException.class);
    }

    @Test
    public void testIsSuccessfulResponse() {
        assertThat(request.isSuccessfulResponse(199)).isFalse();
        assertThat(request.isSuccessfulResponse(200)).isTrue();
        assertThat(request.isSuccessfulResponse(250)).isTrue();
        assertThat(request.isSuccessfulResponse(299)).isTrue();
        assertThat(request.isSuccessfulResponse(300)).isFalse();
        assertThat(request.isSuccessfulResponse(400)).isFalse();
    }

    private static class Impl extends AbstractTokenRequest<AuthorizationCodeResponse> {

        public Impl(HttpClient httpClient, JsonDeserializer json, String request, String state, URI tokenEndpoint) {
            super(httpClient, json, request, state, tokenEndpoint);
        }

        @Override
        protected AuthorizationCodeResponse deserializeTokenResponse(final String body) throws JsonDeserializationException, OAuthException {
            return super.deserialize(body, AuthorizationCodeResponse.class);
        }

    }

}
