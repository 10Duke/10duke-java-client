/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

public class FakeDeserializer implements JsonDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(String string, Class<T> klass) throws JsonDeserializationException {

        if (klass == String.class) {
            return (T)"hello, world!";
        }
        return null;
    }

}
