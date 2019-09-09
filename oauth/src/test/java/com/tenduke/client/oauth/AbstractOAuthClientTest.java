/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class AbstractOAuthClientTest {

    @Test
    public void shouldReturnTheConfiguration() {
        final OAuthConfig config = new OAuthConfig(null, null, null);

        assertThat(new Impl(config).getConfig()).isSameAs(config);
    }

    // <editor-fold defaultstate="collapsed" desc="Class implementation">

    private static class Impl extends AbstractOAuthClient<OAuthConfig> {

        public Impl(final OAuthConfig config) {
            super(config);
        }

        @Override
        public OAuthFlowBuilder request() {
            return null;
        }

    }

    // </editor-fold>

}
