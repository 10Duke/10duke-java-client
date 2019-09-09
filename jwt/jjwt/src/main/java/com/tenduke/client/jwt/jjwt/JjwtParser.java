/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt.jjwt;

import com.tenduke.client.jwt.JwtException;
import com.tenduke.client.jwt.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;

/**
 * JJWT-based implementation of {@link JwtParser}.
 *
 */
public class JjwtParser implements JwtParser {

    /**
     * The wrapped parser.
     */
    private final io.jsonwebtoken.JwtParser jwt;

    /**
     * Constructs new instance.
     *
     * @param jwt -
     */
    public JjwtParser(final io.jsonwebtoken.JwtParser jwt) {
        this.jwt = jwt;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Map<String, Object> parse(final String token) throws JwtException {
        // MissingClaimException
        // InvalidClaimException

        try {
            final Jws<Claims> parsedToken = jwt.parseClaimsJws(token);

            return parsedToken.getBody();
        } catch (final ExpiredJwtException e) {
            throw new JwtException("Token has expired", e);
        } catch (final UnsupportedJwtException e) {
            throw new JwtException("Unsuppored JWT", e);
        } catch (final MalformedJwtException e) {
            throw new JwtException("Malformed JWT", e);
        } catch (final SignatureException e) {
            throw new JwtException("JWT signature validation failed", e);
        } catch (final io.jsonwebtoken.JwtException e) {
            throw new JwtException("JWT-exception", e);
        } catch (final IllegalArgumentException e) {
            throw new JwtException("Illegal argument", e);
        }
    }

}
