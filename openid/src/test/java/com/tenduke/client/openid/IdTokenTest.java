/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class IdTokenTest {

    private IdToken token;

    @Before
    public void beforeTest() {
        token = new IdToken(
                "is-sue-r",
                "sub-ject",
                List.of("OOOO-ence"),
                Instant.parse("2001-01-01T00:00:00Z"),
                Instant.parse("2000-01-01T00:00:00Z"),
                Instant.parse("2000-01-02T00:00:00Z"),
                "non-sense",
                "a-c-r",
                List.of("a-m-r"),
                "a-z-p"
        );
    }

    @Test
    public void gettersShouldWork() {
        assertThat(token.getAud()).isEqualTo(List.of("OOOO-ence"));
        assertThat(token.getAcr()).isEqualTo("a-c-r");
        assertThat(token.getAmr()).isEqualTo(List.of("a-m-r"));
        assertThat(token.getAuthTime()).isEqualTo(Instant.parse("2000-01-02T00:00:00Z"));
        assertThat(token.getAzp()).isEqualTo("a-z-p");
        assertThat(token.getExp()).isEqualTo(Instant.parse("2001-01-01T00:00:00Z"));
        assertThat(token.getIat()).isEqualTo(Instant.parse("2000-01-01T00:00:00Z"));
        assertThat(token.getIss()).isEqualTo("is-sue-r");
        assertThat(token.getNonce()).isEqualTo("non-sense");
        assertThat(token.getSub()).isEqualTo("sub-ject");
    }

    @Test
    public void shouldConstructWithNullAmr() {
        assertThat(new IdToken().getAmr()).isNull();
    }

    @Test
    public void toStringShouldBeImplemented() {
        assertThat(token).hasToString("IdToken{"
                + "iss=is-sue-r"
                + ", sub=sub-ject"
                + ", aud=[OOOO-ence]"
                + ", exp=2001-01-01T00:00:00Z"
                + ", iat=2000-01-01T00:00:00Z"
                + ", authTime=2000-01-02T00:00:00Z"
                + ", nonce=non-sense"
                + ", acr=a-c-r"
                + ", amr=[a-m-r]"
                + ", azp=a-z-p"
                + '}'
        );
    }

}
