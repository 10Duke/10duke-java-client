/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.testutils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpClientTestUtil {

    public static void stubHttp(
            final HttpClient http,
            final String expectedMethod,
            final String expectedUri,
            final int respondedStatusCode,
            final String responseBody
    ) throws InterruptedException, IOException {
        @SuppressWarnings("unchecked")
        final HttpResponse<String> response = mock(HttpResponse.class);

        when(http.send(
                argThat(new HttpRequestMatcher(expectedMethod, URI.create(expectedUri))),
                argThat(new BodyHandlerMatcher<String>()))
        ).thenReturn(response);

        when(response.statusCode()).thenReturn(respondedStatusCode);
        when(response.body()).thenReturn(responseBody);
    }

    public static void stubHttpToThrowIOException(
            final HttpClient http,
            final String expectedMethod,
            final String expectedUri
    ) throws InterruptedException, IOException {
        when(http.send(
                argThat(new HttpRequestMatcher(expectedMethod, URI.create(expectedUri))),
                argThat(new BodyHandlerMatcher<String>()))
        ).thenThrow(new IOException("simulated exception"));
    }

}
