/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class OAuthResponseTest {

    private OAuthResponse response;

    @Before
    public void beforeTest() {
        response = new OAuthResponse(
                "a",
                42L,
                "t"
        );
    }

    @Test
    public void shouldReturnConfiguredValues() {
        assertThat(response.getAccessToken()).isEqualTo("a");
        assertThat(response.getExpiresIn()).isEqualTo(42L);
        assertThat(response.getTokenType()).isEqualTo("t");
    }

    @Test
    public void shouldHaveToStringImplementation() {
        assertThat(response.toString()).isEqualTo("OAuthResponse{accessToken=a, expiresIn=42, tokenType=t}");
    }

    @Test
    public void shouldNoArgConstruct() {
        assertThat(new OAuthResponse())
                .hasFieldOrPropertyWithValue("accessToken", null)
                .hasFieldOrPropertyWithValue("expiresIn", -1L)
                .hasFieldOrPropertyWithValue("tokenType", null)
                .matches(r -> r.getAdditionalProperties().isEmpty());
    }

    @Test
    public void shouldSetPropertyFluently() {
        assertThat(new OAuthResponse().withAdditionalProperty("hello", "world"))
                .hasFieldOrPropertyWithValue("accessToken", null)
                .hasFieldOrPropertyWithValue("expiresIn", -1L)
                .hasFieldOrPropertyWithValue("tokenType", null)
                .matches(r -> r.getAdditionalProperties().size() == 1);
    }

}
