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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenIdAuthorizationCodeTokenRequestTest {

    private JsonDeserializer json;
    private AuthorizationCodeResponse response;
    private IdTokenParser idTokenParser;

    @Before
    public void beforeTest() throws Exception {
        json = mock(JsonDeserializer.class);
        response = new AuthorizationCodeResponse("a", "r", 42, "t");
        idTokenParser = mock(IdTokenParser.class);

        when(json.deserialize("SIMULATED BODY", AuthorizationCodeResponse.class)).thenReturn(response);
    }


    @Test
    public void shouldDeserializeTheResponseWithIdToken() throws Exception {
        final IdToken idToken = new IdToken(null, null, null, null, null, null, null, null, null, null);
        response.zetAdditionalProperty("id_token", "SIMULATED ID-TOKEN");

        when(idTokenParser.from("SIMULATED ID-TOKEN")).thenReturn(new IdToken(null, null, null, null, null, null, null, null, null, null));

        assertThat(new OpenIdAuthorizationCodeTokenRequest(null, json, "request", "state", null, idTokenParser, true).deserializeTokenResponse("SIMULATED BODY"))
                .usingRecursiveComparison()
                .isEqualTo(new OpenIdAuthorizationCodeResponse(response, idToken));

    }

    @Test
    public void shouldDeserializeTheResponseWithOutIdTokenIfAllowed() throws Exception {
        assertThat(new OpenIdAuthorizationCodeTokenRequest(null, json, "request", "state", null, idTokenParser, false).deserializeTokenResponse("SIMULATED BODY"))
                .usingRecursiveComparison()
                .isEqualTo(new OpenIdAuthorizationCodeResponse(response, null));

    }

    @Test
    public void shouldThrowOAuthServerExceptionWhenResponseDoesNotContainRequiredIdToken() throws Exception {
        assertThatExceptionOfType(OAuthServerException.class).isThrownBy(() -> {
            new OpenIdAuthorizationCodeTokenRequest(null, json, "request", "state", null, idTokenParser, true)
                    .deserializeTokenResponse("SIMULATED BODY");
        }).withMessage("Token response does not contain ID-token");

    }
}
