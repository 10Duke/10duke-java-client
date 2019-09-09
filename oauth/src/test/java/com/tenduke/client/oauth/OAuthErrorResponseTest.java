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

public class OAuthErrorResponseTest {

    @Test
    public void shouldReturnConfiguredValues() {
        final OAuthErrorResponse response = new OAuthErrorResponse("a", "b", "c");

        assertThat(response.getError()).isEqualTo("a");
        assertThat(response.getErrorDescription()).isEqualTo("b");
        assertThat(response.getErrorUri()).isEqualTo("c");
    }

    @Test
    public void shouldNoArgConstrut() {
        assertThat(new OAuthErrorResponse()).hasAllNullFieldsOrProperties();
    }

}
