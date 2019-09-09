/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.testutils;

import com.tenduke.client.json.JsonDeserializationException;
import com.tenduke.client.json.JsonDeserializer;

public class FakeJsonDeserializer implements JsonDeserializer{

    @Override
    public <T> T deserialize(String string, Class<T> klass) throws JsonDeserializationException {
        throw new UnsupportedOperationException("Not intented to be called");
    }

}
