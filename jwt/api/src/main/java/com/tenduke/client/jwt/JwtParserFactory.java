/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

import java.security.Key;

/**
 * Factory for creating {@link JwtParser} with given validation {@link Key}.
 *
 */
public interface JwtParserFactory {

    /**
     * Creates the parser.
     *
     * @param validationKey -
     * @return -
     */
    JwtParser create(Key validationKey);
}
