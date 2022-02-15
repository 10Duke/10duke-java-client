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
import com.tenduke.client.oauth.QueryParser;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.testutils.ChecksumUtil;
import com.tenduke.client.testutils.HttpClientTestUtil;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Flow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorizationCodeClientTest {

    private AuthorizationCodeConfig config;
    private AuthorizationCodeClient client;
    private JsonDeserializer json;
    private HttpClient http;
    private ArgumentCaptor<HttpRequest> requestArg;
    private ArgumentCaptor<HttpResponse.BodyHandler<String>> handlerArg;
    private Random random;
    private Random randomSpy;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest()
    {
        config = new AuthorizationCodeConfig(
                "tenduke",
                URI.create("http://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("http://example.com/token"),
                "igotthesecret"
        );

        http = mock(HttpClient.class);
        json = mock(JsonDeserializer.class);
        random = new Random();
        randomSpy = spy(random);

        client = new AuthorizationCodeClient(
                config,
                http,
                json,
                randomSpy
        );

        requestArg = ArgumentCaptor.forClass(HttpRequest.class);
        handlerArg = ArgumentCaptor.forClass(HttpResponse.BodyHandler.class);
    }

    @Test
    public void shouldUsePKCEIfConfigured() throws InterruptedException, IOException, JsonDeserializationException, OAuthException {
        config = new AuthorizationCodeConfig(
                "tenduke",
                URI.create("http://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("http://example.com/token"),
                "igotthesecret",
                true
        );

        client = new AuthorizationCodeClient(config, http, json, randomSpy);

        final AuthorizationCodeFlow flow = client.request()
                .scope("profile")
                .scope("email")
                .scope("https://apis.10duke.com/auth/openidconnect/organization")
                .parameter("madeup", "fake")
                .parameter("http://example.com", "tenduke://callback")
                .state("alaska")
                .start();

        assertThat(flow.getRequest().toUrl())
                .hasProtocol("http")
                .hasHost("example.com")
                .hasParameter("client_id", "tenduke")
                .hasParameter("redirect_uri", "tenduke://callback")
                .hasParameter("response_type", "code")
                .hasParameter("scope", "profile email https://apis.10duke.com/auth/openidconnect/organization")
                .hasParameter("state", "alaska")
                // custom parameters:
                .hasParameter("madeup", "fake")
                .hasParameter("http://example.com", "tenduke://callback")
                .hasNoParameter("client_secret")
                // PKCE_parameters:
                .hasParameter("code_challenge_method", "S256")
                .hasParameter("code_challenge", ChecksumUtil.urlSafeSHA256(flow.getRequest().getCodeVerifier()));

        HttpClientTestUtil.stubHttp(http, "POST", "http://example.com/token", 200, RESPONSE);

        final AuthorizationCodeResponse expected = new AuthorizationCodeResponse("open-sesame", "water", 42, "Bearer");

        when(json.deserialize(RESPONSE, AuthorizationCodeResponse.class)).thenReturn(expected);

        assertThat(flow.process("tenduke://callback?code=co-de&state=alaska")).isSameAs(expected);

        // Verify the token-request parameters:
        verify(http, times(1)).send(requestArg.capture(), handlerArg.capture());

        requestArg.getValue().bodyPublisher().get().subscribe(new TestSubscriber() {
            @Override
            public void onNext(final ByteBuffer item) {
                final QueryParser parseQuery = new QueryParser(UTF_8);

                assertThat(parseQuery.from(StandardCharsets.UTF_8.decode(item).toString())).containsOnly(
                        entry("client_id", List.of("tenduke")),
                        entry("client_secret", List.of("igotthesecret")),
                        entry("code", List.of("co-de")),
                        entry("grant_type", List.of("authorization_code")),
                        entry("redirect_uri", List.of("tenduke://callback")),
                        entry("code_verifier", List.of(flow.getRequest().getCodeVerifier()))
                );
            }
        });
    }

    @Test
    public void shouldProcessFlowInSuccessCase() throws InterruptedException, IOException, JsonDeserializationException, OAuthException {
        final AuthorizationCodeFlow flow = client.request()
                .scope("profile")
                .scope("email")
                .scope("https://apis.10duke.com/auth/openidconnect/organization")
                .parameter("madeup", "fake")
                .parameter("http://example.com", "tenduke://callback")
                .state("alaska")
                .start();

        assertThat(flow.getRequest().toUrl())
                .hasProtocol("http")
                .hasHost("example.com")
                .hasParameter("client_id", "tenduke")
                .hasParameter("redirect_uri", "tenduke://callback")
                .hasParameter("response_type", "code")
                .hasParameter("scope", "profile email https://apis.10duke.com/auth/openidconnect/organization")
                .hasParameter("state", "alaska")
                // custom parameters:
                .hasParameter("madeup", "fake")
                .hasParameter("http://example.com", "tenduke://callback")
                .hasNoParameter("client_secret")
        ;

        HttpClientTestUtil.stubHttp(http, "POST", "http://example.com/token", 200, RESPONSE);

        final AuthorizationCodeResponse expected = new AuthorizationCodeResponse("open-sesame", "water", 42, "Bearer");

        when(json.deserialize(RESPONSE, AuthorizationCodeResponse.class)).thenReturn(expected);

        assertThat(flow.process("tenduke://callback?code=co-de&state=alaska")).isSameAs(expected);

        // Verify the token-request parameters:
        verify(http, times(1)).send(requestArg.capture(), handlerArg.capture());

        requestArg.getValue().bodyPublisher().get().subscribe(new TestSubscriber() {
            @Override
            public void onNext(final ByteBuffer item) {
                final QueryParser parseQuery = new QueryParser(UTF_8);

                assertThat(parseQuery.from(StandardCharsets.UTF_8.decode(item).toString())).containsOnly(
                        entry("client_id", List.of("tenduke")),
                        entry("client_secret", List.of("igotthesecret")),
                        entry("code", List.of("co-de")),
                        entry("grant_type", List.of("authorization_code")),
                        entry("redirect_uri", List.of("tenduke://callback"))
                );
            }
        });

    }

    @Test
    public void shouldRefreshAToken() throws InterruptedException, IOException, JsonDeserializationException, OAuthException {
        HttpClientTestUtil.stubHttp(http, "POST", "http://example.com/token", 200, RESPONSE);

        final AuthorizationCodeResponse expected = new AuthorizationCodeResponse("open-sesame", "water", 42, "Bearer");

        when(json.deserialize(RESPONSE, AuthorizationCodeResponse.class)).thenReturn(expected);

        assertThat(client.refresh("1-2-3-4-5")).isSameAs(expected);

        // Verify the refresh-token-request parameters:
        verify(http, times(1)).send(requestArg.capture(), handlerArg.capture());

        requestArg.getValue().bodyPublisher().get().subscribe(new TestSubscriber() {
            @Override
            public void onNext(final ByteBuffer item) {
                final QueryParser parseQuery = new QueryParser(UTF_8);

                assertThat(parseQuery.from(StandardCharsets.UTF_8.decode(item).toString())).containsOnly(
                        entry("client_id", List.of("tenduke")),
                        entry("client_secret", List.of("igotthesecret")),
                        entry("grant_type", List.of("refresh_token")),
                        entry("refresh_token", List.of("1-2-3-4-5"))
                );
            }
        });
    }

    @Test
    public void shouldConstructWithOnlyConfig() {
        assertThat(new AuthorizationCodeClient(config).request()).isNotNull();
    }

    private static final String RESPONSE = ""
            + "{ \n"
            + "    \"access_token\": \"open-sesame\" \n"
            + "  , \"refresh_token\": \"water\" \n"
            + "  , \"token_type\": \"Bearer\" \n"
            + "  , \"expires_in\": 42 \n"
            + "} \n"
            + "";

    private abstract static class TestSubscriber implements Flow.Subscriber<ByteBuffer> {

        @Override
        public void onSubscribe(final Flow.Subscription subscription) {
            subscription.request(1L);
        }

        @Override
        public void onError(final Throwable throwable) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onComplete() {
            // Intentionally empty
        }

    }
}
