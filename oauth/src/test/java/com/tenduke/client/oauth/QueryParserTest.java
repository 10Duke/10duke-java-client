/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import org.junit.Test;

public class QueryParserTest {

    private QueryParser parseQuery = new QueryParser(UTF_8);

    @Test
    public void shouldParseValidQueryString() {
        assertThat(parseQuery.from("hello=world&hello=you&empty%20object=%7B%7D")).containsOnly(
                entry("empty object", List.of("{}")),
                entry("hello", List.of("world", "you"))
        );
    }

    @Test
    public void parseQueryShouldAddKeyButNotValueIfValueMissingInQuery() {
        assertThat(parseQuery.from("no-val&hello=world")).containsOnly(
                entry("hello", List.of("world")),
                entry("no-val", List.of())
        );
    }

    @Test
    public void decodeShouldReturnNullIfProvidedNull() {
        assertThat(parseQuery.decode(null)).isNull();
    }

    @Test
    public void decodeShouldUrlDecodeGivenValue() {
        assertThat(parseQuery.decode("%7B%7D")).isEqualTo("{}");
    }

}
