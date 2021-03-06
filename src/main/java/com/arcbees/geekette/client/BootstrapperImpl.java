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

package com.arcbees.geekette.client;

import javax.inject.Inject;

import com.arcbees.geekette.client.resources.Resources;
//import com.google.gwt.dom.client.StyleInjector;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class BootstrapperImpl implements Bootstrapper {
    private final PlaceManager placeManager;

    @Inject
    BootstrapperImpl(
            Resources resources,
            PlaceManager placeManager) {
        this.placeManager = placeManager;

        resources.styles().ensureInjected();
//        StyleInjector.injectAtEnd("@media (max-width: 768px) {"+ resources.mediaQueries().getText()+"}");
    }

    @Override
    public void onBootstrap() {
        placeManager.revealCurrentPlace();
    }
}
