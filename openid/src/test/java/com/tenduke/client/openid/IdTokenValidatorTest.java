/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;

public class IdTokenValidatorTest {

    private IdTokenValidator validator;
    private String iss;
    private String sub;
    private Collection<String> aud;
    private Instant exp;
    private Instant iat;
    private Instant authTime;
    private String nonce;
    private String acr;
    private Collection<String> amr;
    private String azp;

    @Before
    public void beforeTest() {
        validator = new IdTokenValidator("unit-test", "is-sue-r");

        iss = "is-sue-r";
        sub = "sub-ject";
        aud = List.of("unit-test", "other audience");
        exp = Instant.now().plusSeconds(3600);
        iat = Instant.now().minusSeconds(3600);
        authTime = Instant.now().minusSeconds(3600);
        nonce = "non-sense";
        acr = "a-c-r";
        amr = List.of("a", "b");
        azp = "unit-test";
    }

    @Test
    public void shouldValidateToken() throws IdTokenException {
        validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
    }

    @Test
    public void shouldValidateIfSingleAudienceAndAuthorizedPartyIsNull() throws IdTokenException {
        aud = List.of("unit-test");
        azp = null;

        validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
    }

    @Test
    public void shouldValidateIfOldRejectionIsNull() throws IdTokenException {
        validator.validate(createToken(), null, "non-sense");
    }

    @Test
    public void shouldValidateIfNoNonceExpected() throws IdTokenException {
        nonce = null;

        validator.validate(createToken(), Instant.now().minusSeconds(7200), null);
    }

    @Test
    public void shouldFailIfIssuerDoesNotMatch() throws IdTokenException {
        iss = "Is-sue-r";

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("Issuer");
    }

    @Test
    public void shouldFailIfClientIdNotPresentInAudiences() throws IdTokenException {
        aud = List.of("invalid-1", "invalid-2");

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("Claim \"aud\"");
    }

    @Test
    public void shouldFailIfMultipleAudiencesButAzpIsNull() throws IdTokenException {
        azp = null;

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("ID token contains multiple audiences, but claim \"azp\" not present");
    }

    @Test
    public void shouldFailIfMultipleAudiencesButAzpIsEmpty() throws IdTokenException {
        azp = "";

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("ID token contains multiple audiences, but claim \"azp\" not present");
    }

    @Test
    public void shouldFailIfAzpIsPresentButDoesNotMatchClientId() throws IdTokenException {
        azp = "some-other-client-id";

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("Invalid authorized party (\"azp\")");
    }

    @Test
    public void shouldFailIfTokenExpired() throws IdTokenException {
        exp = Instant.now().minusSeconds(1);

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("Token expired");
    }

    @Test
    public void shouldFailIfTokenIssuedTooLongTimeAgo() throws IdTokenException {
        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(1), "non-sense");
        }).withMessageStartingWith("Token too old");
    }

    @Test
    public void shouldFailIfNonceDoesNotMatch() throws IdTokenException {
        nonce = "sneaky attack";

        AssertionsForClassTypes.assertThatExceptionOfType(IdTokenException.class).isThrownBy(() -> {
            validator.validate(createToken(), Instant.now().minusSeconds(7200), "non-sense");
        }).withMessageStartingWith("Nonce does not match");
    }

    protected IdToken createToken() {
        return new IdToken(iss, sub, aud, exp, iat, authTime, nonce, acr, amr, azp);
    }
}