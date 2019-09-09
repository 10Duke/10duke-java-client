/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.oauth.authorizationcode.AuthorizationCodeResponse;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import org.junit.Test;

public class OpenIdAuthorizationCodeResponseTest {

    private final IdToken idToken = new IdToken("iss", "sub", List.of("aud"), Instant.MIN, Instant.MAX, Instant.MIN, "nonce", "acr", List.of("amr"), "azp");

    @Test
    public void shouldNoArgConstruct() {
        new OpenIdAuthorizationCodeResponse();
    }

    @Test
    public void shouldConstruct() {
        final OpenIdAuthorizationCodeResponse response = new OpenIdAuthorizationCodeResponse("a", "r", 42, "t", idToken);

        assertThat(response.getAccessToken()).isEqualTo("a");
        assertThat(response.getRefreshToken()).isEqualTo("r");
        assertThat(response.getExpiresIn()).isEqualTo(42);
        assertThat(response.getIdToken()).isSameAs(idToken);
        assertThat(response.getTokenType()).isEqualTo("t");
    }

    @Test
    public void shouldCopyConstructFromAuthorizationCodeResponse() {
        final AuthorizationCodeResponse acResponse = new AuthorizationCodeResponse("a", "r", 42, "t");

        acResponse.zetAdditionalProperty("hello", "world");

        final OpenIdAuthorizationCodeResponse response = new OpenIdAuthorizationCodeResponse(acResponse, idToken);

        assertThat(response.getAccessToken()).isEqualTo("a");
        assertThat(response.getRefreshToken()).isEqualTo("r");
        assertThat(response.getExpiresIn()).isEqualTo(42);
        assertThat(response.getIdToken()).isSameAs(idToken);
        assertThat(response.getTokenType()).isEqualTo("t");

        assertThat(response.getAdditionalProperties()).containsOnly(
                entry("hello", "world")
        );
    }

    @Test
    public void shouldHaveToString() {
        final OpenIdAuthorizationCodeResponse response = new OpenIdAuthorizationCodeResponse("a", "r", 42, "t", null);

        assertThat(response).hasToString("OpenIdAuthorizationCodeResponse{accessToken=a, refreshToken=r, expiresIn=42, tokenType=t, idToken=null}");
    }

}
