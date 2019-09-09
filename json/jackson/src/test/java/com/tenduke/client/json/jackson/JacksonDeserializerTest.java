/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json.jackson;

import com.tenduke.client.json.DynamicBean;
import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import java.io.IOException;
import java.util.ServiceLoader;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class JacksonDeserializerTest {

    @Test
    public void shouldDeserialize() throws JsonDeserializationException {
        assertThat(new JacksonDeserializer().deserialize("\"hello, world!\"", String.class)).isEqualTo("hello, world!");
    }

    @Test
    public void shouldDeserializeWithSnakeCase() throws JsonDeserializationException {
        assertThat(new JacksonDeserializer()
                .deserialize("{\"string_value\": \"hello, world!\"}", StaticTestBean.class)
                .getStringValue()
        ).isEqualTo("hello, world!");
    }

    @Test
    public void serializeFromStringShouldWrapExceptionsToJsonSerializationException() {
        Assertions.assertThatExceptionOfType(JsonDeserializationException.class).isThrownBy(() -> {
            new JacksonDeserializer().deserialize("{\"a: \"b\"}", StaticTestBean.class);
        }).withCauseInstanceOf(IOException.class);
    }

    @Test
    public void defaultObjectMapperShouldHandleAdditionalPropertiesOnDynamicBeans() throws IOException {
        assertThat(JacksonDeserializer.createDefaultObjectMapper()
                .readValue("{\"hello\": \"world\"}", DynamicTestBean.class)
                .gimmeAdditionalProperty("hello")
        ).isEqualTo("world");
    }

    @Test
    public void defaultObjectMapperShouldSkipOnUnknownPropertiesOnNonDynamicBeans() throws IOException {
        assertThat(JacksonDeserializer.createDefaultObjectMapper()
                .readValue("{\"hello\": \"world\"}", StaticTestBean.class)
        ).usingRecursiveComparison().isEqualTo(new StaticTestBean());
    }

    @Test
    public void defaultObjectMapperShouldDeserializeFieldsUsingCamelCase() throws IOException {
        assertThat(JacksonDeserializer.createDefaultObjectMapper().readValue("{\"stringValue\": \"snake\"}", DynamicTestBean.class))
                .hasFieldOrPropertyWithValue("stringValue", "snake");
    }

    @Test
    public void jacksonDeserializerShouldBeRegisteredAsService() {
        assertThat(ServiceLoader.load(JsonDeserializer.class).findFirst().get()).isExactlyInstanceOf(JacksonDeserializer.class);
    }


    private static class StaticTestBean {

        private String stringValue;

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(final String stringValue) {
            this.stringValue = stringValue;
        }
    }

    private static class DynamicTestBean extends DynamicBean {

        private String stringValue;

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(final String stringValue) {
            this.stringValue = stringValue;
        }
    }

}
