/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import com.tenduke.client.testutils.StubbedRandom;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class OpenIdAuthorizationCodeFlowBuilderTest {

    private OpenIdAuthorizationCodeFlowBuilder builder;
    private StubbedRandom random;

    @Before
    public void beforeTest() {
        random = new StubbedRandom();
        builder = new OpenIdAuthorizationCodeFlowBuilder(
                null,
                null,
                null,
                random
        );
    }

    @Test
    public void determineNonceShouldReturnNonceProvidedByUser() {
        assertThat(builder.nonce("non-sense").determineNonce()).isEqualTo("non-sense");
    }

    @Test
    public void determineNonceShouldReturnParameterNonceIfSet() {
        assertThat(builder.parameter("nonce", "non-sense").determineNonce()).isEqualTo("non-sense");
    }

    @Test
    public void determineNonceShouldGenerateNonceIfNoneProvidedByUser() {
        assertThat(builder.determineNonce()).isNotNull().hasSize(22);
    }

    @Test
    public void determineNonceShouldGenerateRandomNonceOnlyOnce() {

        builder.determineNonce();

        assertThat(builder.determineNonce()).isNotNull().hasSize(22);
        assertThat(random.callNo).isEqualTo(1);
    }


}
