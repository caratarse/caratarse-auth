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
 * Tests on {@link ServiceDao}
 * 
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class ServiceDaoTest extends BaseTest {
    
    @Resource
    private ServiceDao serviceDao;
     
    @Before
    public void setFilters() {
        ((HibernateGenericDao)serviceDao).setFilterNames("limitByNotDeleted");
    }

    @After
    public void removeFilters() {
        ((HibernateGenericDao)serviceDao).setFilterNames();
    }
    
    @Test
    public void testAllServices() {
        List<Service> services = serviceDao.findAll();
        assertSize(3, services);
    }

    @Test
    public void testFindByUsername() {
        String name = "service1";
        Service service = serviceDao.findByName(name);
        assertNotNull(service);
        assertThat(service.getName(), is(name));
        assertFalse(service.checkDeleted());
    }
    
    @Test
    public void testLogicDelete() {
        String name = "service2";
        Service service = serviceDao.findByName(name);
        final AdjustableDateTimeProvider dateTimeProvider = new AdjustableDateTimeProvider();
        final LocalDateTime deleteDate = LocalDateTime.of(2015, 3, 25, 12, 54, 22);
        dateTimeProvider.setNow(deleteDate);
        DateTimeProviderHolder.setDateTimeProvider(dateTimeProvider);
        service.delete();
        assertThat(service.getDeleted(), is(Date.from(deleteDate.toInstant(ZoneOffset.UTC))));
    }

    @Test
    public void notFindingDeletedServicesByName() {
        String name = "deleted service";
        Service service = serviceDao.findByName(name);
        assertNull(service);
    }
    
    @Test
    public void logicallyDeleteServiceAndUserLinks() {
        String name = "service1";
        Service service = serviceDao.findByName(name);
        List<UserService> userServices = service.getUserServices();
        List<User> users = new ArrayList<User>();
        for (UserService userService : userServices) {
            users.add(userService.getUser());
        }
        service.delete();
        assertTrue(service.checkDeleted());
        for (UserService userService : userServices) {
            assertTrue(userService.checkDeleted());
        }
        for (User user : users) {
            assertFalse(user.checkDeleted());
        }
    }
    
    @Test
    public void getNotDeletedUserServices() {
        String name = "service1";
        Service service = serviceDao.findByName(name);
        List<UserService> userServices = service.getUserServices();
        assertSize(3, userServices);
    }
}
