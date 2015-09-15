/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Client Java.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.caratarse.auth.client.apiversion0100;

import java.io.IOException;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.caratarse.auth.client.ApiResponse;
import org.caratarse.auth.client.CaratarseAuthClientFactory;

/**
 * Implementation of the Caratarse-auth client for the 1.0 version.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class CaratarseAuthClient0100Impl implements CaratarseAuthClient0100 {

    private static final String version = "1.0";
    private String baseUrl;
    private String defaultFormat = CaratarseAuthClientFactory.DEFAULT_FORMAT;

    public CaratarseAuthClient0100Impl() {
        this(CaratarseAuthClientFactory.DEFAULT_BASE_URL);
    }

    public CaratarseAuthClient0100Impl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public CaratarseAuthClient0100Impl(Properties properties) {
        this(properties.getProperty(
                "caratarse.auth.baseUrl", CaratarseAuthClientFactory.DEFAULT_BASE_URL));
        this.defaultFormat = properties.getProperty(
                "caratarse.auth.defaultFormat", CaratarseAuthClientFactory.DEFAULT_FORMAT);
    }

    @Override
    public String getApiVersion() {
        return version;
    }

    @Override
    public ApiResponse getUser(String userUuid) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(this.baseUrl + "/users/" + userUuid + this.defaultFormat);
        final HttpResponse response = httpClient.execute(getRequest);
        return createDefaultResponse(response, 200);
    }

    @Override
    public ApiResponse getUserByUsername(String username) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(this.baseUrl + "/users" + this.defaultFormat
                + "?filter=username:=:" + username);
        final HttpResponse response = httpClient.execute(getRequest);
        return createDefaultResponse(response, 200);
    }

    private ApiResponse createDefaultResponse(final HttpResponse response,
            int... expectedStatusCodes) throws IllegalStateException, IOException {
        boolean found = false;
        final int statusCode = response.getStatusLine().getStatusCode();
        for (int expectedStatusCode : expectedStatusCodes) {
            if (expectedStatusCode == statusCode) {
                found = true;
                break;
            }
        }
        if (found) {
            return createResponse(response, "OK", "");
        } else {
            return createResponse(response, null, null);
        }
    }

    private static ApiResponse createResponse(final HttpResponse response, String apiCode,
            String apiDescription) throws IllegalStateException, IOException {
        ApiResponse result;
        if (apiCode == null) {
            result = new ApiResponse(Integer.toString(response.getStatusLine().getStatusCode()),
                    response.getStatusLine().getReasonPhrase(), response.getEntity().getContent());
        } else {
            result = new ApiResponse(apiCode, apiDescription, response.getEntity().getContent());
        }
        return result;
    }
    
}
