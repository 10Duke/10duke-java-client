/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenduke.client.json.DynamicBean;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class AdditionalPropertyHandlerTest {

    private ObjectMapper mapper;

    @Before
    public void beforeTest() {
        mapper = JacksonDeserializer.createDefaultObjectMapper();
    }

    @Test
    public void shouldHandleArray() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": [\"a\", \"b\"]}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo(List.of("a", "b"));
    }

    @Test
    public void shouldHandleBooleanFalse() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": false}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo(Boolean.FALSE);
    }

    @Test
    public void shouldHandleBooleanTrue() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": true}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void shouldHandleFloat() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": 5.2}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo(5.2D);
    }

    @Test
    public void shouldHandleInt() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": 5}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo(5);
    }

    @Test
    public void shouldHandleString() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": \"a\"}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo("a");
    }

    @Test
    public void shouldHandleObject() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": {\"hello\": \"world\"}}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isEqualTo(Map.of("hello", "world"));
    }

    @Test
    public void shouldHandleNullAndOtherValues() throws IOException {
        assertThat(mapper
                .readValue("{\"val\": null}", DynamicBean.class)
                .gimmeAdditionalProperty("val")
        ).isNull();
    }
}
