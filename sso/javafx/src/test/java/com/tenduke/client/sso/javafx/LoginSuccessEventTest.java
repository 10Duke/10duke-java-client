/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.openid.OpenIdAuthorizationCodeResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class LoginSuccessEventTest {

    @Test
    public void shouldConstruct() {
        final OpenIdAuthorizationCodeResponse response = new OpenIdAuthorizationCodeResponse("a", "r", 42, "t", null);

        assertThat(new LoginSuccessEvent(null, null, response).getResponse()).isSameAs(response);
    }

}
