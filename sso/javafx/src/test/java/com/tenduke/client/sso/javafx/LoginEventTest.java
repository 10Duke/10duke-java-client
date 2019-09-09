/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import static com.tenduke.client.sso.javafx.LoginErrorEvent.ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class LoginEventTest {

    @Test
    public void shouldConstructWithType() {
        final LoginEvent event = new LoginEvent(ERROR);

        assertThat(event.getEventType()).isEqualTo(ERROR);
    }

}
