# 10Duke JavaFX-Client For Single Sign-On 

This artifact contains JavaFX-client to do Single Sign-On. The client contains a modal login-dialog and a 
JavaFX-control. For further details on those, see below.


## Requirements

The client is written in JDK 11 and is tested on OpenJFX version 11.0.2. It may work with other versions of 
OpenJFX, but is not tested.


## Dependencies

To use the client, declare following dependencies:

```xml
    <dependency>
        <groupId>com.10duke.client</groupId>
        <artifactId>openid</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.10duke.client.sso</groupId>
        <artifactId>javafx</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

You need also implementation of JSON-deserializer and JWT-parser. These are separate artifacts so that
developers can choose their preferred implementations. We currently support Jackson as JSON-seder and
io.jsonwebtoken as JWT-processor (but more may be added in the future):

```xml
    <dependency>
        <groupId>com.10duke.client.json</groupId>
        <artifactId>jackson</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.10duke.client.jwt</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

## Using the client


### Configuring the client

The client is configured with class `OpenIdAuthorizationCodeConfig`. The values for the configuration are
provided to you by 10Duke.

NOTE: Custom schemes cannot be used for redirect URIs in vanilla JavaFX, which is why our examples use
http://127.0.0.1:49151 as redirect URI. The end-user *does not* need to run any service responding to those
redirect URIs: The redirection URI is intercepted and processed even though the navigation fails.

Example:

```java
    // Configuration: Following are fake values for demonstrating the usage.
    // Actual values are provided to you by 10Duke.
    final OpenIdAuthorizationCodeConfig config = new OpenIdAuthorizationCodeConfig(
        "javafx",                                        // your client id
        URI.create("https://example.com/oauth2/authz"),  // OAuth-authorization endpoint
        URI.create("http://127.0.0.1:49151/login"),      // re-direct URI
        URI.create("https://example.com/oauth2/access"), // authorization code token endpoint
        "client secret",                                 // your client secret
        "expected issuer",                               // expected issuer, used to validate ID-token
        signatureVerificationKey                         // signature verification key
    );
```

The configuration is immutable and can be reused.


### LoginRequest

The JavaFX-components use class `LoginRequest` to wrap necessary info into a login request: An OpenID Connect
client and the OAuth-scopes you want to authorize. The client adds necessary scopes (the `openid` scope) to
the request automatically. The following example requests authorization for scopes `email`, `profile` and
`https://apis.10duke.com/auth/openidconnect/organization`:

```java
    final OpenIdAuthorizationCodeClient client = new OpenIdAuthorizationCodeClient(config);
    final LoginRequest request = new LoginRequest(
        client,
        "email",                                                   // a scope
        "profile",                                                 // another scope
        "https://apis.10duke.com/auth/openidconnect/organization"  // another scope
    );
```

The `OpenIdAuthorizationCodeClient` is immutable and can be reused.


### Using modal LoginDialog

`LoginDialog` opens a modal dialog, which starts the login flow and waits for the login to close with either
success, cancel or failure. The dialog is invoked with the `LoginRequest` and it returns a
`OpenIdAuthorizationCodeResponse` on successful login or `null` if login was dismissed (e.g. by closing the
login dialog). Errors are reported via exceptions.

The successful response contains access token, refresh token and the ID-token.

```java
    try {
        // Open the dialog and wait for the login to complete:
        final OpenIdAuthorizationCodeResponse response = new LoginDialog(rootStage)
                .show(request);
        //
        // Null response means that the dialog was dismissed
        if (response == null) {
            System.out.println("Login canceled");
        }
        // Otherwise, the login succeeds
        else {
            System.out.println("Login successful, token: " + response);
        }
    }
    // Errors are reported with exceptions
    catch (final LoginException e) {
        System.out.println("Login failed: " + e.getEvent().getCause());
    }
```

### Using LoginControl

If you need more control or want to embed the login into your UI, you can use `LoginControl` instead of the
dialog. The control wraps an embedded browser, which performs the login-flow and reports of the login
success or failure by emitting events.

The login flow is started when property `requestProperty` changes, which can be done by calling method
`start()`. In FXML, binding controller to the `request`-property does the same thing. Event listeners are
registered with methods `setOnLoginError()` and `setOnLoginSuccess()`.

The successful response contains access token, refresh token and the ID-token.

Example:

```java
    // Instantiate the LoginControl:
    final Login login = new LoginControl();
    login.setOnLoginError(event -> {
        //... do something with the LoginErrorEvent
    });
    login.setOnLoginSuccess(event -> {
        //... do something with the LoginSuccessEvent
    });
    
    final Stage stage ...; // Some JavaFX-stage
    
    // Set the login-control to some JavaFX-stage. The login-control could be added e.g. to some container,
    // depending on how you want to integrate it to the UI
    stage.setScene(new Scene(login));
    
    // Starts the login
    login.start(request);
```

### Complete example with LoginDialog:

```java
    final Stage parentStage = ...; // Stage on which to show the dialog
    final Key signatureVerificationKey = ...; // Used to verify ID-token signature. Provided to you 10Duke.
    
    // Configuration: Following are fake values for demonstrating the usage.
    // Actual values are provided to you by 10Duke.
    final OpenIdAuthorizationCodeConfig config = new OpenIdAuthorizationCodeConfig(
            "javafx",                                        // your client id
            URI.create("https://example.com/oauth2/authz"),  // OAuth-authorization endpoint
            URI.create("http://127.0.0.1:49151/login"),      // re-direct URI
            URI.create("https://example.com/oauth2/access"), // authorization code token endpoint
            "client secret",                                 // your client secret
            "expected issuer",                               // expected issuer, used to validate ID-token
            signatureVerificationKey                         // signature verification key, see above
    );
    
    final OpenIdAuthorizationCodeClient client = new OpenIdAuthorizationCodeClient(config);

    // Login request contains configured client and requested scopes.
    // OpenIdAuthorizationCodeClient automatically adds the "openid" -scope
    final LoginRequest request = new LoginRequest(
            client,
            "email",                                                   // a scope
            "profile",                                                 // another scope
            "https://apis.10duke.com/auth/openidconnect/organization"  // another scope
    );
    
    try {
        // Open the dialog and wait for the login to complete:
        final OpenIdAuthorizationCodeResponse response = new LoginDialog(rootStage)
                .show(request);

        // Null response means that the dialog was dismissed
        if (response == null) {
            System.out.println("Login canceled");
        }
        // Otherwise, the login succeeds
        else {
            System.out.println("Login successful, token: " + response);
        }
    }
    // Errors are reported with exceptions
    catch (final LoginException e) {
        System.out.println("Login failed: " + e.getEvent().getCause());
    }
```

### Demo

For a working demo, see artifact [com.10duke.client.sso:javax-demo](../javafx-demo)


### Storing the response

Successful login response contains the access-token, refresh-token and ID-token. The application should store
these for further use. The tokens can be persisted so that the user does not need to re-authenticate as long
as the refresh-token does not expire. If persisted, the tokens should be encrypted. How the tokens are
persisted and encrypted is up to the developer.


### Refreshing the access token

Class `OpenIdAuthorizationCodeClient` has method for refreshing the access token:

```java
    // The original login response contains the refresh-token
    final String refreshToken = ...;

    // Refresh the token with the client:
    final OpenIdAuthorizationCodeResponse response = client.refresh(refreshToken);
```

The response then contains new access token and possibly changed refresh-token (depending on the
implementation). If the response contains refresh-token, it should replace the original refresh-token.
