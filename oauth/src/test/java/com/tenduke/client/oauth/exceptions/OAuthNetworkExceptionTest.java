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

public class OAuthNetworkExceptionTest {

    @Test
    public void testConstructors() {
        final IOException expectedCause = new IOException();

        assertThat(new OAuthNetworkException("simulated", expectedCause)).hasMessage("simulated").hasCause(expectedCause);
    }

}
