/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import com.tenduke.client.openid.OpenIdAuthorizationCodeResponse;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Event emitted after successful login.
 *
 */
public class LoginSuccessEvent extends LoginEvent {

    private static final long serialVersionUID = 1L;

    /** Event type indicating login success. */
    public static final EventType<LoginSuccessEvent> LOGIN_SUCCESS = new EventType<>(LOGIN_ALL, "XD_LOGIN_SUCCESS");

    /** The response from the authorization server. */
    private final OpenIdAuthorizationCodeResponse response;

    /**
     * Constructs new instance.
     *
     * @param source -
     * @param target -
     * @param response -
     */
    public LoginSuccessEvent(
            final Object source,
            final EventTarget target,
            final OpenIdAuthorizationCodeResponse response
    ) {
        super(source, target, LOGIN_SUCCESS);
        this.response = response;
    }

    /**
     * Returns the response from the authorization server.
     *
     * @return -
     */
    public OpenIdAuthorizationCodeResponse getResponse() {
        return response;
    }

}
