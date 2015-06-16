/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Model.
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
package org.caratarse.auth.model.bo;

import java.util.List;
import javax.annotation.Resource;
import org.caratarse.auth.model.dao.AuthorizationDao;
import org.caratarse.auth.model.dao.ServiceDao;
import org.caratarse.auth.model.dao.UserAuthorizationDao;
import org.caratarse.auth.model.dao.UserDao;
import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.po.Permissions;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Service
public class UserBo {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAuthorizationDao userAuthorizationDao;
    @Resource
    private ServiceDao serviceDao;
    @Resource
    private AuthorizationDao authorizationDao;
    
    @Transactional
    public List<UserAuthorization> retrieveUserDirectAuthorizations(String uuid, String serviceName) {
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames("limitByNotDeleted");
        List<UserAuthorization> result = userAuthorizationDao.findByUserUuidAndServiceName(uuid, serviceName);
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames();
        return result;
    }
    
    @Transactional
    public List<User> readAll(QueryFilter filter, QueryRange range, QueryOrder order) {
        return userDao.findAll();
    }

    @Transactional
    public User getUser(String uuid) {
        ((HibernateGenericDao)userDao).setFilterNames("limitByNotDeleted");
        final User user = userDao.findByUuid(uuid);
        ((HibernateGenericDao)userDao).setFilterNames();
        return user;
    }
    
    @Transactional
    public List<User> populate() {
        final User userLucio = new User("lucio", "lucioPwd");
        userLucio.setUuid("a1ab82a6-c8ce-4723-8532-777c4b05d03c");
        userDao.create(userLucio);
        final User userMichele = new User("michele", "michelePwd");
        userDao.create(userMichele);
        final org.caratarse.auth.model.po.Service serviceTest
                = new org.caratarse.auth.model.po.Service("TEST_SERVICE", "A service for tests");
        serviceDao.create(serviceTest);
        userLucio.addService(serviceTest);
        userMichele.addService(serviceTest);
        Authorization authorizationAdmin
                = new Authorization("ROLE_ADMIN", "Admin authorization", serviceTest);
        authorizationDao.create(authorizationAdmin);
        userLucio.addAuthorization(authorizationAdmin, Permissions.RWX);
        Authorization authorizationUser
                = new Authorization("ROLE_USER", "User authorization", serviceTest);
        authorizationDao.create(authorizationUser);
        userMichele.addAuthorization(authorizationUser, Permissions.RWX);
        Authorization authorizationPrinter
                = new Authorization("ROLE_PRINTER", "Printer authorization", serviceTest);
        authorizationDao.create(authorizationPrinter);
        userMichele.addAuthorization(authorizationUser, Permissions.R);
        return userDao.findAll();
    }

    @Transactional
    public void deleteUser(String uuid) {
        User user = userDao.findByUuid(uuid);
        user.delete();
    }
    
}
