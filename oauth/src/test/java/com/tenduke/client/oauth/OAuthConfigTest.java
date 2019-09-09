/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void shouldReturnConfiguredValues() {
        final OAuthConfig config = new OAuthConfig(
                "unit-test",
                URI.create("https://example.com/oauth"),
                URI.create("tenduke://callback")
        );

        assertThat(config.getAuthorizationEndpoint()).isEqualTo(URI.create("https://example.com/oauth"));
        assertThat(config.getClientId()).isEqualTo("unit-test");
        assertThat(config.getRedirectUri()).isEqualTo(URI.create("tenduke://callback"));
    }

}
