/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class AbstractTokenRequestFactoryTest {

    private Impl factory;

    @Before
    public void beforeTest() {
        factory = new Impl(new AuthorizationCodeConfig(
                "unit-test",
                null,
                URI.create("tenduke://callback/login"),
                URI.create("https://example.com/token"),
                "1-2-3-4-5")
        );
    }

    @Test
    public void shouldCreateTokenRequest() {
        factory.create("asdf", "alaska");
        assertThat(factory.requestBody).isEqualTo("grant_type=authorization_code&code=asdf&redirect_uri=tenduke%3A%2F%2Fcallback%2Flogin&client_id=unit-test&client_secret=1-2-3-4-5");
    }

    @Test
    public void shouldCreateRefreshTokenRequest() {
        factory.refresh("asdf", "alaska");
        assertThat(factory.requestBody).isEqualTo("grant_type=refresh_token&refresh_token=asdf&client_id=unit-test&client_secret=1-2-3-4-5");
    }

    private static class Impl extends AbstractTokenRequestFactory<AuthorizationCodeResponse, AuthorizationCodeTokenRequest> {

        private String requestBody;

        public Impl(final AuthorizationCodeConfig config) {
            super(config);
        }

        @Override
        protected AuthorizationCodeTokenRequest createRequest(final String requestBody, final String state, final URI tokenEndpoint) {
            this.requestBody = requestBody;
            return new AuthorizationCodeTokenRequest(null, null, requestBody, state, tokenEndpoint);
        }
    }

}
