/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

import java.security.Key;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * A singleton to access default {@link JwtParserFactory}.
 *
 * <p>
 * The default factory is initialized using {@link ServiceLoader}. Implementation of the {@code JwtParserFactory} is loaded from
 * classpath. In Maven terms, declare a dependency to an artifact containing the implementation of the service (with proper
 * {@code META-INF/services} definition).
 */
public enum DefaultJwtParserFactory implements JwtParserFactory {
    /**
     * The singleton instance.
     */
    INSTANCE;

    /**
     * The actual factory implementation.
     */
    private final JwtParserFactory factory;

    /**
     * Constructs new instance.
     *
     * @throws IllegalStateException -
     */
    DefaultJwtParserFactory() throws IllegalStateException {
        final ServiceLoader<JwtParserFactory> jwtParserFactories = ServiceLoader.load(JwtParserFactory.class);
        final Optional<JwtParserFactory> configuredFactory = jwtParserFactories.findFirst();

        this.factory = configuredFactory.orElseThrow(() -> new IllegalStateException(
                "No implementation of "
                + JwtParserFactory.class.getCanonicalName()
                + " found in classpath by ServiceLoader."
        ));
    }

    /**
     * {@inheritDoc}
     *
     * @param validationKey -
     * @return -
     */
    @Override
    public JwtParser create(final Key validationKey) {
        return factory.create(validationKey);
    }

}
