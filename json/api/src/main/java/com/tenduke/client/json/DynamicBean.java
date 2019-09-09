/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * A "dynamic bean", which can have additional properties.
 *
 * <p>
 * This is often used as JSON-deserialization target, where the JSON can contain additional properties.
 */
public class DynamicBean {

    /** The additional properties. */
    private final LinkedHashMap<String, Object> additionalProperties = new LinkedHashMap<>();

    /**
     * Returns the additional properties.
     *
     * @return -
     */
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Get one additional property.
     *
     * @param name -
     * @return -
     */
    public @Nullable Object gimmeAdditionalProperty(final String name) {
        return additionalProperties.get(name);
    }

    /**
     * Sets one additional property.
     *
     * @param name -
     * @param value -
     */
    public void zetAdditionalProperty(
            final String name,
            final Object value
    ) {
        additionalProperties.put(name, value);
    }


    /**
     * Sets multiple additional properties.
     *
     * @param properties -
     */
    public void zetAdditionalProperties(final Map<String, Object> properties) {
        properties.forEach((key, value) -> zetAdditionalProperty(key, value));
    }

}
