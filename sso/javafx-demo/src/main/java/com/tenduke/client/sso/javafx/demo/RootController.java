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
import java.math.BigInteger;
import java.net.URI;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javax.annotation.Nullable;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwk.JsonWebKey;

/**
 * Simple demo-controller.
 *
 */
public class RootController {

    /** 10Duke service base URL. */
    private static final String BASE_URL = "https://genco.10duke.com";

    /** JWKS "n" -part of RSA public key used to validate the ID-token. */
    // NOTE: This is not needed if the key is fetched from jwks.json.
    private static final String JWKS_PUBLIC_KEY_N = ""
            + "ywVSSuHKmyNrcT8JArxoIqTuWdCvG2R78p1Osdav8ivjQWqDnjR37tt7L-U-sopV"
            + "4ka4gUQVi7Ie87l2cJwhsJ6uAQWfp6K7r-H_yH-ak-F8EvcWLFNqRjbvgAu0MqSt"
            + "16bkZX01AanBca3yioZ-Ihe7DryKSbR1n8IMU7DRUiZzB4c9qdPphmDwxzryaiTk"
            + "E1QJyXGjpSdvwwIdXE9uXE12zSeR2-CRKWTPZsnRBKpSDdrEwE8nSRW5XmDppnpo"
            + "Avb6YI7ULtXZN354atbHsC1s-siHsjD7zB__cUzsRtge4YCTOoIs4thirizP3uXg"
            + "8xJSs1Quie1GvZt0ufwljMQnbBR7Le1ctV7sCZFom4XJGewGpnXQP9TBBpofH1Rh"
            + "jmBBRyruvbX3xGj2mKpihy6k3FzoxZ580Pv1KGo1CYjLgfXFSmwnq_MJ6bE1wR9r"
            + "mexOE1b2laWsTbTdpZB4_3mHGQ1yd5w-7ZjOQ1_K0g5FHm5yKK9cJSvQihN_BpGN"
            + "5YhvwkpjhAhJlF-csLg4DGXl5GxnTfP1ZSUywOP2Da4PzpaghsDJpkBkh6rKDK-m"
            + "J9v0He1BfvhxIqAjVnurIRZriZ6mXwTM7C9v30IBIgnadgLkyptuj-_1F3Z3m2-I"
            + "x6uLpZGUQpWVgMcC2uM5kcU6rWjrfEfAVM23axgy7c0"
            + "";

    /** JWKS "e" -part of RSA public key used to validate the ID-token. */
    // NOTE: This is not needed if the key is fetched from jwks.json.
    private static final String JWKS_PUBLIC_KEY_E = ""
            + "AQAB"
            + "";

    /** Login-client. */
    private final OpenIdAuthorizationCodeClient client;

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

        // Configure the client.
        // The values are provided to you by 10Duke
        // The values below are for working demonstration backend.
        PublicKey publicKey;

        // Fetch public key from server or use the hard coded value on error.
        try {
            final HttpsJwks jwksToken = new HttpsJwks(BASE_URL + "/.well-known/jwks.json");
            JsonWebKey firstKey = jwksToken.getJsonWebKeys().get(0);
            publicKey = (PublicKey) firstKey.getKey();
        } catch (Exception e) {
            System.out.println("Unable to fetch jwks, using hard coded values\nError:" + e.getMessage());
            publicKey = createRSAPublicKeyFromJWKS(JWKS_PUBLIC_KEY_N, JWKS_PUBLIC_KEY_E);
        }

        // Note that the client is re-usable and can be used e.g. to refresh the access-token
        this.client = new OpenIdAuthorizationCodeClient(
                new OpenIdAuthorizationCodeConfig(
                        "javafx-demo",                                      // clientId
URI.create(BASE_URL + "/user/oauth20/authz"),     // authorizationEndpoint
                        URI.create("http://127.0.0.1:49151/login"),         // redirectURI
URI.create(BASE_URL + "/user/oauth20/token"),     // tokenEndpoint
                        null,                                               // clientSecret
                        BASE_URL,                                         // issuer, used to validate the ID-token
                        publicKey,                                          // ID-token verification key
                        true                                                // use PKCE
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


    /**
     * Creates a RSA {@link PublicKey} from JWKS "n" and "e".
     *
     * @param n -
     * @param e -
     * @return -
     * @throws InvalidKeySpecException -
     * @throws NoSuchAlgorithmException -
     */
    public static PublicKey createRSAPublicKeyFromJWKS(
            final String n,
            final String e
    ) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final Base64.Decoder base64 = Base64.getUrlDecoder();
        final BigInteger modulus = new BigInteger(1, base64.decode(n));
        final BigInteger exponent = new BigInteger(1, base64.decode(e));
        final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        final KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(keySpec);
    }

}
