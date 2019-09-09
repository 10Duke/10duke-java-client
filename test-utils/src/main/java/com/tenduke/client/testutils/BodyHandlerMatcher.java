/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.testutils;

import java.net.http.HttpResponse;
import org.mockito.ArgumentMatcher;

public class BodyHandlerMatcher<T> implements ArgumentMatcher<HttpResponse.BodyHandler<T>> {

    @Override
    public boolean matches(final HttpResponse.BodyHandler<T> argument) {
        return argument != null;
    }

}
