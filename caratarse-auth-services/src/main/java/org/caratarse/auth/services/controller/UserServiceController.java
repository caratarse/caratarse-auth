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
import java.util.List;
import javax.annotation.Resource;
import org.caratarse.auth.model.bo.UserBo;
import org.caratarse.auth.model.dao.ServiceDao;
import org.caratarse.auth.model.dao.UserServiceDao;
import org.caratarse.auth.model.po.UserService;
import org.caratarse.auth.services.Constants;

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

    @Resource
    private UserServiceDao userServiceDao;
    @Resource
    private ServiceDao serviceDao;

    public UserServiceController() {
    }

    public Object create(Request request, Response response) {
        //TODO: Your 'POST' logic here...
        return null;
    }

    public UserService read(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME, "No UserService name supplied");
        UserService userService = userServiceDao.findByUserUuidAndServiceName(userUuid, serviceName);

        addTokenBinder();

        return userService;
    }

    @Transactional
    public List<UserService> readAll(Request request, Response response) {
        String uuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        QueryFilter filter = QueryFilters.parseFrom(request);
        QueryOrder order = QueryOrders.parseFrom(request);
        QueryRange range = QueryRanges.parseFrom(request, Constants.DEFAULT_RANGE_LIMIT);
        List<UserService> entities = userServiceDao.findByUserUuid(uuid);
        response.setCollectionResponse(range, entities.size(), entities.size());

        addTokenBinder();

        return entities;
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
        //TODO: Your 'DELETE' logic here...
        response.setResponseNoContent();
    }
}
