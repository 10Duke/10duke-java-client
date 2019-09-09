/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.oauth.exceptions.OAuthException;
import static com.tenduke.client.sso.javafx.LoginErrorEvent.ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class LoginErrorEventTest {

    @Test
    public void shouldConstructWithessage() {
        final LoginErrorEvent event = new LoginErrorEvent(null, null, ERROR, "simulated error");

        assertThat(event.getCause()).isNull();
        assertThat(event.getEventType()).isEqualTo(ERROR);
        assertThat(event.getMessage()).isEqualTo("simulated error");
    }

    @Test
    public void shouldConstructWithCause() {
        final OAuthException cause = new OAuthException("simulated exception");
        final LoginErrorEvent event = new LoginErrorEvent(null, null, ERROR, cause);

        assertThat(event.getCause()).isSameAs(cause);
        assertThat(event.getEventType()).isEqualTo(ERROR);
        assertThat(event.getMessage()).isEqualTo("simulated exception");
    }

}
