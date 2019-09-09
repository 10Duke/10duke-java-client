/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Simple parser for parsing URL-query strings.
 *
 * <p>
 * This implementation expects the query string to be URL-encoded.
 *
 * <p>
 * NOTE: This class may move to another artifact in the future.
 */
public class QueryParser {

    /** Encoding of the query string. */
    private final Charset charset;

    /**
     * Constructs new instance.
     *
     * @param charset -
     */
    public QueryParser(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Parses a query-string into a key-values -pairs.
     *
     * <p>
     * NOTE: The query string should be provided without the leading separator (e.g. question mark).
     *
     * @param queryString -
     * @return -
     */
    public Map<String, List<String>> from(final String queryString) {
        final HashMap<String, List<String>> result = new HashMap<>();
        final String[] pairs = queryString.split("&");

        for (final String pair : pairs) {
            final String[] split = pair.split("=");
            final String key = decode(split[0]);
            final String value = (split.length <= 1 ? null : decode(split[1]));
            @Nullable final List<String> values = result.getOrDefault(key, new ArrayList<>());

            if (value != null) {
                values.add(value);
            }

            result.putIfAbsent(key, values);
        }

        return result;
    }

    /** URL-decodes value.
     *
     * @param value -
     * @return URL-decoded value of the query string. Returns {@code null} if the value is {@code null}.
     */
    protected @Nullable String decode(@Nullable final String value) {
        return (value == null ? null : URLDecoder.decode(value, charset));
    }

}
