/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthConfig config;
    private OAuthRequest<OAuthConfig> request;

    @Before
    public void beforeTest() {
        config = new OAuthConfig(
                "unit-test",
                URI.create("https://example.com/oauth"),
                URI.create("tenduke://callback")
        );
        request = new OAuthRequest<>(
                config,
                Map.of("hello:", "world!"),
                "fake",
                new LinkedHashSet<>(List.of("http://a.a/", "http://b.b/")),
                "alaska"
        );
    }

    @Test
    public void shouldUrlEncodeString() {
        assertThat(request.encode("{}")).isEqualTo("%7B%7D");
    }


    @Test
    public void izRedirectUriShouldMatchProperURIPrefix() {
        assertThat(request.izRedirectUri("tenduke://callback?a=b")).isTrue();
    }

    @Test
    public void izRedirectUriShouldNotMatchInvalidURIPrefix() {
        assertThat(request.izRedirectUri("tenduke://callbakc")).isFalse();
    }

    @Test
    public void buildQueryShouldBuildQueryWithAllGivenParameters() throws MalformedURLException{
        assertThat(new URL("http://localhost?" + request.buildQuery()))
                .hasParameter("client_id", "unit-test")
                .hasParameter("redirect_uri", "tenduke://callback")
                .hasParameter("response_type", "fake")
                .hasParameter("state", "alaska")
                .hasParameter("scope", "http://a.a/ http://b.b/")
                .hasParameter("hello:", "world!");
    }

    @Test
    public void buildQueryShouldAddScopesOnlyIfTheyArePresent() throws MalformedURLException{
        assertThat(new URL("http://localhost?" + new OAuthRequest<>(config, Map.of(), "fake", Set.of(), "alaska").buildQuery()))
                .hasNoParameter("scope");
    }

    @Test
    public void toUrlShoulBuildRequestUrl() {
        assertThat(request.toUrl())
                .hasProtocol("https")
                .hasHost("example.com")
                .hasPath("/oauth")
                .hasParameter("client_id", "unit-test")
                .hasParameter("redirect_uri", "tenduke://callback")
                .hasParameter("response_type", "fake")
                .hasParameter("state", "alaska")
                .hasParameter("scope", "http://a.a/ http://b.b/")
                .hasParameter("hello:", "world!");
    }

    @Test
    public void toUrlShouldOnlyAppendQueryStringIfQueryStringHasValue() {
        assertThat(new Fake(config).toUrl().toString()).isEqualTo("https://example.com/oauth");
    }

    @Test
    public void createUrlFromlShouldWrapMalformedURLExceptionToIllegalArgumentException() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new Fake2(config).createUrlFrom("ASDASD://ASDSAD");
        });
    }

    @Test
    public void gimmeParameterShouldReturnNamedParameter() {
        assertThat(request.gimmeParameter("hello:")).isEqualTo("world!");
    }

    @Test
    public void gimmeParameterShouldReturnNullIfNoSuchParameter() {
        assertThat(request.gimmeParameter("hello?")).isNull();
    }

    private static class Fake extends OAuthRequest<OAuthConfig>
    {

        public Fake(final OAuthConfig config) {
            super(config, Map.of(), "fake", Set.of(), "alaska");
        }

        @Override
        protected StringBuilder buildQuery() {
            return new StringBuilder();
        }
    }

    private static class Fake2 extends OAuthRequest<OAuthConfig>
    {

        public Fake2(final OAuthConfig config) {
            super(config, Map.of(), "fake", Set.of(), "alaska");
        }

        @Override
        protected StringBuilder buildQuery() {
            return new StringBuilder("{}");
        }
    }



}
