/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.jwt.JwtException;
import com.tenduke.client.jwt.JwtParser;
import static com.tenduke.client.oauth.authorizationcode.AuthorizationCodeClient.DEFAULT_HTTP_CLIENT;
import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeResponse;
import com.tenduke.client.openid.IdTokenParser;
import com.tenduke.client.openid.IdTokenValidator;
import com.tenduke.client.openid.OpenIdAuthorizationCodeClient;
import com.tenduke.client.openid.OpenIdAuthorizationCodeConfig;
import com.tenduke.client.sso.LoginRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Set;
import java.util.concurrent.Semaphore;
import org.junit.After;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testfx.framework.junit.ApplicationTest;

public class BaseLoginTst extends ApplicationTest {

    protected IdpBackend backend;

    protected Semaphore semaphore;
    protected JsonDeserializer jsonDeserializer;
    protected JwtParser jwtParser;

    @Before
    public void startBackend() throws IOException {
        backend = new IdpBackend();
    }

    @Before
    public void setupTest() throws InterruptedException, JsonDeserializationException, JwtException {
        semaphore = new Semaphore(1);
        semaphore.acquire();

        jsonDeserializer = mock(JsonDeserializer.class);

        final AuthorizationCodeResponse response = new AuthorizationCodeResponse("a", "r", 42, "t");

        response.zetAdditionalProperty("id_token", "SIMULATED ID TOKEN");

        when(jsonDeserializer.deserialize("SIMULATED TOKEN RESPONSE", AuthorizationCodeResponse.class)).thenReturn(response);

        jwtParser = mock(JwtParser.class);

        when(jwtParser.parse("SIMULATED ID TOKEN")).thenReturn(
                Map.ofEntries(
                        entry("iss", "is-sue-r"),
                        entry("sub", "sub-ject"),
                        entry("aud", "unit-test"),
                        entry("exp", 3),
                        entry("iat", 2),
                        entry("auth_time", 1),
                        entry("nonce", "non-sense")
                )
        );
    }

    @After
    public void closeBackend() throws IOException {
        backend.close();
    }

    protected LoginRequest createRequest() {
        return createRequest(backend.url());
    }

    protected LoginRequest createRequest(final String baseUrl) {
        return createRequest(baseUrl, DEFAULT_HTTP_CLIENT);
    }

    protected LoginRequest createRequest(final String baseUrl, final HttpClient httpClient) {
        return new LoginRequest(
                new OpenIdAuthorizationCodeClient(
                        new OpenIdAuthorizationCodeConfig(
                                "unit-test",
                                URI.create(baseUrl + "oauth"),
                                URI.create("http://127.0.0.1:49151/login"),
                                URI.create(baseUrl + "token"),
                                "1-2-3-4-5",
                                "is-sue-r",
                                null,
                                true // Use PKCE.
                        ),
                        httpClient,
                        new IdTokenParser(jwtParser),
                        new IdTokenValidator("unit-test", "is-sue-r", Clock.fixed(Instant.ofEpochSecond(2), ZoneOffset.UTC)),
                        jsonDeserializer
                ),
                Map.of(
                        "nonce", "non-sense",
                        "state", "alaska"
                ),
                Set.of("email", "profile")
        );
    }
}
