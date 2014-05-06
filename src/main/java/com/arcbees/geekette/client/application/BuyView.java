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

import com.arcbees.geekette.client.application.BuyPresenter.MyView;
import com.arcbees.geekette.shared.CreditCardInfo;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class BuyView extends PopupViewWithUiHandlers<BuyUiHandlers> implements MyView, Editor<CreditCardInfo> {
    private final Driver driver;

    interface Binder extends UiBinder<Widget, BuyView> {
    }

    interface Driver extends SimpleBeanEditorDriver<CreditCardInfo, BuyView> {
    }

    @UiField
    Button buyNow;
    @UiField
    InlineLabel total;
    @UiField
    TextBox name;
    @UiField
    TextBox email;
    @UiField
    TextBox creditCard;
    @UiField
    IntegerBox month;
    @UiField
    IntegerBox year;
    @UiField
    TextBox cvc;

    @Ignore
    @UiField
    IntegerBox numberOfTickets;

    @Inject
    BuyView(
            Binder binder,
            Driver driver,
            EventBus eventBus) {
        super(eventBus);

        this.driver = driver;

        initWidget(binder.createAndBindUi(this));
        driver.initialize(this);
        driver.edit(new CreditCardInfo());

        $(name).attr("placeholder", "Votre charmant NOM");
        $(email).attr("placeholder", "Votre courriel");
        $(year).attr("placeholder", "aaaa");
        $(month).attr("placeholder", "mm");

        name.getElement().setAttribute("placeholder", "Votre charmant NOM");
    }

    @Override
    public void enableBuyNow() {
        buyNow.setEnabled(true);
    }

    @UiHandler("buyNow")
    void onBuyNow(ClickEvent event) {
        CreditCardInfo creditCardInfo = driver.flush();

        getUiHandlers().onBuyNow(creditCardInfo);
    }

    @UiHandler("numberOfTickets")
    void onNumberOfTickets(BlurEvent event) {
        String totalString = String.valueOf(numberOfTickets.getValue() * 55);

        total.setText(totalString + "$");
    }
}
