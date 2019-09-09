/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt.jjwt;

import com.tenduke.client.jwt.JwtParserFactory;
import io.jsonwebtoken.Jwts;
import java.security.Key;

/**
 * Jjwt-based implementation of {@link JwtParserFactory}.
 *
 *
 */
public class JjwtParserFactory implements JwtParserFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public JjwtParser create(final Key validationKey) {
        return new JjwtParser(Jwts.parser().setSigningKey(validationKey));
    }

}
