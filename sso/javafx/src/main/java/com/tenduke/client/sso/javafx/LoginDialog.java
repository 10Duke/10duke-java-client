/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.openid.OpenIdAuthorizationCodeResponse;
import com.tenduke.client.sso.LoginRequest;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.annotation.Nullable;

/**
 * Modal dialog to perform login with OpenID Connect.
 *
 * <p>
 * Calling {@link #show(com.tenduke.client.sso.LoginRequest) } opens a modal dialog, which initiates the login flow. The call is blocking
 * and returns {@link OpenIdAuthorizationCodeResponse} on successful login. Login errors are reported via {@link LoginException}.
 *
 * If a modal dialog does not suit your needs, you may want to use {@link LoginControl}, which is used by {@code LoginDialog}.
 *
 * <p>
 * Example:
 *
 * <pre>
 *   final Stage parentStage = ...; // Stage on which to show the dialog
 *   final Key signatureVerificationKey = ...; // Used to verify ID-token signature. Provided to you by 10Duke.
 *
 *   // Configuration: Following are fake values for demonstrating the usage.
 *   // Actual values are provided to you by 10Duke.
 *   final OpenIdAuthorizationCodeConfig config = new OpenIdAuthorizationCodeConfig(
 *           "javafx",                                        // your client id
 *           URI.create("https://example.com/oauth2/authz"),  // OAuth-authorization endpoint
 *           URI.create("http://127.0.0.1:49151/login"),      // re-direct URI
 *           URI.create("https://example.com/oauth2/access"), // authorization code token endpoint
 *           "client secret",                                 // your client secret
 *           "expected issuer",                               // expected issuer, used to validate ID-token
 *           signatureVerificationKey                         // signature verification key, see above.
 *   );
 *
 *   final OpenIdAuthorizationCodeClient client = new OpenIdAuthorizationCodeClient(config);
 *
 *   // Login request contains configured client and requested scopes.
 *   // OpenIdAuthorizationCodeClient automatically adds the "openid" -scope
 *   final LoginRequest request = new LoginRequest(
 *           client,
 *           "email",                                                   // a scope
 *           "profile",                                                 // another scope
 *           "https://apis.10duke.com/auth/openidconnect/organization"  // another scope
 *   );
 *
 *   try {
 *       // Open the dialog and wait for the login to complete:
 *       final OpenIdAuthorizationCodeResponse response = new LoginDialog(rootStage)
 *               .show(request);
 *
 *       // Null response means that the dialog was dismissed
 *       if (response == null) {
 *           System.out.println("Login canceled");
 *       }
 *       // Otherwise, the login succeeds
 *       else {
 *           System.out.println("Login successful, token: " + response);
 *       }
 *   }
 *   // Errors are reported with exceptions
 *   catch (final LoginException e) {
 *       System.out.println("Login failed: " + e.getEvent().getCause());
 *   }
 * </pre>
 *
 */
public class LoginDialog {

    /** Parent stage. */
    private final Stage stage;

    /** Scene. */
    private final Scene scene;

    /** Login control. */
    private final LoginControl login;

    /** Success result. */
    private @Nullable OpenIdAuthorizationCodeResponse result;

    /** Error result. */
    private @Nullable LoginErrorEvent error;

    /**
     * Constructs new instance.
     *
     * @param parent -
     */
    public LoginDialog(final Stage parent) {
        login = new LoginControl();
        login.setOnLoginError(event -> handleLoginError(event));
        login.setOnLoginSuccess(event -> handleLoginSuccess(event));

        stage = new Stage();

        stage.initOwner(parent);
        stage.initModality(Modality.APPLICATION_MODAL);

        scene = new Scene(login);
        stage.setScene(scene);
    }

    /**
     * Shows the login dialog, starting the login flow.
     *
     * <p>
     * The dialog is modal, so this is blocking action and will return after successful login, cancellation or error.
     *
     * @param request configured login request
     * @return OpenID Connect response after successful login. Returns {@code null}, if login is cancelled (e.g. dialog dismissed).
     * @throws LoginException on login errors
     */
    public @Nullable OpenIdAuthorizationCodeResponse show(final LoginRequest request) throws LoginException {
        login.setRequest(request);
        stage.showAndWait();

        if (error != null) {
            throw new LoginException(error);
        }

        return result;
    }

    /**
     * Handles login success.
     *
     * @param event -
     */
    protected void handleLoginSuccess(final LoginSuccessEvent event) {
        result = event.getResponse();
        stage.close();
    }

    /**
     * Handles login error.
     *
     * @param event -
     */
    protected void handleLoginError(final LoginErrorEvent event) {
        error = event;
        stage.close();
    }

}
