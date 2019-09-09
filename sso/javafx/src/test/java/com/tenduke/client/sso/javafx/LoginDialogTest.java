/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.oauth.OAuthErrorResponse;
import com.tenduke.client.oauth.exceptions.OAuthErrorException;
import com.tenduke.client.openid.IdToken;
import com.tenduke.client.openid.OpenIdAuthorizationCodeResponse;
import com.tenduke.client.sso.javafx.IdpBackend.Dialog;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.Test;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

public class LoginDialogTest extends BaseLoginTst {

    private Stage stage;

    @Override
    public void start(final Stage stage) throws Exception {

        stage.setScene(new Scene(new FlowPane(new Label("Unit test in progress"))));
        stage.show();
    }

    @Test
    public void shouldReturnResponseAfterSuccessfulLogin() throws IOException {

        backend.dialog(Dialog.SUCCESS);

        final OpenIdAuthorizationCodeResponse actual = waitForAsyncFx(5000, new Callable<OpenIdAuthorizationCodeResponse>() {
            @Override
            public OpenIdAuthorizationCodeResponse call() throws Exception {
                return new LoginDialog(stage).show(createRequest());
            }
        });

        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new OpenIdAuthorizationCodeResponse(
                        "a",
                        "r",
                        42,
                        "t",
                        new IdToken(
                                "is-sue-r",
                                "sub-ject",
                                List.of("unit-test"),
                                Instant.parse("1970-01-01T00:00:03Z"),
                                Instant.parse("1970-01-01T00:00:02Z"),
                                Instant.parse("1970-01-01T00:00:01Z"),
                                "non-sense",
                                null,
                                List.of(),
                                null
                        )
                ).withAdditionalProperty("id_token", "SIMULATED ID TOKEN")
                );
    }

    //@Test
    public void shouldReturnNullIfClosed() {
        // TBD
    }

    @Test
    public void shouldThrowLoginExceptionWrappingErrorEvent() throws InterruptedException, IOException {
        backend.dialog(Dialog.ERROR_AUTHZ);

        final LoginErrorEvent actual = waitForAsyncFx(5000, new Callable<LoginErrorEvent>() {
            @Override
            public LoginErrorEvent call() {
                try {
                    new LoginDialog(stage).show(createRequest());
                    fail("Should have thrown LoginException");
                } catch (final LoginException e) {
                    return e.getEvent();
                }
                return null;
            }
        });

        assertThat(actual).isNotNull();
        assertThat(actual.getEventType()).isEqualTo(LoginErrorEvent.ERROR);
        assertThat(actual.getCause())
                .isInstanceOf(OAuthErrorException.class)
                .extracting(c -> ((OAuthErrorException)c).getError())
                .isEqualToComparingFieldByField(new OAuthErrorResponse(
                        "simulated-error",
                        "simulated-description",
                        null
                ));
    }
}
