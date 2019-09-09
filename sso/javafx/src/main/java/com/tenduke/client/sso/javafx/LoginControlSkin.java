/*
 * Copyright (c) 2019 10Duke. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * https://opensource.org/licenses/MIT
 *
 */
package com.tenduke.client.sso.javafx;

import javafx.scene.control.SkinBase;
import javafx.scene.web.WebView;

/**
 * Default skin for {@link LoginControl}.
 *
 */
public class LoginControlSkin extends SkinBase<LoginControl> {

    /** The wrapped WebView. */
    private final WebView view;

    /**
     * Constructs new instance.
     *
     * @param control -
     * @param view -
     */
    public LoginControlSkin(final LoginControl control, final WebView view) {
        super(control);

        this.view = view;

        getChildren().add(view);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected double computePrefHeight(final double d, final double d1, final double d2, final double d3, final double d4) {
        return view.getPrefHeight();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected double computePrefWidth(final double d, final double d1, final double d2, final double d3, final double d4) {
        return view.getPrefWidth();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected double computeMaxHeight(final double d, final double d1, final double d2, final double d3, final double d4) {
        return view.getMaxHeight();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected double computeMaxWidth(final double d, final double d1, final double d2, final double d3, final double d4) {
        return view.getMaxWidth();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected double computeMinHeight(final double d, final double d1, final double d2, final double d3, final double d4) {
        return view.getMinHeight();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    protected double computeMinWidth(final double d, final double d1, final double d2, final double d3, final double d4) {
        return view.getMinWidth();
    }

}
