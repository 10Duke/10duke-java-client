/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class IdpBackend implements AutoCloseable {

    private final MockWebServer server;

    public IdpBackend() {
        server = new MockWebServer();
    }

    @Override
    public void close() throws IOException {
        server.close();
    }

    public String url() {
        return server.url("/").toString();
    }

    public void dialog(final Dialog dialog) throws IOException {
        for (final MockResponse response : dialog.responses()) {
            server.enqueue(response);
        }
        server.start();
    }

    public void verify(final Dialog dialog) {
        // TODO
    }

    public enum Dialog {

        ERROR_AUTHZ(List.of(
                new MockResponse().setResponseCode(302).addHeader("Location", "http://127.0.0.1:49151/login?state=alaska&error=simulated-error&error_description=simulated-description")
        )),
        INVALID_REDIRECT_URI(List.of(
                new MockResponse().setResponseCode(302).addHeader("Location", "http://127.0.0.1:49151/login?state=florida&code={}")
        )),
        INVALID_STATE(List.of(
                new MockResponse().setResponseCode(302).addHeader("Location", "http://127.0.0.1:49151/login?state=florida&code=open-sesame")
        )),
        SUCCESS(List.of(
                new MockResponse().setResponseCode(302).addHeader("Location", "http://127.0.0.1:49151/login?state=alaska&code=open-sesame"),
                new MockResponse().setResponseCode(200).setBody("SIMULATED TOKEN RESPONSE")
        ));

        private final ArrayList<MockResponse> responses;

        private Dialog(
                final List<MockResponse> responses
        ) {
            this.responses = new ArrayList<>(responses);
        }

        public List<MockResponse> responses() {
            return responses;
        }
    }

}
