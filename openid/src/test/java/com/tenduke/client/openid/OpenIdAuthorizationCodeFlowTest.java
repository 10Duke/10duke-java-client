/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeResponse;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import com.tenduke.client.testutils.HttpClientTestUtil;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenIdAuthorizationCodeFlowTest {

    private OpenIdAuthorizationCodeConfig config;
    private HttpClient http;
    private JsonDeserializer json;
    private OpenIdAuthorizationCodeRequest request;
    private OpenIdAuthorizationCodeFlow flow;
    private IdTokenParser parseIdToken;
    private IdTokenValidator validateIdToken;
    private IdToken idToken;

    @Before
    public void beforeTest() throws Exception {
        config = new OpenIdAuthorizationCodeConfig(
                "unit-test",
                URI.create("https://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("https://example.com/token"),
                "1-2-3-4-5",
                "is-sue-r",
                null
        );

        http = mock(HttpClient.class);
        json = mock(JsonDeserializer.class);
        request = new OpenIdAuthorizationCodeRequest(
                config,
                Map.of("nonce", "non-sense", "hello", "world"),
                new LinkedHashSet<>(List.of("a", "b")),
                "alaska"
        );

        idToken = new IdToken();

        parseIdToken = mock(IdTokenParser.class);
        validateIdToken = mock(IdTokenValidator.class);

        flow = new OpenIdAuthorizationCodeFlow(request, validateIdToken, new OpenIdAuthorizationCodeTokenRequestFactory(config, http, json, parseIdToken));

        HttpClientTestUtil.stubHttp(http, "POST", "https://example.com/token", 200, "SIMULATED RESPONSE");

        when(parseIdToken.from("simulates-id-token")).thenReturn(idToken);

        final AuthorizationCodeResponse response = new AuthorizationCodeResponse("a", "r", 42, "t");

        response.zetAdditionalProperty("id_token", "simulates-id-token");

        when(json.deserialize("SIMULATED RESPONSE", AuthorizationCodeResponse.class)).thenReturn(response);
    }

    @Test
    public void shouldExchangeCodeToToken() throws Exception {
        final OpenIdAuthorizationCodeResponse response = flow.exchangeCodeToToken("open-sesame");

        assertThat(response.getAccessToken()).isEqualTo("a");
        assertThat(response.getRefreshToken()).isEqualTo("r");
        assertThat(response.getExpiresIn()).isEqualTo(42);
        assertThat(response.getTokenType()).isEqualTo("t");
        assertThat(response.getIdToken()).isSameAs(idToken);
        assertThat(response.getAdditionalProperties()).containsOnly(
                entry("id_token", "simulates-id-token")
        );
    }

    @Test
    public void shouldThrowOAuthServerExceptionIfResponseDoesNotContainIdToken() throws Exception {
        when(json.deserialize("SIMULATED RESPONSE", AuthorizationCodeResponse.class)).thenReturn(new AuthorizationCodeResponse("a", "r", 42, "t"));

        assertThatExceptionOfType(OAuthServerException.class).isThrownBy(() -> {
            flow.exchangeCodeToToken("open-sesame");
        }).withMessageStartingWith("Token response does not contain ID-token");
    }

}
