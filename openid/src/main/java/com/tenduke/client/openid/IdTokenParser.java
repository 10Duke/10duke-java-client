/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.jwt.JwtException;
import com.tenduke.client.jwt.JwtParser;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Parses Id-token.
 *
 */
public class IdTokenParser {

    /** JWT-parser. */
    private final JwtParser parser;

    /**
     * Constructs new instance.
     *
     * @param parser -
     */
    public IdTokenParser(final JwtParser parser) {
        this.parser = parser;
    }

    /**
     * Parses Id-token.
     *
     * @param idTokenString -
     * @return -
     * @throws IdTokenException -
     */
    public IdToken from(final String idTokenString) throws IdTokenException {
        final Map<String, Object> claims;

        try {
            claims = parser.parse(idTokenString);
        } catch (final JwtException e) {
            throw new IdTokenException("Invalid id token", e);
        }

        @SuppressWarnings("unchecked")
        final IdToken idToken = new IdToken(
                stringClaim(claims.get("iss")),
                stringClaim(claims.get("sub")),
                parseListOfStrings(claims.get("aud")),
                toInstant(claims.get("exp")),
                toInstant(claims.get("iat")),
                toInstant(claims.get("auth_time")),
                stringClaim(claims.get("nonce")),
                stringClaim(claims.get("acr")),
                parseListOfStrings(claims.get("amr")),
                stringClaim(claims.get("azp"))
        );

        // Add additional claims:
        for (final Map.Entry<String, Object> claim : claims.entrySet()) {
            switch (claim.getKey()) {
                // Skip the default claims:
                case "acr":
                case "amr":
                case "aud":
                case "auth_time":
                case "azp":
                case "exp":
                case "iat":
                case "iss":
                case "nonce":
                case "sub":
                    break;

                // Add unhandled claim:
                default:
                    idToken.zetAdditionalProperty(claim.getKey(), claim.getValue());
                    break;
            }
        }

        return idToken;
    }

    /**
     * Helper for extracting string-claim.
     *
     * @param claim -
     * @return -
     */
    protected @Nullable String stringClaim(@Nullable final Object claim) {
        return (claim == null ? null : claim.toString());
    }

    /**
     * Helper for extracting list-of-string claim.
     *
     * @param value -
     * @return -
     */
    protected List<String> parseListOfStrings(@Nullable final Object value) {
        if (value instanceof List)  {
            final List originalList = (List) value;
            final ArrayList<String> parsedList = new ArrayList<>(originalList.size());

            for (final Object item : originalList) {
                parsedList.add(item.toString());
            }
            return parsedList;
        }
        return (value == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(value.toString())));
    }

    /**
     * Converts "epoch-second" to an {@link Instant}.
     *
     * @param epochSecond -
     * @return -
     */
    public @Nullable Instant toInstant(@Nullable final Long epochSecond) {
        return (epochSecond == null ? null : Instant.ofEpochSecond(epochSecond));
    }

    /**
     * Converts an object to an {@link Instant}.
     *
     * <p>
     * Currently only numbers are supported.
     *
     * @param object -
     * @return -
     */
    public @Nullable Instant toInstant(@Nullable final Object object) {
        if (object instanceof Number) {
            return toInstant(((Number) object).longValue());
        }
        return null;
    }

}
