/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx.demo;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX application demonstrating login.
 *
 */
public class App extends Application {

    /** Scene. */
    private static Scene scene;

    /** Root stage. */
    private static Stage rootStage;

    /**
     * Starts the demo application.
     *
     * @param stage -
     * @throws IOException -
     */
    @Override
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public void start(final Stage stage) throws IOException {
        rootStage = stage;

        scene = new Scene(new FXMLLoader(RootController.class.getResource("root.fxml")).load());

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Returns the root stage.
     *
     * @return -
     */
    public static Stage getRootStage() {
        return rootStage;
    }

    /**
     * Launch.
     *
     * @param args -
     */
    public static void main(final String[] args) {
        launch();
    }

}
