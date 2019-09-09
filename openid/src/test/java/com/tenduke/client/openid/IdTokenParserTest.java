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
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IdTokenParserTest {

    private IdTokenParser parseIdToken;
    private JwtParser jwt;

    @Before
    public void beforeTest() {
        jwt = mock(JwtParser.class);

        parseIdToken = new IdTokenParser(jwt);
    }

    @Test
    public void shouldParseIdTokenWithAdditionalProperties() throws IdTokenException, JwtException {
        when(jwt.parse("simulated-value")).thenReturn(Map.ofEntries(
                entry("iss", "is-sue-r"),
                entry("sub", "sub-ject"),
                entry("aud", "OOOO-ence"),
                entry("exp", 44),
                entry("iat", 43),
                entry("auth_time", 42),
                entry("nonce", "non-sense"),
                entry("acr", "a-c-r"),
                entry("amr", List.of("a", "b")),
                entry("azp", "a-z-p"),
                entry("hello", "world")
        ));

        final IdToken actual = parseIdToken.from("simulated-value");

        assertThat(actual.getIss()).isEqualTo("is-sue-r");
        assertThat(actual.getSub()).isEqualTo("sub-ject");
        assertThat(actual.getAud()).containsExactly("OOOO-ence");
        assertThat(actual.getExp()).isEqualTo(Instant.parse("1970-01-01T00:00:44Z"));
        assertThat(actual.getIat()).isEqualTo(Instant.parse("1970-01-01T00:00:43Z"));
        assertThat(actual.getAuthTime()).isEqualTo(Instant.parse("1970-01-01T00:00:42Z"));
        assertThat(actual.getNonce()).isEqualTo("non-sense");
        assertThat(actual.getAcr()).isEqualTo("a-c-r");
        assertThat(actual.getAmr()).containsExactly("a", "b");
        assertThat(actual.getAzp()).isEqualTo("a-z-p");

        // Verify that none of the fixed properties ends as additional properties
        assertThat(actual.getAdditionalProperties()).containsOnly(
                entry("hello", "world")
        );
    }

    @Test
    public void parseIdTokenShouldWrapJwtExceptionToIdTokenException() throws IdTokenException, JwtException {
        when(jwt.parse("simulated-value")).thenThrow(new JwtException("simulated exception"));

        assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            parseIdToken.from("simulated-value");
        }).withCauseInstanceOf(JwtException.class);
    }

    @Test
    public void stringClaimShouldReturnClaimAsString() {
        assertThat(parseIdToken.stringClaim("a")).isEqualTo("a");
    }

    @Test
    public void stringClaimShouldReturnNullIfPassedNull() {
        assertThat(parseIdToken.stringClaim(null)).isNull();
    }

    @Test
    public void parseListOfStringShouldParseListClaimToListOfStrings() {
        assertThat(parseIdToken.parseListOfStrings(List.of("a", "b"))).isEqualTo(List.of("a", "b"));
    }

    @Test
    public void parseListOfStringShouldParseNonListClaimToToListOfOneStringClaim() {
        assertThat(parseIdToken.parseListOfStrings("a")).isEqualTo(List.of("a"));
    }

    @Test
    public void parseListOfStringShouldReturnEmptyListIfClaimNull() {
        assertThat(parseIdToken.parseListOfStrings(null)).isEmpty();
    }

    @Test
    public void toInstantShouldConvertLongToInstant() {
        assertThat(parseIdToken.toInstant(42L)).isEqualTo(Instant.parse("1970-01-01T00:00:42Z"));
    }

    @Test
    public void toInstantShouldReturnNullWhenPassedNullLong() {
        assertThat(parseIdToken.toInstant((Long)null)).isNull();
    }

    @Test
    public void toInstantShouldConvertNumericValueToInstant() {
        assertThat(parseIdToken.toInstant(42D)).isEqualTo(Instant.parse("1970-01-01T00:00:42Z"));
    }

    @Test
    public void toInstantShouldReturnNullIfClaimIsNotNumeric() {
        assertThat(parseIdToken.toInstant("non-numeric")).isNull();
    }

    @Test
    public void toInstantShouldReturnNullIfNullObjectClaim() {
        assertThat(parseIdToken.toInstant((String)null)).isNull();
    }
}
