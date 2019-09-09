/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt.jjwt;

import com.tenduke.client.jwt.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;

public class JjwtParserTest extends BaseJjwtTst {

    private JwtParser wrappedParser;
    private JjwtParser parser;

    @Before
    public void beforeTest() {
        wrappedParser = Jwts.parser().setSigningKey(verificationKey);

        parser = new JjwtParser(wrappedParser);
    }

    @Test
    public void shouldParseValidToken() throws JwtException {
        assertThat(parser.parse(VALID_TOKEN)).containsOnly(
                entry("sub", "a"),
                entry("iat", 42)
        );
    }

    @Test
    public void shouldWrapExpiredJwtExceptionToJwtException() {
        wrappedParser.requireExpiration(Date.from(Instant.now()));

        assertThatExceptionOfType(JwtException.class).isThrownBy(() -> parser.parse(EXPIRED_TOKEN))
                .withCauseInstanceOf(ExpiredJwtException.class);
    }

    @Test
    public void shouldWrapMalformedJwtExceptionToJwtException() {
        assertThatExceptionOfType(JwtException.class).isThrownBy(() -> parser.parse(MALFORMED_TOKEN))
                .withCauseInstanceOf(MalformedJwtException.class);
    }

    @Test
    public void shouldWrapSignatureExceptionToJwtException() {
        assertThatExceptionOfType(JwtException.class).isThrownBy(() -> parser.parse(TOKEN_WITH_INVALID_SIGNATURE))
                .withCauseInstanceOf(SignatureException.class);
    }

    @Test
    public void shouldWrapIllegalArgumentExceptionToJwtException() {
        assertThatExceptionOfType(JwtException.class).isThrownBy(() -> parser.parse(null))
                .withCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldOtherJwtParsingExceptionsToJwtException() {
        wrappedParser.requireExpiration(Date.from(Instant.now()));

        assertThatExceptionOfType(JwtException.class).isThrownBy(() -> parser.parse(VALID_TOKEN))
                .withCauseInstanceOf(io.jsonwebtoken.JwtException.class);
    }

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhIiwiZXhwIjo0MywiaWF0Ijo0Mn0.jvEB3X4-LYyCKL4retzp-SpmSHl_QVfOTy-fKFdpiV8ni7wb8KX0Ka7c2fw2rj4DwuaF8JJBOx0A3sFPryM35A5Oz-3NJUUGj-lpCdp1wJAQw7QjDk3IHSvFF-jJugZXub52Q7q0jqaYZ72QXUgoxxeARjxP_DIagx0sFZU4UkKVv3OwNC_zqT_BWgN1DhvdDHZsNvt0BxhKwPs0q2WoPC7lhbHcSMRKurtemzDNef-9hFN36_0iJCcrl1QWHCHc42J7wuQArXASsCdTYdShtAKXwQ3xzXkkDqUoE68jbjHLJKoMV2iaFiz8ZBvKp-nnGssRdhBHGbBikLTlLca83g";
    private static final String MALFORMED_TOKEN = "eyJzdWIiOiJhIiwiZXhwIjo0MywiaWF0Ijo0Mn0.jvEB3X4-LYyCKL4retzp-SpmSHl_QVfOTy-fKFdpiV8ni7wb8KX0Ka7c2fw2rj4DwuaF8JJBOx0A3sFPryM35A5Oz-3NJUUGj-lpCdp1wJAQw7QjDk3IHSvFF-jJugZXub52Q7q0jqaYZ72QXUgoxxeARjxP_DIagx0sFZU4UkKVv3OwNC_zqT_BWgN1DhvdDHZsNvt0BxhKwPs0q2WoPC7lhbHcSMRKurtemzDNef-9hFN36_0iJCcrl1QWHCHc42J7wuQArXASsCdTYdShtAKXwQ3xzXkkDqUoE68jbjHLJKoMV2iaFiz8ZBvKp-nnGssRdhBHGbBikLTlLca83g";
    private static final String TOKEN_WITH_INVALID_SIGNATURE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhIiwiZXhwIjo0MywiaWF0Ijo0Mn0.jvEB3X4-LYYCKL4retzp-SpmSHl_QVfOTy-fKFdpiV8ni7wb8KX0Ka7c2fw2rj4DwuaF8JJBOx0A3sFPryM35A5Oz-3NJUUGj-lpCdp1wJAQw7QjDk3IHSvFF-jJugZXub52Q7q0jqaYZ72QXUgoxxeARjxP_DIagx0sFZU4UkKVv3OwNC_zqT_BWgN1DhvdDHZsNvt0BxhKwPs0q2WoPC7lhbHcSMRKurtemzDNef-9hFN36_0iJCcrl1QWHCHc42J7wuQArXASsCdTYdShtAKXwQ3xzXkkDqUoE68jbjHLJKoMV2iaFiz8ZBvKp-nnGssRdhBHGbBikLTlLca83g";
}
