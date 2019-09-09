/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.json;

/**
 * Exception thrown when JSON-deserialization fails.
 *
 */
public class JsonDeserializationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * No-arg constructor.
     *
     */
    public JsonDeserializationException() {
        super();
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     */
    public JsonDeserializationException(final String message) {
        super(message);
    }

    /**
     * Constructs new instance.
     *
     * @param message -
     * @param cause -
     */
    public JsonDeserializationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
