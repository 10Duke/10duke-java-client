/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth;

import static com.tenduke.client.oauth.AbstractOAuthFlow.getFirst;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.oauth.exceptions.OAuthSecurityException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;

public class AbstractOAuthFlowTest {

    private OAuthConfig config;
    private Impl flow;
    private OAuthRequest<OAuthConfig> request;

    @Before
    public void beforeTest() {
        config = new OAuthConfig(null, null, URI.create("tenduke://callback"));
        request = new OAuthRequest<>(
                config,
                Collections.emptyMap(),
                "fake",
                Collections.emptySet(),
                "alaska"
        );
        flow = new Impl(request);
    }

    @Test
    public void decodeShouldReturnNullIfProvidedNull() {
        assertThat(flow.decode(null)).isNull();
    }

    @Test
    public void decodeShouldUrlDecodeGivenValue() {
        assertThat(flow.decode("%7B%7D")).isEqualTo("{}");
    }

    @Test
    public void getFirstShouldReturnFirstValueOfMultiMapEntry() {
        assertThat(getFirst(Map.of("hello", List.of("world", "you")), "hello")).isEqualTo("world");
    }

    @Test
    public void getFirstShouldReturnNullIfNoSuchKeyInMap() {
        assertThat(getFirst(Map.of("hello", List.of("world", "you")), "hello!")).isNull();
    }

    @Test
    public void getFirstShouldReturnNullIfKeyExistsButNoValues() {
        assertThat(getFirst(Map.of("hello", List.of()), "hello")).isNull();
    }

    @Test
    public void shouldReturnTheRequest() {
        assertThat(flow.getRequest()).isSameAs(request);
    }

    @Test
    public void parseUriShouldReturnResponseWhenResponseUriIsRedirectUri() throws OAuthServerException {
        assertThat(flow.parseUri("tenduke://callback?hello=world")).containsOnly(
                entry("hello", List.of("world"))
        );
    }

    @Test
    public void parseUriShouldThrowOAuthServerExceptionWhenTheProvidedUriIsRedirectionUriButInvalid() {

        assertThatExceptionOfType(OAuthServerException.class).isThrownBy(() -> {
            flow.parseUri("tenduke://callback?a={}");
        });
    }

    @Test
    public void parseUriShouldReturnNullIfResponseExtractionReturnedNull() throws OAuthServerException {
        flow.extractedResponse = null;

        assertThat(flow.parseUri("tenduke://callback")).isNull();
    }

    @Test
    public void parseUriShouldShouldReturnNullIfProvidedUriIsNotRedirectUri() throws OAuthServerException {
        assertThat(flow.parseUri("invalid://callback")).isNull();
    }

    @Test
    public void verifyStateShouldDoNothingIfResponseHasProperState() throws OAuthSecurityException {
        flow.verifyState(Map.of("state", List.of("alaska")));
    }

    @Test
    public void verifyStateShouldThrowOAuthSecurityIfResponseDoesNotHaveProperState() {
        assertThatExceptionOfType(OAuthSecurityException.class).isThrownBy(() -> {
            flow.verifyState(Map.of("state", List.of("invalid")));
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Class implementation">

    private static class Impl extends AbstractOAuthFlow<OAuthConfig, OAuthRequest<OAuthConfig>, OAuthResponse> {

        public String extractedResponse = "hello=world";

        public Impl(final OAuthRequest<OAuthConfig> request) {
            super(request);
        }

        @Override
        protected String extractResponse(final URI uri) {
            return extractedResponse;
        }

        @Override
        public OAuthResponse process(String uri) throws InterruptedException, OAuthException {
            return null;
        }
    }

    // </editor-fold>

}
