/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

import com.tenduke.client.oauth.OAuthErrorResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class OAuthErrorExceptionTest {

    @Test
    public void testConstructors() {
        final OAuthErrorResponse error = new OAuthErrorResponse(null, null, null);
        final OAuthErrorException exception = new OAuthErrorException("simulated", error);

        assertThat(exception).hasMessage("simulated");
        assertThat(exception.getError()).isSameAs(error);
    }

}
