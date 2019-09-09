# Very simple demonstration of Single Sign-On with JavaFX

The following JavaFX application demonstrates the the configuration and use of `LoginDialog`. Also, token
refreshing is demonstrated. See the source code for comments.

The application has button "Login", which opens the login-dialog and starts the login flow. Success and error
are reported on console.

After login, to refresh the access token, click on "Refresh". Again, success and error are reported on
console.

To run the demo, execute in the artifact root:

```bash
mvn clean compile javafx:run
```

Enter following credentials: username `javafx-demo@10duke.com`, password `1-2-3-4-5`.

