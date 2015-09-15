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
package org.caratarse.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.RestExpress;
import org.caratarse.auth.services.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaratarseAuthClient0100Test {

    /**
     * The REST server that handles the test calls.
     */
    private static RestExpress server;
    private HttpClient httpClient;
    private static final String BASE_URL = "http://localhost:8081";
    final Logger log = LoggerFactory.getLogger(CaratarseAuthClient0100Test.class);


    @BeforeClass
    public static void beforeClass() throws Exception {
        String[] env = { "dev" };
        server = Main.initializeServer(env);
        Thread.sleep(2000L);
    }

    @AfterClass
    public static void afterClass() throws InterruptedException {
        server.shutdown();
        Thread.sleep(5000L);
    }

    @Before
    public void beforeEach() throws IOException {
        httpClient = new DefaultHttpClient();
        HttpDelete deleteRequest = new HttpDelete(BASE_URL + "/populates.json");
        HttpResponse response = httpClient.execute(deleteRequest);        
        assertEquals(204, response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());
        HttpGet getRequest = new HttpGet(BASE_URL + "/populates.json");
        response = httpClient.execute(getRequest);
        assertEquals(200, response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());
    }


    @After
    public void afterEach() throws IOException {
        httpClient = null;
    }

    private JsonNode toTree(String json) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode userTree = m.readTree(json);
        return userTree;
    }

    private JsonNode toTree(InputStream json) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode userTree = m.readTree(json);
        return userTree;
    }
    
    @Test
    public void getUser() throws IOException {
        CaratarseAuthClient client = CaratarseAuthClientFactory.getClient();
        final String userUuid = "a1ab82a6-c8ce-4723-8532-777c4b05d03c";
        ApiResponse response = client.getUser(userUuid);
        assertEquals("OK", response.getCode());
        JsonNode userTree = toTree(response.getContent());
        log.debug(userTree.toString());
        assertEquals(userUuid, userTree.get("uuid").asText());
    }

    @Test
    public void getUserByUsername() throws IOException {
        CaratarseAuthClient client = CaratarseAuthClientFactory.getClient();
        final String username = "lucio";
        ApiResponse response = client.getUserByUsername(username);
        assertEquals("OK", response.getCode());
        JsonNode rootTree = toTree(response.getContent());
        log.debug(rootTree.toString());
        JsonNode userTree = rootTree.get("_embedded").get("users").get(0);
        assertEquals(username, userTree.get("username").asText());
    }

    @Test
    public void getNotExistentUser() throws IOException {
        CaratarseAuthClient client = CaratarseAuthClientFactory.getClient();
        final String userUuid = "never-use-this-uuid";
        ApiResponse response = client.getUser(userUuid);
        assertEquals("404", response.getCode());
        log.debug(response.asText());
    }

}
