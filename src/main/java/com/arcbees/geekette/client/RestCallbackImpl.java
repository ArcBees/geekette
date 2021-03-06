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

import com.google.gwt.http.client.Response;
import com.gwtplatform.dispatch.rest.shared.RestCallback;

public abstract class RestCallbackImpl<T> implements RestCallback<T> {
    private Response response;

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public void onFailure(Throwable throwable) {
        onError(response);
    }

    public void onError(Response response) {
    }
}
