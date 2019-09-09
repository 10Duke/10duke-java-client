/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class LoginExceptionTest {

   @Test
    public void testConstructor() {
        final LoginErrorEvent event = new LoginErrorEvent(null, null, null, "simulated cause");

        assertThat(new LoginException(event).getEvent()).isSameAs(event);
    }
}
