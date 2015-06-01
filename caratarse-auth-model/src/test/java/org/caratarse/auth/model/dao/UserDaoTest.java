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
package org.caratarse.auth.model.dao;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.po.Permissions;
import org.caratarse.auth.model.po.Service;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserService;
import org.caratarse.auth.model.test.AdjustableDateTimeProvider;
import org.caratarse.auth.model.test.BaseTest;
import org.caratarse.auth.model.util.DateTimeProviderHolder;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import static org.lambico.test.ExtraAssert.*;

/**
 * Tests on {@link UserDao}.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class UserDaoTest extends BaseTest {

    @Resource
    private UserDao userDao;
    @Resource
    private AuthorizationDao authorizationDao;
    
    @Before
    public void setFilters() {
        ((HibernateGenericDao)userDao).setFilterNames("limitByNotDeleted");
        ((HibernateGenericDao)authorizationDao).setFilterNames("limitByNotDeleted");
    }

    @After
    public void removeFilters() {
        ((HibernateGenericDao)userDao).setFilterNames();
        ((HibernateGenericDao)authorizationDao).setFilterNames();
    }
    
    @Test
    public void testAllUsers() {
        List<User> users = userDao.findAll();
        assertSize(3, users);
    }

    @Test
    public void testActiveUsers() {
        List<User> users = userDao.findAllActive();
        assertSize(2, users);
    }

    @Test
    public void testFindByUuid() {
        String uuid = "12345678-1234-1234-1234-123456781234";
        User user = userDao.findByUuid(uuid);
        assertNotNull(user);
        assertThat(user.getUuid(), is(uuid));
    }

    @Test
    public void creationDateIsSet() {
        String uuid = "12345678-1234-1234-1234-123456781234";
        User user = userDao.findByUuid(uuid);
        assertNotNull(user.getCreationDate());
    }

    @Test
    public void updatedDateIsSet() {
        String uuid = "12345678-1234-1234-1234-123456781234";
        User user = userDao.findByUuid(uuid);
        assertNotNull(user.getUpdatedDate());
        assertEquals(user.getCreationDate(), user.getUpdatedDate());
    }

    @Test
    public void updatedDateIsUpdated() {
        String uuid = "12345678-1234-1234-1234-123456781234";
        User user = userDao.findByUuid(uuid);
        Date oldUpdatedDate = user.getUpdatedDate();
        user.setLastLogin(new Date());
        userDao.store(user);
        ((HibernateGenericDao)userDao).getHibernateTemplate().flush();
        ((HibernateGenericDao)userDao).getHibernateTemplate().evict(user);
        user = userDao.findByUuid(uuid);
        Date newUpdatedDate = user.getUpdatedDate();
        assertNotEquals(oldUpdatedDate, newUpdatedDate);
    }
    
    @Test
    public void testFindByUsername() {
        String username = "user1";
        User user = userDao.findByUsername(username);
        assertNotNull(user);
        assertThat(user.getUsername(), is(username));
        assertFalse(user.checkDeleted());
    }

    @Test
    public void testLogicDelete() {
        String username = "user1";
        User user = userDao.findByUsername(username);
        final AdjustableDateTimeProvider dateTimeProvider = new AdjustableDateTimeProvider();
        final LocalDateTime deleteDate = LocalDateTime.of(2015, 3, 25, 12, 54, 22);
        dateTimeProvider.setNow(deleteDate);
        DateTimeProviderHolder.setDateTimeProvider(dateTimeProvider);
        user.delete();
        assertThat(user.getDeleted(), is(Date.from(deleteDate.toInstant(ZoneOffset.UTC))));
    }

    @Test
    public void logicallyDeleteUserAndServiceLinks() {
        String username = "user1";
        User user = userDao.findByUsername(username);
        List<UserService> userServices = user.getUserServices();
        List<Service> services = new ArrayList<Service>();
        for (UserService userService : userServices) {
            services.add(userService.getService());
        }
        user.delete();
        assertTrue(user.checkDeleted());
        for (UserService userService : userServices) {
            assertTrue(userService.checkDeleted());
        }
        for (Service service : services) {
            assertFalse(service.checkDeleted());
        }
    }
    
    @Test
    public void addAutorizationToUser() {
        String username = "user1";
        User user = userDao.findByUsername(username);
        int oldSize = user.getUserAuthorizations().size();
        Authorization authorization = authorizationDao.findByNameAndService("ROLE_FOURTH", "service1");
        user.addAuthorization(authorization, Permissions.R);        
        userDao.store(user);
        ((HibernateGenericDao)userDao).getHibernateTemplate().flush();
        ((HibernateGenericDao)userDao).getHibernateTemplate().clear();
        user = userDao.findByUsername(username);
        assertSize(oldSize + 1, user.getUserAuthorizations());
        assertTrue(user.hasAuthorization(authorization));
    }
    
    @Test(expected = IllegalStateException.class)
    public void addAutorizationToUserWithoutService() {
        String username = "user1";
        User user = userDao.findByUsername(username);
        Authorization authorization = authorizationDao.findByNameAndService("ROLE_THIRD", "service2");
        user.addAuthorization(authorization, Permissions.R);
    }
    
}
