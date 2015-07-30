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


import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.builder.TokenBinder;
import com.strategicgains.hyperexpress.builder.TokenResolver;
import com.strategicgains.hyperexpress.builder.UrlBuilder;
import com.strategicgains.syntaxe.ValidationException;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.caratarse.auth.model.bo.ServiceBo;
import org.caratarse.auth.model.bo.UserBo;
import org.caratarse.auth.model.bo.UserServiceBo;
import org.caratarse.auth.model.dao.ServiceDao;
import org.caratarse.auth.model.dao.UserServiceDao;
import org.caratarse.auth.model.po.Service;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserService;
import org.caratarse.auth.services.Constants;
import org.caratarse.auth.services.util.HyperExpressBindHelper;
import org.lambico.dao.generic.Page;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.restexpress.query.QueryFilters;
import org.restexpress.query.QueryOrders;
import org.restexpress.query.QueryRanges;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceController {

    private static final UrlBuilder LOCATION_BUILDER = new UrlBuilder();
    @Resource
    private UserServiceDao userServiceDao;
    @Resource
    private ServiceDao serviceDao;
    @Resource
    private UserBo userBo;
    @Resource
    private UserServiceBo userServiceBo;
    @Resource
    private ServiceBo serviceBo;

    public UserServiceController() {
    }

    public UserService addServiceToUser(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME, "No Service Name supplied");
        
        validateAndThrow(userUuid, serviceName);

        UserService userService = userServiceBo.addServiceToUser(userUuid, serviceName);

        // Construct the response for create...
        response.setResponseCreated();

        TokenResolver resolver = HyperExpress.bind(Constants.Url.USER_UUID, userUuid)
                .bind(Constants.Url.SERVICE_NAME, serviceName);

        // Include the Location header...
        String locationPattern = request.getNamedUrl(HttpMethod.GET,
                Constants.Routes.USER_READ_ROUTE);
        response.addLocationHeader(LOCATION_BUILDER.build(locationPattern, resolver));

        // Return the newly-created item...
        return userService;
    }

    public UserService read(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME, "No UserService name supplied");
        UserService userService = userServiceDao.findByUserUuidAndServiceName(userUuid, serviceName);
        if (userService == null) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
        }

        addTokenBinder();

        return userService;
    }

    @Transactional
    public List<UserService> readAll(Request request, Response response) {
        String uuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        QueryFilter filter = QueryFilters.parseFrom(request);
        QueryOrder order = QueryOrders.parseFrom(request);
        QueryRange range = QueryRanges.parseFrom(request, Constants.DEFAULT_RANGE_LIMIT);
        Page<UserService> entities = userServiceBo.readAllByUser(uuid, filter, range, order);
        response.setCollectionResponse(range, entities.getList().size(), entities.getRowCount());
        HyperExpressBindHelper.bindPaginationTokens(range, entities.getRowCount());
        addTokenBinder();

        return entities.getList();
    }

    private void addTokenBinder() {
        // Bind the resources in the collection with link URL tokens, etc. here...
        HyperExpress.tokenBinder(new TokenBinder<UserService>() {
            @Override
            public void bind(UserService entity, TokenResolver resolver) {
                resolver.bind(Constants.Url.USER_SERVICE_ID, entity.getId().toString())
                        .bind(Constants.Url.SERVICE_NAME, entity.getService().getName())
                        .bind(Constants.Url.USER_UUID, entity.getUser().getUuid())
                        .bind(Constants.Url.USER_ID, entity.getUser().getId().toString());
            }
        });
    }

    public void update(Request request, Response response) {
        //TODO: Your 'PUT' logic here...
        response.setResponseNoContent();
    }

    public void delete(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME, "No UserService name supplied");
        userServiceBo.delete(userUuid, serviceName);
        response.setResponseNoContent();
    }

    private void validateAndThrow(String userUuid, String serviceName) {
        List<String> errors = new ArrayList<>();
        User user = userBo.getUser(userUuid);
        if (user == null) {
            errors.add("User with UUID "+userUuid+" not found.");
        }
        Service service = serviceBo.getService(serviceName);
        if (service == null) {
            errors.add("Service with name "+serviceName+" not found.");
        }
        List<UserService> userServices = userServiceBo.findServicesByUser(userUuid);
        boolean found = false;
        for (UserService userService : userServices) {
            if (userService.getService().getName().equals(serviceName)) {
                found = true;
                break;
            }
        }
        if (found) {
            errors.add("User "+userUuid+" already attached to Service "+serviceName+".");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
