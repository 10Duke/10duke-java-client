/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso;

import com.tenduke.client.openid.OpenIdAuthorizationCodeClient;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class LoginRequestTest {

    private OpenIdAuthorizationCodeClient client;

    @Before
    public void beforeTest() {
        client = mock(OpenIdAuthorizationCodeClient.class);
    }

    @Test
    public void shouldConstructWithScopes() {
        final LoginRequest request = new LoginRequest(client, "a", "b", "c");

        assertThat(request.getClient()).isSameAs(client);
        assertThat(request.getParameters()).isEmpty();
        assertThat(request.getScopes()).containsExactly("a", "b", "c");
    }

}
