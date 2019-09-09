/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import com.tenduke.client.testutils.StubbedRandom;
import java.util.Map;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import org.junit.Before;
import org.junit.Test;

public class AbstractOAuthFlowBuilderTest {

    private Impl flow;

    @Before
    public void beforeTest() {
        this.flow = new Impl();
    }

    @Test
    public void shouldAddSingleParameter() {
        assertThat(flow.parameter("a", "b").getParameters()).containsOnly(
                entry("a", "b")
        );
    }

    @Test
    public void shouldAddMultipleParameters() {
        assertThat(flow.parameters(Map.of("a", "b", "c", "d")).getParameters()).containsOnly(
                entry("a", "b"),
                entry("c", "d")
        );
    }

    @Test
    public void shouldAddScope() {
        assertThat(flow.scope("a").getScopes()).containsOnly("a");
    }

    @Test
    public void shouldAddCollectionOfScopes() {
        assertThat(flow.scopes(Set.of("a", "b")).getScopes()).containsOnly("a", "b");
    }

    @Test
    public void shouldAddArrayOfScopes() {
        assertThat(flow.scopes("a", "b").getScopes()).containsOnly("a", "b");
    }

    @Test
    public void shouldAddNothingIfArrayOfScopesIsNull() {
        assertThat(flow.scopes((String[])null).getScopes()).isEmpty();
    }

    @Test
    public void shouldSetState() {
        assertThat(flow.state("alaska")).hasFieldOrPropertyWithValue("state", "alaska");
    }

    @Test
    public void shouldGetStateThatWasSetByUser() {
        assertThat(flow.state("alaska").getState()).isEqualTo("alaska");
    }

    @Test
    public void shouldGetStateFromParameterSetByUser() {
        assertThat(flow.parameter("state", "alaska").getState()).isEqualTo("alaska");
    }

    @Test
    public void getStateShouldGenerateStateIfNoneProvidedByUser() {
        assertThat(flow.getState()).isNotNull().hasSize(22);
    }

    @Test
    public void getStateShouldGenerateRandomStateOnlyOnce() {

        flow.getState();

        assertThat(flow.getState()).isNotNull().hasSize(22);
        assertThat(flow.random.callNo).isEqualTo(1);
    }

    // <editor-fold defaultstate="collapsed" desc="Class implementation">

    private static class Impl extends AbstractOAuthFlowBuilder<Impl> {

        public StubbedRandom random;

        public Impl(final StubbedRandom random) {
            super(random);
            this.random = random;
        }

        public Impl() {
            this(new StubbedRandom());
        }

        @Override
        public OAuthFlow<OAuthResponse> start() {
            return null;
        }
    }


    // </editor-fold>

}
