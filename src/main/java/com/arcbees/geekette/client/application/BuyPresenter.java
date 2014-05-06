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

import com.arcbees.geekette.client.RestCallbackImpl;
import com.arcbees.geekette.client.api.BuyNowService;
import com.arcbees.geekette.client.application.BuyPresenter.MyView;
import com.arcbees.geekette.shared.CreditCardInfo;
import com.arcbees.stripe.client.CreditCard;
import com.arcbees.stripe.client.ResponseHandler;
import com.arcbees.stripe.client.Stripe;
import com.arcbees.stripe.client.jso.Response;
import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.shared.RestDispatch;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;

public class BuyPresenter extends PresenterWidget<MyView> implements BuyUiHandlers {
    interface MyView extends PopupView, HasUiHandlers<BuyUiHandlers> {
        void enableBuyNow();
    }

    private static final String STRIPE_PUBLIC_KEY = "pk_test_K9Oer0kZTz5qqJMxyCNSoIhr";

    private final Stripe stripe;
    private final RestDispatch dispatcher;
    private final BuyNowService buyNowService;
    private final ThanksPresenter thanksPresenter;

    @Inject
    BuyPresenter(
            EventBus eventBus,
            MyView view,
            Stripe stripe,
            RestDispatch dispatcher,
            BuyNowService buyNowService,
            ThanksPresenter thanksPresenter) {
        super(eventBus, view);

        this.stripe = stripe;
        this.dispatcher = dispatcher;
        this.buyNowService = buyNowService;
        this.thanksPresenter = thanksPresenter;

        getView().setUiHandlers(this);
    }

    @Override
    public void onBuyNow(final CreditCardInfo creditCardInfo) {
        CreditCard creditCard = CreditCard.with()
                .creditCardNumber(creditCardInfo.getCreditCard())
                .cvc(creditCardInfo.getCvc())
                .expirationMonth(creditCardInfo.getMonth())
                .expirationYear(creditCardInfo.getYear())
                .name(creditCardInfo.getName())
                .build();

        stripe.getStripeToken(creditCard, new ResponseHandler() {
            @Override
            public void onTokenReceived(int i, Response response) {
 //               if (STATUS_CODE_OK == i) {
                    creditCardInfo.setAuthKey(response.getId());

                    dispatcher.execute(buyNowService.buyNow(creditCardInfo), new RestCallbackImpl<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            getView().hide();

                            RevealRootPopupContentEvent.fire(BuyPresenter.this, thanksPresenter);
                        }
                    });
 //               }
            }
        });
    }

    @Override
    protected void onBind() {
        super.onBind();

        stripe.inject(new Callback<Void, Exception>() {
            @Override
            public void onFailure(Exception e) {
                Window.alert("Failed to inject stripe");
            }

            @Override
            public void onSuccess(Void aVoid) {
                onStripeInjected();
            }
        });
    }

    private void onStripeInjected() {
        getView().enableBuyNow();

        stripe.setPublishableKey(STRIPE_PUBLIC_KEY);
    }
}
