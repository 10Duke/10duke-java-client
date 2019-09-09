/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.testutils;

import java.util.Random;

public class StubbedRandom extends Random {

    public int callNo = 0;

    @Override
    public void nextBytes(final byte[] bytes) {
        callNo++;
        super.nextBytes(bytes);
    }

}
