/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import org.junit.Before;
import org.junit.Test;

public class DynamicBeanTest {

    private DynamicBean bean;

    @Before
    public void before() {
        bean = new DynamicBean();
    }

    @Test
    public void shouldAddSingleProperty() {
        bean.zetAdditionalProperty("hello", "world");

        assertThat(bean.getAdditionalProperties()).containsOnly(entry("hello", "world"));
    }

    @Test
    public void shouldAddMultiplerProperties() {
        bean.zetAdditionalProperties(Map.of("hello", "world", "sesam", "open"));

        assertThat(bean.getAdditionalProperties()).containsOnly(entry("hello", "world"), entry("sesam", "open"));
    }

    @Test
    public void shouldReturnAdditionalProperty() {
        bean.zetAdditionalProperties(Map.of("hello", "world", "sesam", "open"));

        assertThat(bean.gimmeAdditionalProperty("sesam")).isEqualTo("open");
    }

}
