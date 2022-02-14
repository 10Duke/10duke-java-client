/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import java.security.NoSuchAlgorithmException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.Test;

public class AbstractAuthorizationCodeRequestTest {

    @Test
    public void createDigestShouldThrowIllegalArgumentExceptionIfNoSuchAlgorithm() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AbstractAuthorizationCodeRequest.createDigest("NO-SUCH-ALGORITHM"))
                .withCauseExactlyInstanceOf(NoSuchAlgorithmException.class);
    }

}
