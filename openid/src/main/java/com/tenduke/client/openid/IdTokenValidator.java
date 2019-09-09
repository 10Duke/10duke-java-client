/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.openid;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Validates an Id-token.
 *
 */
public class IdTokenValidator {

    /** Expected client id. */
    private final String clientId;

    /** Clock for current timestamp. */
    private final Clock clock;

    /** Expected issuer. */
    private final String expectedIssuer;

    /**
     * Constructs new instance.
     *
     * @param clientId -
     * @param expectedIssuer -
     */
    public IdTokenValidator(
            final String clientId,
            final String expectedIssuer
    ) {
        this(
                clientId,
                expectedIssuer,
                Clock.systemUTC()
        );
    }

    /**
     * Constructs new instance.
     *
     * @param clientId -
     * @param expectedIssuer -
     * @param clock -
     */
    public IdTokenValidator(
            final String clientId,
            final String expectedIssuer,
            final Clock clock
    ) {
        this.clientId = clientId;
        this.clock = clock;
        this.expectedIssuer = expectedIssuer;
    }


    /** Validates id token as per OpenId Connect spec, section 3.1.3.7: ID Token Validation.
     *
     * @param token -
     * @param rejectTokensIssuedBeforeThis -
     * @param nonce -
     * @throws IdTokenException -
     */
    public void validate(
            final IdToken token,
            @Nullable final Instant rejectTokensIssuedBeforeThis,
            @Nullable final String nonce
    ) throws IdTokenException {
        // UNSUPPORTED: 1. If the ID Token is encrypted, decrypt it using the keys and algorithms that the Client specified during
        // Registration that the OP was to use to encrypt the ID Token. If encryption was negotiated with the OP at Registration time and
        // the ID Token is not encrypted, the RP SHOULD reject it.

        // 2. The Issuer Identifier for the OpenID Provider (which is typically obtained during Discovery) MUST exactly match the value of
        // the iss (issuer) Claim.
        if (!expectedIssuer.equals(token.getIss())) {
            throw new IdTokenException(String.format("Issuer does not match: expected %s, got: %s", expectedIssuer, token.getIss()));
        }

        // 3. The Client MUST validate that the aud (audience) Claim contains its client_id value registered at the Issuer identified by the
        // iss (issuer) Claim as an audience. The aud (audience) Claim MAY contain an array with more than one element. The ID Token MUST be
        // rejected if the ID Token does not list the Client as a valid audience, or if it contains additional audiences not trusted by the
        // Client.
        final List<String> audiences = token.getAud();
        if (!audiences.contains(clientId)) {
            throw new IdTokenException(String.format("Claim \"aud\" does not contain client id", expectedIssuer, token.getIss()));
        }

        // 4. If the ID Token contains multiple audiences, the Client SHOULD verify that an azp Claim is present.
        @Nullable final String authorizedParty = token.getAzp();

        if (audiences.size() > 1 && (authorizedParty == null || authorizedParty.isEmpty())) {
            throw new IdTokenException("ID token contains multiple audiences, but claim \"azp\" not present");
        }

        // 5. If an azp (authorized party) Claim is present, the Client SHOULD verify that its client_id is the Claim Value.
        if (authorizedParty != null && !authorizedParty.equals(clientId)) {
            throw new IdTokenException(String.format("Invalid authorized party (\"azp\"): Expected %s, got %s", clientId, authorizedParty));
        }

        // ALREADY DONE: 6. If the ID Token is received via direct communication between the Client and the Token Endpoint (which it is in
        // this flow), the TLS server validation MAY be used to validate the issuer in place of checking the token signature. The Client
        // MUST validate the signature of all other ID Tokens according to JWS [JWS] using the algorithm specified in the JWT alg Header
        // Parameter. The Client MUST use the keys provided by the Issuer.

        // 7. The alg value SHOULD be the default of RS256 or the algorithm sent by the Client in the id_token_signed_response_alg parameter
        // during Registration.

        // 8. If the JWT alg Header Parameter uses a MAC based algorithm such as HS256, HS384, or HS512, the octets of the UTF-8
        // representation of the client_secret corresponding to the client_id contained in the aud (audience) Claim are used as the key to
        // validate the signature. For MAC based algorithms, the behavior is unspecified if the aud is multi-valued or if an azp value is
        // present that is different than the aud value.

        // 9. The current time MUST be before the time represented by the exp Claim.
        final Instant currentTime = clock.instant();

        if (currentTime.isAfter(token.getExp())) {
            throw new IdTokenException(String.format("Token expired"));
        }

        // 9. The iat Claim can be used to reject tokens that were issued too far away from the current time, limiting the amount of time
        // that nonces need to be stored to prevent attacks. The acceptable range is Client specific.
        if (rejectTokensIssuedBeforeThis != null && token.getIat().isBefore(rejectTokensIssuedBeforeThis)) {
            throw new IdTokenException(String.format("Token too old"));
        }

        // 10. If a nonce value was sent in the Authentication Request, a nonce Claim MUST be present and its value checked to verify that
        // it is the same value as the one that was sent in the Authentication Request. The Client SHOULD check the nonce value for replay
        // attacks. The precise method for detecting replay attacks is Client specific.
        if (nonce != null && !nonce.equals(token.getNonce())) {
            throw new IdTokenException(String.format("Nonce does not match!"));
        }

        // CALLER VERIFIES: 11. If the acr Claim was requested, the Client SHOULD check that the asserted Claim Value is appropriate. The
        // meaning and processing of acr Claim Values is out of scope for this specification.

        // CALLER VERIFIES: 12. If the auth_time Claim was requested, either through a specific request for this Claim or by using the
        // max_age parameter, the Client SHOULD check the auth_time Claim value and request re-authentication if it determines too much time
        // has elapsed since the last End-User authentication.
    }
}
