/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt;

import java.security.Key;

public class FakeJwtParserFactory implements JwtParserFactory {

    @Override
    public JwtParser create(final Key validationKey) {
        return new FakeJwtParser();
    }

}
