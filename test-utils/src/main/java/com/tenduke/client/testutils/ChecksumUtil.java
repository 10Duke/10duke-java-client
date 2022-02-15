/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.testutils;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ChecksumUtil {

    public static String urlSafeSHA256(final String str) {
        try {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(str.getBytes(UTF_8))
            );
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("No such algorithm!?!?!", e);
        }
    }

}
