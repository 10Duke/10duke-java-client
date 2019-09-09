/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

import java.util.Map;

public class FakeJwtParser implements JwtParser {

    @Override
    public Map<String, Object> parse(final String token) throws JwtException {
        return null;
    }

}
