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
import com.tenduke.client.sso.LoginRequest;
import com.tenduke.client.sso.javafx.IdpBackend.Dialog;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testfx.util.WaitForAsyncUtils;

public class LoginControlTest extends BaseLoginTst {

    private LoginControl login;
    private LoginErrorEvent errorEvent;
    private LoginSuccessEvent successEvent;

    @Override
    public void start(final Stage stage) throws Exception {

        login = new LoginControl();

        login.setOnLoginError(event -> {
            errorEvent = event;
            semaphore.release();
        });
        login.setOnLoginSuccess(event -> {
            successEvent = event;
            semaphore.release();
        });

        stage.setScene(new Scene(login));
        stage.show();
    }

    @Before
    public void beforeTest() throws InterruptedException {
        errorEvent = null;
        successEvent = null;
    }

    @Test
    public void shouldEmitLoginSuccessEventWithSuccessfulLogin() throws InterruptedException, IOException {

        backend.dialog(Dialog.SUCCESS);

        final LoginRequest request = createRequest();

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.start(request);
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(successEvent).isNotNull().extracting(event -> event.getResponse())
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

        backend.verify(Dialog.SUCCESS);

        assertThat(login.getRequest()).isSameAs(request);
    }

    @Test
    public void shouldEmitLoginErrorEventIfInvalidURL() throws InterruptedException, IOException {

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest("http://no-such-host-should-ever-exist.no-such-domain:99999/"));
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.SERVER_ERROR);
        assertThat(errorEvent.getMessage()).contains("Server not found");
    }

    @Test
    public void shouldEmitLoginErrorEventIfServerNotFound() throws InterruptedException, IOException {

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest("http://no-such-host-should-ever-exist.no-such-domain:49151/"));
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.SERVER_ERROR);
        assertThat(errorEvent.getMessage()).contains("Invalid url, host not found or network failure:");
    }

    @Test
    public void shouldEmitLoginErrorEventIfThreadInterrupted() throws InterruptedException, IOException {

        final HttpClient http = mock(HttpClient.class);

        when(http.send(any(), any())).thenThrow(new InterruptedException());

        backend.dialog(Dialog.SUCCESS);

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest(backend.url(), http));
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.INTERRUPTED);
    }

    @Test
    public void shouldEmitLoginErrorEventIfErrorResponseAtAuthorization() throws InterruptedException, IOException {

        backend.dialog(Dialog.ERROR_AUTHZ);

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest());
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.ERROR);
        assertThat(errorEvent.getCause())
                .isInstanceOf(OAuthErrorException.class)
                .extracting(c -> ((OAuthErrorException)c).getError())
                .isEqualToComparingFieldByField(new OAuthErrorResponse(
                        "simulated-error",
                        "simulated-description",
                        null
                ));
    }

    @Test
    public void shouldEmitLoginErrorEventIfNetworkFailureAtTokenFetching() throws InterruptedException, IOException {

        final HttpClient http = mock(HttpClient.class);

        when(http.send(any(), any())).thenThrow(new IOException());

        backend.dialog(Dialog.SUCCESS);

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest(backend.url(), http));
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.NETWORK_ERROR);
    }

    @Test
    public void shouldEmitLoginErrorEventIfSecurityException() throws InterruptedException, IOException {

        backend.dialog(Dialog.INVALID_STATE);

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest());
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.SECURITY_ERROR);
    }

    @Test
    public void shouldEmitLoginErrorEventIfServerException() throws InterruptedException, IOException {

        backend.dialog(Dialog.INVALID_REDIRECT_URI);

        WaitForAsyncUtils.waitForAsyncFx(5000, () -> {
            login.setRequest(createRequest());
        });

        semaphore.tryAcquire(15, TimeUnit.SECONDS);

        assertThat(errorEvent).isNotNull();
        assertThat(errorEvent.getEventType()).isEqualTo(LoginErrorEvent.SERVER_ERROR);
    }

    @Test
    public void shouldReturnOnLoginErrorHandler() {
        assertThat(login.getOnLoginError()).isNotNull();
    }

    @Test
    public void shouldReturnOnLoginSuccessHandler() {
        assertThat(login.getOnLoginSuccess()).isNotNull();
    }

    @Test
    public void testOnLoginErrorProperty() {
        assertThat(login.onLoginErrorProperty().getName()).isEqualTo("onLoginError");
        assertThat(login.onLoginErrorProperty().getBean()).isSameAs(login);
    }

    @Test
    public void testOnLoginSuccessProperty() {
        assertThat(login.onLoginSuccessProperty().getName()).isEqualTo("onLoginSuccess");
        assertThat(login.onLoginSuccessProperty().getBean()).isSameAs(login);
    }

    @Test
    public void getMessageShouldReturnMessageFromThrowable() {
        assertThat(LoginControl.getMessage(new IOException("simulated"))).isEqualTo("simulated");
    }

    @Test
    public void getMessageShouldReturnNullIfThrowableIsNull() {
        assertThat(LoginControl.getMessage(null)).isNull();
    }

}
