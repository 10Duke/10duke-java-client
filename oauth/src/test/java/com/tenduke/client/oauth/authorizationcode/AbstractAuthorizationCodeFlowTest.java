/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.oauth.authorizationcode;

import com.tenduke.client.json.JsonDeserializer;
import com.tenduke.client.oauth.exceptions.OAuthErrorException;
import com.tenduke.client.oauth.exceptions.OAuthException;
import com.tenduke.client.oauth.exceptions.OAuthSecurityException;
import com.tenduke.client.oauth.exceptions.OAuthServerException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import org.junit.Before;
import org.junit.Test;

public class AbstractAuthorizationCodeFlowTest {

    private AuthorizationCodeConfig config;
    private HttpClient http;
    private JsonDeserializer json;
    private AuthorizationCodeRequest request;
    private Flow flow;

    @Before
    public void beforeTest() {
        config = new AuthorizationCodeConfig(
                "unit-test",
                URI.create("https://example.com/oauth"),
                URI.create("tenduke://callback"),
                URI.create("https://example.com/token"),
                "1-2-3-4-5"
        );

        request = new AuthorizationCodeRequest(config, Map.of("hello", "world"), new LinkedHashSet<>(List.of("a", "b")), "alaska");

        flow = new Flow(request);
    }


    @Test
    public void shouldExtractRawQueryFromURIasResponse() {
        assertThat(flow.extractResponse(URI.create("http://localhost/?a=%7B%7D"))).isEqualTo("a=%7B%7D");
    }
/*
    @Test
    public void processShouldHandleRedirectUriAndExchangeAuthorizationCodeToToken() throws Exception {
        final AuthorizationCodeResponse response = new AuthorizationCodeResponse("a-t", "r-t", 42, "t-t");

        stubHttp(http, "POST", "https://example.com/token", 200, "SIMULATED RESPONSE");

        when(json.deserialize("SIMULATED RESPONSE", AuthorizationCodeResponse.class)).thenReturn(response);

        assertThat(flow.process("tenduke://callback/?code=open-sesame&state=alaska")).isSameAs(response);
    }
*/
    @Test
    public void processShouldReturnNullIfNotRedirectUri() throws Exception {
        assertThat(flow.process("10duke://callback/?code=open-sesame&state=alaska")).isNull();
    }

    @Test
    public void processShouldThrowOAuthSecurityExceptionIfStateDoesNotMatch() throws Exception {
        assertThatExceptionOfType(OAuthSecurityException.class).isThrownBy(() -> {
            flow.process("tenduke://callback/?code=open-sesame&state=florida");
        });
    }

    @Test
    public void processShouldThrowOAuthErrorExceptionIfResponseContainsErrorParameter() throws Exception {
        assertThatExceptionOfType(OAuthErrorException.class).isThrownBy(() -> {
            flow.process("tenduke://callback/?error=simulated-error&state=alaska");
        }).matches(e -> e.getError().getError().equals("simulated-error"));
    }

    @Test
    public void processShouldThrowOAuthServerExceptionIfResponseDoesNotContainCodeOrError() throws Exception {
        assertThatExceptionOfType(OAuthServerException.class).isThrownBy(() -> {
            flow.process("tenduke://callback/?state=alaska");
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Class implementation">

    private static class Flow extends AbstractAuthorizationCodeFlow<
            AuthorizationCodeConfig,
            AuthorizationCodeRequest,
            AuthorizationCodeResponse
    > {
        public Flow(
                final AuthorizationCodeRequest request
        ) {
            super(request);
        }

        @Override
        protected AuthorizationCodeResponse exchangeCodeToToken(final String authorizationCode) throws InterruptedException, OAuthException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }


    }

    // </editor-fold>

}
