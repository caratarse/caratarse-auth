/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth support for CAS.
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
package org.caratarse.auth.cas.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.caratarse.auth.client.ApiResponse;
import org.caratarse.auth.client.CaratarseAuthClient;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadPasswordAuthenticationException;
import org.jasig.cas.authentication.handler.BlockedCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.UnknownUsernameAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.beans.factory.InitializingBean;

/**
 * An authentication handler that uses the Caratarse-auth services.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class CaratarseAuthClientAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler implements InitializingBean {

    private CaratarseAuthClient caratarseAuthClient;
    private boolean withSalt = true;

    protected final boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials) throws AuthenticationException {
        final String transformedUsername = getPrincipalNameTransformer().transform(credentials.getUsername());
        final String encryptedPassword = getPasswordEncoder().encode(
                credentials.getPassword() + (isWithSalt()?"{" + transformedUsername + "}":""));
        
        ApiResponse response;
        JsonNode userTree;
        try {
            response = caratarseAuthClient.getUserByUsername(transformedUsername);
            
            if ("404".equals(response.getCode())) {
                throw new UnknownUsernameAuthenticationException();
            }
            
            ObjectMapper m = new ObjectMapper();
            JsonNode rootTree = m.readTree(response.getContent());
            userTree = rootTree.get("_embedded").get("users").get(0);
        } catch (Exception ex) {
            throw new AuthenticationException("Authentication failed for user "+transformedUsername, ex) {};
        }

        if (!userIsEnabled(userTree)) {
            throw new BlockedCredentialsAuthenticationException();
        }

        if (!passwordMatches(userTree, encryptedPassword)) {
            throw new BadPasswordAuthenticationException();
        }

        // update last login

        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public CaratarseAuthClient getCaratarseAuthClient() {
        return caratarseAuthClient;
    }

    public void setCaratarseAuthClient(CaratarseAuthClient caratarseAuthClient) {
        this.caratarseAuthClient = caratarseAuthClient;
    }

    public boolean isWithSalt() {
        return withSalt;
    }

    public void setWithSalt(boolean withSalt) {
        this.withSalt = withSalt;
    }

    private boolean userIsEnabled(JsonNode userTree) {
        return userTree.get("disabled") == null;
    }

    private boolean passwordMatches(JsonNode userTree, String encryptedPassword) {
        return encryptedPassword.equals(userTree.get("password").asText());
    }
    
}