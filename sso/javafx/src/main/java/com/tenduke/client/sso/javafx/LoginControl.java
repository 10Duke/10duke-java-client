/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.oauth.exceptions.OAuthErrorException;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.oauth.exceptions.OAuthNetworkException;
import com.tenduke.client.oauth.exceptions.OAuthSecurityException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import com.tenduke.client.openid.OpenIdAuthorizationCodeFlow;
import com.tenduke.client.openid.OpenIdAuthorizationCodeResponse;
import com.tenduke.client.sso.LoginRequest;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX-control for OpenIdConnect login.
 *
 * <p>
 * This control wraps an embedded browser and starts login flow, when {@link #requestProperty() } changes. Login success or failure (there
 * are several possible error conditions) are communicated via events. Caller should register listeners using {@link #onLoginErrorProperty()
 * } and {@link #onLoginSuccessProperty() }.
 *
 * <p>
 * To start the login flow, configure {@link LoginRequest}: OAuth {@code state} and OpenID Connect {@code nonce} are automatically
 * generated. If you want to provide custom values for state and nonce, pass them as parameters when constructing {@link LoginRequest}, with
 * names {@code state} and {@code nonce}. Then call {@link #start(com.tenduke.client.sso.LoginRequest) } with the request.
 *
 * <p>
 * The login flow is actually started by changing {@link #requestProperty()} and {@code start()} delegates the call to
 * {@link #setRequest(com.tenduke.client.sso.LoginRequest) }. This way you can bind controller to the request property in FXML: Changing
 * the property in controller triggers change of the request-property and starts the login-flow.
 *
 * <p>
 * On login success, a {@link LoginSuccessEvent} is emitted. This event contains {@link OpenIdAuthorizationCodeResponse} as payload,
 * containing all necessary information.
 *
 * <p>
 * On login failure, a {@link LoginErrorEvent} is emitted. The events are sub-typed according to the error type. Error events usually
 * (depending on the error condition) contain the exception, which caused the login failure. Note that causes of {@link OAuthErrorException}
 * contain additional information as returned by the OAuth server.
 *
 * <p>
 * Example of using the control from code:
 *
 * <pre>
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
 *           signatureVerificationKey                         // signature verification key, see above
 *   );
 *
 *   final OpenIdAuthorizationCodeClient = new OpenIdAuthorizationCodeClient(config);
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
 *   // Instantiate the LoginControl:
 *   final Login login = new LoginControl();
 *   login.setOnLoginError(event -&gt; {
 *       //... do something with the LoginErrorEvent
 *   });
 *   login.setOnLoginSuccess(event -&gt; {
 *       //... do something with the LoginSuccessEvent
 *   });
 *
 *   final Stage stage ...; // Some JavaFX-stage
 *
 *   // Set the login-control to some JavaFX-stage
 *   // We could add the login control to some container depending on how we want to integrate it to the UI
 *   stage.setScene(new Scene(login));
 *
 *   // Starts the login
 *   login.setRequest(request);
 *
 * </pre>
 *
 */
public class LoginControl extends Control {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(LoginControl.class);

    // <editor-fold defaultstate="collapsed" desc="Fields">

    /** The wrapped {@link WebView}. */
    private final WebView view;

    /** WebEngine.  */
    private final WebEngine engine;

    /** Property for the login-request. Change of this property initiates new login-flow. */
    private final ObjectProperty<LoginRequest> request = new SimpleObjectProperty<>();

    /** Property for emitting {@link LoginSuccessEvent. */
    private final ObjectProperty<EventHandler<LoginSuccessEvent>> onLoginSuccess
            = new ObjectPropertyBase<EventHandler<LoginSuccessEvent>>() {

        @Override
        protected void invalidated() {
            setEventHandler(LoginSuccessEvent.LOGIN_SUCCESS, get());
        }

        @Override
        public Object getBean() {
            return LoginControl.this;
        }

        @Override
        public String getName() {
            return "onLoginSuccess";
        }
    };

    /** Property for emitting {@link LoginErrorEvent. */
    private final ObjectProperty<EventHandler<LoginErrorEvent>> onLoginError = new ObjectPropertyBase<EventHandler<LoginErrorEvent>>() {

        @Override
        protected void invalidated() {
            setEventHandler(LoginErrorEvent.LOGIN_ERROR_ALL, get());
        }

        @Override
        public Object getBean() {
            return LoginControl.this;
        }

        @Override
        public String getName() {
            return "onLoginError";
        }
    };

    /** Currently active login flow. */
    private OpenIdAuthorizationCodeFlow currentFlow;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">

    /**
     * Constructs new instance.
     *
     */
    public LoginControl() {
        super();

        this.view = new WebView();
        this.engine = this.view.getEngine();

        this.view.setFontSmoothingType(FontSmoothingType.GRAY);

        engine.getLoadWorker().stateProperty().addListener(new StateListener());

        // Register listener for the LoginRequest. When the property changes, start new login-flow.
        // NOTE: The WebEngine in OpenJFX does not understand custom schemes. When redirecting to URL with custom scheme, the redirection
        // event is never emitted, instead the the code automatically pops to LOADING_FAILED with error code indicating MalformedURL.
        // In that branch the URL is lost (it is unknown if the uri-parameter contains the redirect uri). This is why we need to use
        // real URL and listen to that.
        request.addListener(new ChangeListener<LoginRequest>() {

            @Override
            public void changed(
                    final ObservableValue<? extends LoginRequest> observableValue,
                    final LoginRequest oldValue,
                    final LoginRequest newValue
            ) {
                startLoginFlow(newValue);
            }
        });
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Properties">
    // CSOFF: JavadocMethod

    public final EventHandler<LoginErrorEvent> getOnLoginError() {
        return onLoginErrorProperty().get();
    }

    public final EventHandler<LoginSuccessEvent> getOnLoginSuccess() {
        return onLoginSuccessProperty().get();
    }

    public LoginRequest getRequest() {
        return request.get();
    }


    public final void setOnLoginError(final EventHandler<LoginErrorEvent> value) {
        onLoginErrorProperty().set(value);
    }

    public final void setOnLoginSuccess(final EventHandler<LoginSuccessEvent> value) {
        onLoginSuccessProperty().set(value);
    }

    public void setRequest(final LoginRequest request) {
        requestProperty().set(request);
    }

    public final ObjectProperty<EventHandler<LoginErrorEvent>> onLoginErrorProperty() {
        return onLoginError;
    }

    public final ObjectProperty<EventHandler<LoginSuccessEvent>> onLoginSuccessProperty() {
        return onLoginSuccess;
    }

    public ObjectProperty<LoginRequest> requestProperty() {
        return this.request;
    }

    // CSON: JavadocMethod
    // </editor-fold>

    /**
     * Starts the login-flow with given request.
     *
     * This internally calls {@link #setRequest(com.tenduke.client.sso.LoginRequest) }.
     *
     * @param request -
     */
    public void start(final LoginRequest request) {
        setRequest(request);
    }

    /**
     * Internal implementation for starting the login flow.
     *
     * @param request -
     */
    protected void startLoginFlow(final LoginRequest request) {

        currentFlow = request.getClient().request()
                .scopes(request.getScopes())
                .parameters(request.getParameters())
                .start();

        LOG.info("[state={}] Starting login flow...", currentFlow.state());

        engine.load(currentFlow.getRequest().toUrl().toString());
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new LoginControlSkin(this, view);
    }

    /**
     * Custom state-listener, which observes the page-loading state changes and reacts when url-change fails.
     *
     */
    protected class StateListener implements ChangeListener<Worker.State> {

        /**
         * {@inheritDoc}
         *
         */
        @Override
        public void changed(
                final ObservableValue<? extends Worker.State> ov,
                final Worker.State oldState,
                final Worker.State newState
        ) {
            LOG.trace("[old={}][new={}][url={}] Browser state changed", oldState, newState, engine.getLocation());

            // New URL is redirect-URI and is handled
            if (handleUrlChange()) {
                return;
            }

            // URL is not redirect-URI and either the navigation or redirection failed: Emit an error.
            if (newState == Worker.State.FAILED) {
                @Nullable final String message = engine.getLoadWorker().getMessage();
                @Nullable final Throwable throwable = engine.getLoadWorker().getException();
                @Nullable final String exceptionMessage = getMessage(throwable);

                LOG.error(
                        "[state={}] Invalid url, host not found or network failure: {}: {} ",
                        currentFlow.state(),
                        message,
                        exceptionMessage
                );

                fireEvent(new LoginErrorEvent(
                        LoginControl.this,
                        LoginControl.this,
                        LoginErrorEvent.SERVER_ERROR,
                        "Invalid url, host not found or network failure: " + message + ": " + exceptionMessage
                ));
            }
        }

        /**
         * Called when URL has changed in the {@link WebEngine}, so that we need to take an action, i.e.&nbsp;check if callback URI has been
         * called or if navigation has failed.
         *
         * <p>
         * If the URI is a callback URI, then this method determines whether login is successful, and if so, fires
         * {@link LoginSuccessEvent}. If the URI is null or an error callback, {@link LoginErrorEvent} is fired.
         *
         * <p>
         * @return {@code true} if the new URL is a redirect URI (either success or error), {@code false} otherwise
         */
        protected boolean handleUrlChange() {
            try {
                @Nullable final String url = engine.getLocation();

                if (url == null) {
                    throw new UrlNotFoundException("Embedded browser returned null URL");
                }

                @Nullable final OpenIdAuthorizationCodeResponse result = currentFlow.process(url);

                if (result == null) {
                    return false;
                }

                fireEvent(new LoginSuccessEvent(LoginControl.this, LoginControl.this, result));
            } catch (final InterruptedException e) {
                fireEvent(new LoginErrorEvent(LoginControl.this, LoginControl.this, LoginErrorEvent.INTERRUPTED, "Interrupted"));
            } catch (final OAuthErrorException e) {
                fireEvent(new LoginErrorEvent(LoginControl.this, LoginControl.this, LoginErrorEvent.ERROR, e));
            } catch (final OAuthNetworkException e) {
                fireEvent(new LoginErrorEvent(LoginControl.this, LoginControl.this, LoginErrorEvent.NETWORK_ERROR, e));
            } catch (final OAuthSecurityException e) {
                fireEvent(new LoginErrorEvent(LoginControl.this, LoginControl.this, LoginErrorEvent.SECURITY_ERROR, e));
            } catch (final OAuthServerException e) {
                fireEvent(new LoginErrorEvent(LoginControl.this, LoginControl.this, LoginErrorEvent.SERVER_ERROR, e));
            } catch (final OAuthException e) {
                fireEvent(new LoginErrorEvent(LoginControl.this, LoginControl.this, LoginErrorEvent.ERROR, e));
            } catch (final UrlNotFoundException e) {
                LOG.error("[state={}] Server not found or network failure: {}", currentFlow.state(), e.getMessage());

                fireEvent(new LoginErrorEvent(
                        LoginControl.this,
                        LoginControl.this,
                        LoginErrorEvent.SERVER_ERROR,
                        "Server not found or network failure: " + e.getMessage()
                ));
            }

            return true;
        }
    }

    /**
     * Returns message from possibly {@code null} {@code Throwable}.
     *
     * @param throwable -
     * @return returns possibly {@code null} message from throwable. Returns {@code null}, if throwable is {@code null}.
     */
    protected static @Nullable String getMessage(@Nullable final Throwable throwable) {
        return (throwable == null ? null : throwable.getMessage());
    }

    /**
     * Internal exception.
     *
     */
    private static class UrlNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Constructs new instance.
         *
         * @param message -
         */
        UrlNotFoundException(final String message) {
            super(message);
        }
    }

}
