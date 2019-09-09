/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

import java.util.Map;

/**
 * Signature for a simple JWT-parser.
 *
 */
public interface JwtParser {

    /**
     * Parses string-representation of JWT into map, which contains the claims.
     *
     * @param token -
     * @return -
     * @throws JwtException -
     */
    Map<String, Object> parse(String token) throws JwtException;
}
