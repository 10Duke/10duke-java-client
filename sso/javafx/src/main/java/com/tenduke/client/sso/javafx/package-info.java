/**
 * Java-FX components for Single Sign-On login.
 *
 * <p>
 * For simplest possible use, try {@link LoginDialog}, which is a modal dialog for performing the login. If you need more control, you can
 * use {@link LoginControl}, which is internally used by {@code LoginDialog}.
 *
 * <p>
 * To start the login-flow, you need to populate a {@link com.tenduke.client.sso.LoginRequest}, which contains properly configured
 * {@link com.tenduke.client.openid.OpenIdAuthorizationCodeClient} and OAuth-scopes you want authorize.
 *
 * <p>
 * {@link com.tenduke.client.openid.OpenIdAuthorizationCodeClient} is configured with
 * {@link com.tenduke.client.openid.OpenIdAuthorizationCodeConfig}. The values for the configuration are provided to you by 10Duke.
 * The required configuration values are:
 *
 * <ol>
 *   <li>Your client id
 *   <li>Authorization endpoint
 *   <li>Redirect-URI
 *   <li>Token endpoint
 *   <li>Your client secret
 *   <li>Expected Issuer for verifying the ID-token
 *   <li>Signature verification key for verifying the ID-token
 * </ol>
 *
 * <p>
 * The {@link com.tenduke.client.sso.LoginRequest} is provided to {@link LoginDialog} or {@link LoginControl} (see the specific javadocs
 * for API and examples).
 *
 * <p>
 * NOTE: Custom schemes cannot be used for redirect URIs in vanilla JavaFX, which is why our examples use http://127.0.0.1:49151 as redirect
 * URI. The end-user *does not* need to run any service responding to those redirect URIs: The redirection URI is intercepted and processed
 * even though the navigation fails.
 *
 */
package com.tenduke.client.sso.javafx;
