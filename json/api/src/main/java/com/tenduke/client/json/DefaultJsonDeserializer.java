/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

import java.util.Optional;
import java.util.ServiceLoader;
import org.slf4j.LoggerFactory;

/**
 * A singleton to access default {@link JsonDeserializer}.
 *
 * <p>
 * The default de-serializer is initialized using {@link ServiceLoader}. Implementation of the {@code JsonDeserializer} is loaded from
 * classpath. In Maven terms, declare a dependency to an artifact containing the implementation of the service (with proper
 * {@code META-INF/services} definition).
 */
public enum DefaultJsonDeserializer implements JsonDeserializer {

    /** Singleton instance. */
    INSTANCE;

    /** The default de-serializer. */
    private final JsonDeserializer deserializer;

    /**
     * Constructs new instance: Loads the first {@link JsonDeserializer} implementation found using {@link ServiceLoader}.
     *
     * @throws IllegalStateException if no implementation in classpath.
     */
    DefaultJsonDeserializer() throws IllegalStateException {

        final ServiceLoader<JsonDeserializer> jsonDeserializers = ServiceLoader.load(JsonDeserializer.class);
        final Optional<JsonDeserializer> serializer = jsonDeserializers.findFirst();

        this.deserializer = serializer.orElseThrow(() -> new IllegalStateException(
                "No implementation of "
                + JsonDeserializer.class.getCanonicalName()
                + " found in classpath by ServiceLoader."
        ));

        LoggerFactory.getLogger(DefaultJsonDeserializer.class).debug("Configured with {}", deserializer.getClass().getCanonicalName());
    }

    /**
     * {@inheritDoc}
     *
     * @param <T> -
     * @param string -
     * @param klass -
     * @return -
     * @throws JsonDeserializationException -
     */
    @Override
    public <T> T deserialize(final String string, final Class<T> klass) throws JsonDeserializationException {
        return deserializer.deserialize(string, klass);
    }

    /**
     * Returns the default Json-deserializer.
     *
     * @return -
     */
    public JsonDeserializer get() {
        return deserializer;
    }

}
