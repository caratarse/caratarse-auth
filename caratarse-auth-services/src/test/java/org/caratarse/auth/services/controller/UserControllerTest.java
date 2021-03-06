/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Services.
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
package org.caratarse.auth.services.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.IOUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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

public class UserControllerTest {

    /**
     * The REST server that handles the test calls.
     */
    private static RestExpress server;
    private HttpClient httpClient;
    private static final String BASE_URL = "http://localhost:8081";
    final Logger log = LoggerFactory.getLogger(UserControllerTest.class);


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

    @Test
    public void postDirectiveReplayRequest() throws IOException {
        HttpGet getRequest = new HttpGet(BASE_URL + "/users.json");
        final HttpResponse response = httpClient.execute(getRequest);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }
    
    @Test
    public void getUser() throws IOException {
        HttpGet getRequest = new HttpGet(BASE_URL + "/users/a1ab82a6-c8ce-4723-8532-777c4b05d03c.json");
        final HttpResponse response = httpClient.execute(getRequest);
        assertEquals(200, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }

    @Test
    public void getUserByUsername() throws IOException {
        HttpGet getRequest = new HttpGet(BASE_URL + "/users?filter=username:=:lucio");
        final HttpResponse response = httpClient.execute(getRequest);
        assertEquals(200, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }

    @Test
    public void getUsersPaginated() throws IOException {
        HttpGet getRequest = new HttpGet(BASE_URL + "/users?offset=0&limit=1");
        final HttpResponse response = httpClient.execute(getRequest);
        assertEquals(206, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }
    
    @Test
    public void deleteUser() throws IOException {
        HttpDelete deleteRequest = new HttpDelete(BASE_URL + "/users/a1ab82a6-c8ce-4723-8532-777c4b05d03c");
        HttpResponse response = httpClient.execute(deleteRequest);
        assertEquals(204, response.getStatusLine().getStatusCode());
        HttpGet getRequest = new HttpGet(BASE_URL + "/users/a1ab82a6-c8ce-4723-8532-777c4b05d03c.json");
        response = httpClient.execute(getRequest);
        assertEquals(404, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }
    
    @Test
    public void deleteNotExistentUser() throws IOException {
        HttpDelete deleteRequest = new HttpDelete(BASE_URL + "/users/a-not-existent-user");
        HttpResponse response = httpClient.execute(deleteRequest);
        assertEquals(500, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }
 
    @Test
    public void createUser() throws UnsupportedEncodingException, IOException {
        HttpPost postRequest = new HttpPost(BASE_URL + "/users");
        StringEntity input = new StringEntity("{\"username\":\"carlo\",\"password\":\"carloPwd\"}");
        input.setContentType("application/json");
	postRequest.setEntity(input);
        HttpResponse response = httpClient.execute(postRequest);
        assertEquals(201, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }
    
    @Test
    public void updateUser() throws IOException {
        HttpPut putRequest = new HttpPut(BASE_URL + "/users/a1ab82a6-c8ce-4723-8532-777c4b05d03c");
        StringEntity input = new StringEntity("{\"username\":\"updUsername\",\"password\":\"updPwd\"}");
        input.setContentType("application/json");
	putRequest.setEntity(input);
        HttpResponse response = httpClient.execute(putRequest);
        assertEquals(204, response.getStatusLine().getStatusCode());
        HttpGet getRequest = new HttpGet(BASE_URL + "/users/a1ab82a6-c8ce-4723-8532-777c4b05d03c.json");
        response = httpClient.execute(getRequest);
        assertEquals(200, response.getStatusLine().getStatusCode());
        log.debug(IOUtils.toString(response.getEntity().getContent()));
    }
}
