/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class DefaultJsonDeserializerTest {

    @Test
    public void shouldLoadDefaultDeserializer() throws JsonDeserializationException {
        assertThat(DefaultJsonDeserializer.INSTANCE.deserialize("hello, world!", String.class)).isEqualTo("hello, world!");
    }

    @Test
    public void getShouldReturnTheDefaultDeserializer() {
        assertThat(DefaultJsonDeserializer.INSTANCE.get()).isInstanceOf(FakeDeserializer.class);
    }

}
