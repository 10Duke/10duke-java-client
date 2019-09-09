/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class IdTokenExceptionTest {

    @Test
    public void testConstructors() {
        final IOException expectedCause = new IOException();

        assertThat(new IdTokenException()).hasMessage(null).hasNoCause();
        assertThat(new IdTokenException("simulated")).hasMessage("simulated").hasNoCause();
        assertThat(new IdTokenException("simulated", expectedCause)).hasMessage("simulated").hasCause(expectedCause);
    }

}
