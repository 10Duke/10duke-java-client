/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

/**
 * A generic JSON-deserializer.
 *
 *
 */
public interface JsonDeserializer {

    /**
     * Deserialize a {@code String} to an object of class {@code klass}.
     *
     * @param <T> type of the object to deserialize
     * @param string -
     * @param klass -
     * @return -
     * @throws JsonDeserializationException -
     */
    <T> T deserialize(String string, Class<T> klass) throws JsonDeserializationException;

}
