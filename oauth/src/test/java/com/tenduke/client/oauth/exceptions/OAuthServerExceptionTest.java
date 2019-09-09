/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.exceptions;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class OAuthServerExceptionTest {

    @Test
    public void testConstructors() {
        final IOException expectedCause = new IOException();

        assertThat(new OAuthServerException()).hasMessage(null).hasNoCause();
        assertThat(new OAuthServerException("simulated")).hasMessage("simulated").hasNoCause();
        assertThat(new OAuthServerException("simulated", expectedCause)).hasMessage("simulated").hasCause(expectedCause);
        assertThat(new OAuthServerException(expectedCause)).hasCause(expectedCause);
    }

}
