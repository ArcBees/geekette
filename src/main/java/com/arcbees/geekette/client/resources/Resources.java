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

package com.arcbees.geekette.client.resources;

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
    public interface Styles extends CssResource {
        String height();

        String clearfix();

        String section();

        String bubble();

        String wrapper();

        String wrapperBubble();

        String txtcenter();

        String logo();

        String price();

        String title();

        String date();

        String place();

        String ticket();

        String partager();

        String share();

        String tt();

        String tooltip();

        String bubbleWhite();

        String logoCrown();

        String txtPink();

        String flip3D();

        String front();

        String back();

        String dict();

        String bubbleArcbees();

        String left();

        String logoArcbees();

        String moveFastest();

        String moveFaster();

        String moveFast();

        String moveMed();

        String moveMed2();

        String moveMed3();

        String moveSlow();

        String moveSlower();

        String moveSlowest();

        String bubble1();

        String bubble2();

        String bubble3();

        String bubble4();

        String bubble5();

        String bubble6();

        String bubble7();

        String bubble8();

        String bubble9();

        String backTop();

        String popup();

        String popupWrapper();

        String popupPrice();

        String popupCost();

        String popupTotal();

        String nbTickets();

        String numCarte();

        String small();

        String right();

        String error();

        String bgPink();

        String annuler();

        String iconBomb();
    }

    @Source("styles.css")
    Styles styles();

    ImageResource logo();

    ImageResource logoCrown();

    ImageResource backTop();

    ImageResource iconBomb();

    ImageResource bgError();

    SVGResource bubble1();

    SVGResource bubble2();

    SVGResource bubble3();

    SVGResource bubble4();

    SVGResource bubble5();

    SVGResource bubble6();

    SVGResource bubble7();

    SVGResource bubble8();

    SVGResource bubble9();

    SVGResource logoArcbees();

    SVGResource iconFb();

    SVGResource iconLkin();

    SVGResource iconTwit();

    SVGResource iconGplus();

    @Source("baron/baron_neue-webfont.eot")
    DataResource baronNeueEot();

    @Source("baron/baron_neue-webfont.woff")
    DataResource baronNeueWoff();

    @Source("baron/baron_neue-webfont.svg")
    DataResource baronNeueSvg();

    @Source("baron/baron_neue-webfont.ttf")
    DataResource baronNeueTtf();

    @Source("baron/baron_neue_black-webfont.eot")
    DataResource baronNeueBlackEot();

    @Source("baron/baron_neue_black-webfont.ttf")
    DataResource baronNeueBlackTtf();

    @Source("baron/baron_neue_black-webfont.svg")
    DataResource baronNeueBlackSvg();

    @Source("baron/baron_neue_black-webfont.woff")
    DataResource baronNeueBlackWoff();

    @Source("brandongrotesque/brandon_bld-webfont.eot")
    DataResource brandonGrotesqueBoldEot();

    @Source("brandongrotesque/brandon_bld-webfont.svg")
    DataResource brandonGrotesqueBoldSvg();

    @Source("brandongrotesque/brandon_bld-webfont.ttf")
    DataResource brandonGrotesqueBoldTtf();

    @Source("brandongrotesque/brandon_bld-webfont.woff")
    DataResource brandonGrotesqueBoldWoff();

    @Source("brandongrotesque/brandon_light-webfont.eot")
    DataResource brandonGrotesqueLightEot();

    @Source("brandongrotesque/brandon_light-webfont.svg")
    DataResource brandonGrotesqueLightSvg();

    @Source("brandongrotesque/brandon_light-webfont.ttf")
    DataResource brandonGrotesqueLightTtf();

    @Source("brandongrotesque/brandon_light-webfont.woff")
    DataResource brandonGrotesqueLightWoff();

    @Source("brandongrotesque/brandon_reg-webfont.eot")
    DataResource brandonGrotesqueRegEot();

    @Source("brandongrotesque/brandon_reg-webfont.svg")
    DataResource brandonGrotesqueRegSvg();

    @Source("brandongrotesque/brandon_reg-webfont.ttf")
    DataResource brandonGrotesqueRegTtf();

    @Source("brandongrotesque/brandon_reg-webfont.woff")
    DataResource brandonGrotesqueRegWoff();
}
