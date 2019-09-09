/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.tenduke.client.json.DynamicBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A {@link DeserializationProblemHandler} for handling {@link DynamicBean} without Jackson annotations.
 *
 */
public class AdditionalPropertyHandler extends DeserializationProblemHandler {

    /** ?. */
    private static final TypeReference<LinkedHashMap<String, Object>> MAP = new TypeReference<LinkedHashMap<String, Object>>() { };

    /**
     * {@inheritDoc}
     *
     * @param context -
     * @param parser -
     * @param deserializer -
     * @param beanOrClass -
     * @param propertyName -
     * @return -
     * @throws IOException -
     */
    @Override
    public boolean handleUnknownProperty(
            final DeserializationContext context,
            final JsonParser parser,
            final JsonDeserializer<?> deserializer,
            final Object beanOrClass,
            final String propertyName
    ) throws IOException {

        if (beanOrClass instanceof DynamicBean) {
            final DynamicBean bean = (DynamicBean) beanOrClass;

            final Object value;

            switch (parser.currentToken()) {
                case START_ARRAY:
                    value = parser.readValueAs(ArrayList.class);
                    break;
                case VALUE_FALSE:
                    value = Boolean.FALSE;
                    break;
                case VALUE_TRUE:
                    value = Boolean.TRUE;
                    break;
                case VALUE_NUMBER_FLOAT:
                    value = parser.getDoubleValue();
                    break;
                case VALUE_NUMBER_INT:
                    value = parser.getIntValue();
                    break;
                case VALUE_STRING:
                    value = parser.getText();
                    break;
                case START_OBJECT:
                    value = parser.readValueAs(MAP);
                    break;
                default:
                    value = null;
                    break;
            }

            if (value != null) {
                bean.zetAdditionalProperty(propertyName, value);
            }

            return true;
        }

        return false;
    }

}
