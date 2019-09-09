/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.testutils;

import java.net.URI;
import java.net.http.HttpRequest;
import org.mockito.ArgumentMatcher;

public class HttpRequestMatcher implements ArgumentMatcher<HttpRequest>
{
    private final String method;
    private final URI uri;

    public HttpRequestMatcher(final String method, final URI uri)
    {
        this.method = method;
        this.uri = uri;
    }

    @Override
    public boolean matches(final HttpRequest argument)
    {
        // TODO: Match body
        // TODO: Match headers
        return method.equalsIgnoreCase(argument.method()) && uri.equals(argument.uri());
    }

}
