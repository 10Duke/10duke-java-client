/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx.demo;

import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.openid.OpenIdAuthorizationCodeClient;
import com.tenduke.client.openid.OpenIdAuthorizationCodeConfig;
import com.tenduke.client.openid.OpenIdAuthorizationCodeResponse;
import com.tenduke.client.sso.LoginRequest;
import com.tenduke.client.sso.javafx.LoginDialog;
import com.tenduke.client.sso.javafx.LoginException;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.net.URI;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Simple demo-controller.
 *
 */
public class RootController {

    /** Login-client. */
    private final OpenIdAuthorizationCodeClient client;

    /** Public key used to validate the ID-token. Provided by 10Duke. */
    private static final String PUBLIC_KEY = ""
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3oMwV7MpnuEpdDSsW8Fy"
            + "NXQescT48dgcORAU1LFcZr2XpkpS7oCtlzOmFstC0DVtKjJKxUurufGQ4/0XFNA2"
            + "InbGk66WL1ir6fAFC04XisiMVgYqrP4AypqE0jIcfcwrJRzKHPkrtrOib4Da7do1"
            + "nQrsK8p9iXpWMqRDnPpn1fJ9mwxP74jtb7A/4FlrbMZKtud8dK5P6GPxyDUTMDal"
            + "n/dqQ46uuc4FVVn+Kt+/dZpN2uicy3v9SOhS3oVQDlPyrecYuQ8kblwijFVvIC8A"
            + "RVef4ppDil5WGO1rkF6CUYOYL3Z8CWLYuW7uPLDMmPMWPBdobATgEn5+DpkPO5MW"
            + "+QIDAQAB"
            + "";

    /** Current OpenId Connect response, containing e.g.&nbps;access token and refresh token. */
    private @Nullable OpenIdAuthorizationCodeResponse response;

    /** Property for toggling "Refresh"-button state. */
    private final BooleanProperty refreshDisabled = new SimpleBooleanProperty(true);

    // <editor-fold defaultstate="collapsed" desc="Properties">
    // CSOFF: JavadocMethod

    public BooleanProperty refreshDisabledProperty() {
        return refreshDisabled;
    }

    public void setRefreshDisabled(final boolean refreshDisabled) {
        refreshDisabledProperty().set(refreshDisabled);
    }

    public boolean isRefreshDisabled() {
        return refreshDisabledProperty().get();
    }

    // CSON: JavadocMethod
    // </editor-fold>

    /**
     * Configures the parameters.
     *
     * <p>
     * Normally, configuration is environment specific and should be loaded dynamically (and e.g. dependency injected).
     * For this demo, the configuration is hardcoded here.
     *
     * @throws InvalidKeySpecException -
     * @throws NoSuchAlgorithmException -
     */
    public RootController() throws InvalidKeySpecException, NoSuchAlgorithmException {

        // Build the signature verification key:
        final byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);

        final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        final RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(spec);

        // Configure the client.
        // The values are provided to you by 10Duke
        // The values below are for working demonstration backend.

        // Note that the client is re-usable and can be used e.g. to refresh the access-token
        this.client = new OpenIdAuthorizationCodeClient(
                new OpenIdAuthorizationCodeConfig(
                        "javafx-demo",                                          // clientId
                        URI.create("https://genco.10duke.com/oauth2/authz"),    // authorizationEndpoint
                        URI.create("http://127.0.0.1:49151/login"),             // redirectURI
                        URI.create("https://genco.10duke.com/oauth2/access"),   // tokenEndpoint
                        "open-sesame",                                          // clientSecret
                        "https://genco.10duke.com",                             // issuer, used to validate the ID-token
                        publicKey                                               // ID-token verification key
                )
        );
    }

    /**
     * Shows the login dialog.
     * Activated by clicking the login-button.
     *
     */
    public void login() {

        setRefreshDisabled(true);

        try {
            // Open the login dialog
            response = new LoginDialog(App.getRootStage()).show(new LoginRequest(
                        client,
                        "https://apis.10duke.com/auth/openidconnect/organization",
                        "profile",
                        "email"
            ));

            if (response == null) {
                System.out.println("Login canceled");
            } else {
                // We have successful response, containing the access-token, refresh-token and the ID-token.
                // Normally the response is stored for further use, but here we just dump it to console
                System.out.println("Login successful, token: " + response);

                setRefreshDisabled(false);
            }
        } catch (final LoginException e) {
            System.out.println("Login failed: " + e.getEvent().getCause());
        }
    }

    /**
     * Refreshes the access token with refresh token.
     * Activated by clicking "Refresh-button".
     *
     */
    public void refresh() {
        if (response == null) {
            System.out.println("No token to refresh.");
            return;
        }

        setRefreshDisabled(true);

        try {
            response = client.refresh(response.getRefreshToken());

            System.out.println("Token refreshed: " + response);

            setRefreshDisabled(false);
        } catch (final InterruptedException | OAuthException e) {
            System.out.println("Refresh failed: " + e.getMessage());
        }
    }

}
