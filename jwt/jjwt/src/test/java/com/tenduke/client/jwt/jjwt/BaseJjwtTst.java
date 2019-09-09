/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.jwt.jjwt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.junit.Before;

public class BaseJjwtTst {

    protected Key verificationKey;

    @Before
    public void setupKey() throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        final byte[] data = Base64.getDecoder().decode(PUBLIC_KEY);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(data);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        verificationKey = keyFactory.generatePublic(keySpec);
    }

    protected static final String VALID_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhIiwiaWF0Ijo0Mn0.e4IdJE1G0wKr1Wv5cdVsTamlJzn9Zbqt1U2jwJ2brL0i2kX64pWGRNRNufj0mQMhEdtyy6Y3JZwlguB4XpYLfqRFl3lgruPyJETV6jMSYvA9Xav_XgQ9p7EwUnubkwEyw0v8x0mr66Dbx3Pz34GZYPOa0kNHSGbQRy6sfYBtqWDTN7IeMtRFKWIe1IcqiYw6KJ0c70KB8Kr21mKHt0bYcZ2yJxifqbRzCl0ZSxq2GlQDPa5fxrBtFBI9Zkb_WT_iJa0GNShajfhoRaurNyX1QVukOhUo1svWLp_4d5E2JOti-oSPFZgKyhoVllSJrjz0CV3o_EpSnbKx1jwUeAAtdw";
    protected static final String PUBLIC_KEY = ""
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzyis1ZjfNB0bBgKFMSv"
            + "vkTtwlvBsaJq7S5wA+kzeVOVpVWwkWdVha4s38XM/pa/yr47av7+z3VTmvDRyAHc"
            + "aT92whREFpLv9cj5lTeJSibyr/Mrm/YtjCZVWgaOYIhwrXwKLqPr/11inWsAkfIy"
            + "tvHWTxZYEcXLgAXFuUuaS3uF9gEiNQwzGTU1v0FqkqTBr4B8nW3HCN47XUu0t8Y0"
            + "e+lf4s4OxQawWD79J9/5d3Ry0vbV3Am1FtGJiJvOwRsIfVChDpYStTcHTCMqtvWb"
            + "V6L11BWkpzGXSW4Hv43qa+GSYOD2QU68Mb59oSk2OB+BtOLpJofmbGEGgvmwyCI9"
            + "MwIDAQAB"
            + "";

}
