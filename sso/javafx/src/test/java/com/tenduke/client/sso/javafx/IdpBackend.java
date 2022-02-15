/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.oauth.QueryParser;
import com.tenduke.client.testutils.ChecksumUtil;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdpBackend implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdpBackend.class);

    private final MockWebServer server;
    private String codeChallenge;

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

    public void verify(final Dialog dialog) throws InterruptedException {
        int requestNo = 0;
        for (final MockResponse response : dialog.responses()) {
            final RecordedRequest request = server.takeRequest();

            switch (++requestNo) {
                // First request: Verify and store PKCE code_challenge
                case 1:
                    LOGGER.debug("Verifying request #1...");
                    codeChallenge = request.getRequestUrl().queryParameter("code_challenge");
                    assertThat(codeChallenge).isNotNull();
                    assertThat(request.getRequestUrl().queryParameter("code_challenge_method")).isEqualTo("S256");
                    break;

                // Second request: verify code verifier
                case 2:
                    LOGGER.debug("Verifying request #2...");
                    final String requestBody = request.getBody().readUtf8();
                    final Map<String, List<String>> parameters = new QueryParser(UTF_8).from(requestBody);
                    final List<String> codeVerifier = parameters.get("code_verifier");

                    assertThat(codeVerifier).hasSize(1);
                    assertThat(ChecksumUtil.urlSafeSHA256(codeVerifier.get(0))).isEqualTo(codeChallenge);

                    break;
                default:
                    break;
            }
        }
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
