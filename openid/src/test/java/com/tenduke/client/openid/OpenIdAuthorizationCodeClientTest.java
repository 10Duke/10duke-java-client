/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.jwt.JwtParser;
import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeResponse;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.testutils.HttpClientTestUtil;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import static java.time.ZoneOffset.UTC;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenIdAuthorizationCodeClientTest {
    private OpenIdAuthorizationCodeClient client;
    private OpenIdAuthorizationCodeConfig config;
    private JsonDeserializer json;
    private JwtParser jwt;
    private HttpClient http;
    private LocalDate yesterday;
    private Instant issuedAt;
    private Instant expires;
    private Instant authTime;

    @Before
    public void beforeTest() throws NoSuchAlgorithmException {
        config = new OpenIdAuthorizationCodeConfig(
                "unit-test",
                URI.create("http://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("http://example.com/token"),
                "igotthesecret",
                "iss-sue-r",
                null
        );

        http = mock(HttpClient.class);
        json = mock(JsonDeserializer.class);
        jwt = mock(JwtParser.class);

        yesterday = LocalDate.now().minusDays(1);
        expires = yesterday.plusDays(2).atStartOfDay(UTC).toInstant();
        issuedAt = yesterday.atStartOfDay(UTC).toInstant();
        authTime = yesterday.plusDays(1).atStartOfDay(UTC).toInstant();

        client = new OpenIdAuthorizationCodeClient(
                config,
                http,
                new IdTokenParser(jwt),
                new IdTokenValidator("unit-test", "is-sue-r", Clock.fixed(yesterday.atStartOfDay(UTC).plusHours(36).toInstant(), UTC)),
                json
        );
    }

    @Test
    public void shouldProcessFlowInSuccessCase() throws Exception {
        config = new OpenIdAuthorizationCodeConfig(
                "unit-test",
                URI.create("http://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("http://example.com/token"),
                "igotthesecret",
                "iss-sue-r",
                null
        );

        final OpenIdAuthorizationCodeFlow flow = client.request()
                .scope("profile")
                .scope("email")
                .scope("https://apis.10duke.com/auth/openidconnect/organization")
                .parameter("madeup", "fake")
                .parameter("http://example.com", "tenduke://callback")
                .state("alaska")
                .nonce("non-sense")
                .start();

        assertThat(flow.getRequest().toUrl())
                .hasProtocol("http")
                .hasHost("example.com")
                .hasParameter("client_id", "unit-test")
                .hasParameter("redirect_uri", "tenduke://callback")
                .hasParameter("response_type", "code")
                .hasParameter("scope", "openid profile email https://apis.10duke.com/auth/openidconnect/organization")
                .hasParameter("state", "alaska")
                // custom parameters:
                .hasParameter("madeup", "fake")
                .hasParameter("http://example.com", "tenduke://callback")
                .hasNoParameter("client_secret")
                // added by OpenId-flow:
                .hasParameter("nonce", "non-sense")
        ;

        HttpClientTestUtil.stubHttp(http, "POST", "http://example.com/token", 200, RESPONSE);

        final AuthorizationCodeResponse resp = new AuthorizationCodeResponse("open-sesame", "water", 42, "Bearer");
        resp.withAdditionalProperty("id_token", "id-to-ken");

        when(json.deserialize(RESPONSE, AuthorizationCodeResponse.class)).thenReturn(resp);

        final Map<String, Object> claims = Map.ofEntries(
                entry("iss", "is-sue-r"),
                entry("sub", "sub-ject"),
                entry("aud", "unit-test"),
                entry("iat", issuedAt.getEpochSecond()),
                entry("exp", expires.getEpochSecond()),
                entry("auth_time", authTime.getEpochSecond()),
                entry("nonce", "non-sense"),
                entry("acr", "a-c-r"),
                entry("amr", List.of("a", "b")),
                entry("azp", "unit-test")
        );


        when(jwt.parse("id-to-ken")).thenReturn(claims);

        final OpenIdAuthorizationCodeResponse actual = flow.process("tenduke://callback?code=co-de&state=alaska");

        assertThat(actual).usingRecursiveComparison().isEqualTo(new OpenIdAuthorizationCodeResponse(
                "open-sesame",
                "water",
                42,
                "Bearer",
                new IdToken(
                        "is-sue-r",
                        "sub-ject",
                        List.of("unit-test"),
                        yesterday.plusDays(2).atStartOfDay(UTC).toInstant(),
                        yesterday.atStartOfDay(UTC).toInstant(),
                        yesterday.plusDays(1).atStartOfDay(UTC).toInstant(),
                        "non-sense",
                        "a-c-r",
                        List.of("a", "b"),
                        "unit-test"
                )
        ).withAdditionalProperty("id_token", "id-to-ken"));
    }

    @Test
    public void shouldUsePKCEIfConfiguredToDoSo() throws Exception {
        config = new OpenIdAuthorizationCodeConfig(
                "unit-test",
                URI.create("http://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("http://example.com/token"),
                "igotthesecret",
                "iss-sue-r",
                null,
                true
        );

        client = new OpenIdAuthorizationCodeClient(
                config,
                http,
                new IdTokenParser(jwt),
                new IdTokenValidator("unit-test", "is-sue-r", Clock.fixed(yesterday.atStartOfDay(UTC).plusHours(36).toInstant(), UTC)),
                json
        );


        final OpenIdAuthorizationCodeFlow flow = client.request()
                .scope("profile")
                .scope("email")
                .scope("https://apis.10duke.com/auth/openidconnect/organization")
                .parameter("madeup", "fake")
                .parameter("http://example.com", "tenduke://callback")
                .state("alaska")
                .nonce("non-sense")
                .start();

        assertThat(flow.getRequest().toUrl())
                .hasProtocol("http")
                .hasHost("example.com")
                .hasParameter("client_id", "unit-test")
                .hasParameter("redirect_uri", "tenduke://callback")
                .hasParameter("response_type", "code")
                .hasParameter("scope", "openid profile email https://apis.10duke.com/auth/openidconnect/organization")
                .hasParameter("state", "alaska")
                // custom parameters:
                .hasParameter("madeup", "fake")
                .hasParameter("http://example.com", "tenduke://callback")
                .hasNoParameter("client_secret")
                // added by OpenId-flow:
                .hasParameter("nonce", "non-sense")
                // PKCE
                .hasParameter("code_challenge_method", "S256")
                .hasParameter("code_challenge", s256(flow.getRequest().getCodeVerifier()))
        ;

        HttpClientTestUtil.stubHttp(http, "POST", "http://example.com/token", 200, RESPONSE);

        final AuthorizationCodeResponse resp = new AuthorizationCodeResponse("open-sesame", "water", 42, "Bearer");
        resp.withAdditionalProperty("id_token", "id-to-ken");

        when(json.deserialize(RESPONSE, AuthorizationCodeResponse.class)).thenReturn(resp);

        final Map<String, Object> claims = Map.ofEntries(
                entry("iss", "is-sue-r"),
                entry("sub", "sub-ject"),
                entry("aud", "unit-test"),
                entry("iat", issuedAt.getEpochSecond()),
                entry("exp", expires.getEpochSecond()),
                entry("auth_time", authTime.getEpochSecond()),
                entry("nonce", "non-sense"),
                entry("acr", "a-c-r"),
                entry("amr", List.of("a", "b")),
                entry("azp", "unit-test")
        );

        when(jwt.parse("id-to-ken")).thenReturn(claims);

        final OpenIdAuthorizationCodeResponse actual = flow.process("tenduke://callback?code=co-de&state=alaska");

        assertThat(actual).usingRecursiveComparison().isEqualTo(new OpenIdAuthorizationCodeResponse(
                "open-sesame",
                "water",
                42,
                "Bearer",
                new IdToken(
                        "is-sue-r",
                        "sub-ject",
                        List.of("unit-test"),
                        yesterday.plusDays(2).atStartOfDay(UTC).toInstant(),
                        yesterday.atStartOfDay(UTC).toInstant(),
                        yesterday.plusDays(1).atStartOfDay(UTC).toInstant(),
                        "non-sense",
                        "a-c-r",
                        List.of("a", "b"),
                        "unit-test"
                )
        ).withAdditionalProperty("id_token", "id-to-ken"));
    }

    @Test
    public void shouldRefreshAToken() throws InterruptedException, IOException, JsonDeserializationException, OAuthException {
        HttpClientTestUtil.stubHttp(http, "POST", "http://example.com/token", 200, RESPONSE_REFRESH);

        final OpenIdAuthorizationCodeResponse expected = new OpenIdAuthorizationCodeResponse("open-sesame", "water", 42, "Bearer", null);

        when(json.deserialize(RESPONSE_REFRESH, AuthorizationCodeResponse.class)).thenReturn(expected);

        assertThat(client.refresh("1-2-3-4-5")).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldConstructWithDefaults() {
        assertThat(new OpenIdAuthorizationCodeClient(config).request()).isNotNull();
    }

    private String s256(final String str) {
        try {
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(MessageDigest.getInstance("SHA-256").digest(str.getBytes()));
        } catch(final  NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static final String RESPONSE = ""
            + "{ \n"
            + "    \"access_token\": \"open-sesame\" \n"
            + "  , \"refresh_token\": \"water\" \n"
            + "  , \"token_type\": \"Bearer\" \n"
            + "  , \"expires_in\": 42 \n"
            + "  , \"id_token\": \"id-to-ken\" \n"
            + "} \n"
            + "";

    private static final String RESPONSE_REFRESH = ""
            + "{ \n"
            + "    \"access_token\": \"open-sesame\" \n"
            + "  , \"refresh_token\": \"water\" \n"
            + "  , \"token_type\": \"Bearer\" \n"
            + "  , \"expires_in\": 42 \n"
            + "} \n"
            + "";

}
