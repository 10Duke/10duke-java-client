/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class AuthorizationCodeResponseTest {

    @Test
    public void testToString() {
        assertThat(new AuthorizationCodeResponse("a", "r", 42, "b")).hasToString("AuthorizationCodeResponse{accessToken=a, refreshToken=r, expiresIn=42, tokenType=b}");
    }

    @Test
    public void shouldNoArgConstruct() {
        assertThat(new AuthorizationCodeResponse())
                .hasFieldOrPropertyWithValue("accessToken", null)
                .hasFieldOrPropertyWithValue("refreshToken", null)
                .hasFieldOrPropertyWithValue("expiresIn", -1L)
                .hasFieldOrPropertyWithValue("tokenType", null);

    }

}
