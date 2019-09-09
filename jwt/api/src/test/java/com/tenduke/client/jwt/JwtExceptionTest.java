/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class JwtExceptionTest {

    @Test
    public void testConstructors() {
        final IOException expectedCause = new IOException("simulated cause");

        assertThat(new JwtException()).hasMessage(null).hasNoCause();
        assertThat(new JwtException("simulated")).hasMessage("simulated").hasNoCause();
        assertThat(new JwtException("simulated", expectedCause)).hasMessage("simulated").hasCause(expectedCause);
        assertThat(new JwtException(expectedCause)).hasMessage("java.io.IOException: simulated cause").hasCause(expectedCause);
    }

}
