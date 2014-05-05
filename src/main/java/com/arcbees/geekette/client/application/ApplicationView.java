/**
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.geekette.client.application;

import javax.inject.Inject;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.EasingCurve;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ApplicationView extends ViewWithUiHandlers<ApplicationUiHandlers> implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    private class ScrollTopAnimation extends Animation {
        private Element element;
        private int start;

        private ScrollTopAnimation(Element element) {
            this.element = element;
        }

        @Override
        protected void onStart() {
            start = element.getScrollTop();
            super.onStart();
        }

        @Override
        protected void onUpdate(double progress) {
            double value = start - start * progress;
            element.setScrollTop((int) value);
        }

        @Override
        protected double interpolate(double progress) {
            return EasingCurve.easeInOut.interpolate(progress);
        }
    }

    private static int ANIMATION_DURATION = 400;

    @Inject
    ApplicationView(
            Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @UiHandler("buy")
    void onBuy(ClickEvent event) {
        getUiHandlers().onBuy();
    }

    @UiHandler("backToTop")
    void onBackToTopClicked(ClickEvent e) {
        GQuery.$("html, body").each(new Function() {
            @Override
            public void f(Element element) {
                Animation animation = new ScrollTopAnimation(element);
                animation.run(ANIMATION_DURATION);
            }
        });
    }
}
