/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;
import java.io.IOException;

/**
 * Jackson-implementation of {@link JsonDeserializer}.
 *
 */
public class JacksonDeserializer implements JsonDeserializer {

    /** Mapper for normal cases. */
    private final ObjectMapper mapper;

    /**
     * Constructs new instance.
     *
     * @param mapper -
     */
    public JacksonDeserializer(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Constructs new instance with default object mapper.
     *
     */
    public JacksonDeserializer() {
        this(createDefaultObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE));
    }

    /**
     * Creates default object mapper.
     *
     * @return -
     */
    public static ObjectMapper createDefaultObjectMapper() {

        final ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.addHandler(new AdditionalPropertyHandler());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
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

        try {
            return mapper.readValue(string, klass);
        } catch (final IOException e) {
            throw new JsonDeserializationException("Error deserializing String value", e);
        }
    }

}
