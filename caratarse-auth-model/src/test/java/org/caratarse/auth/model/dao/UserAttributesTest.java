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

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.attribute.Attribute;
import org.caratarse.auth.model.po.attribute.BinaryAttribute;
import org.caratarse.auth.model.po.attribute.BooleanAttribute;
import org.caratarse.auth.model.po.attribute.DateAttribute;
import org.caratarse.auth.model.po.attribute.DateTimeAttribute;
import org.caratarse.auth.model.po.attribute.IntAttribute;
import org.caratarse.auth.model.po.attribute.LongAttribute;
import org.caratarse.auth.model.po.attribute.LongStringAttribute;
import org.caratarse.auth.model.po.attribute.StringAttribute;
import org.caratarse.auth.model.test.BaseTest;
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
public class UserAttributesTest extends BaseTest {

    @Resource
    private UserDao userDao;
    
    @Before
    public void setFilters() {
        ((HibernateGenericDao)userDao).setFilterNames("limitByNotDeleted");
    }

    @After
    public void removeFilters() {
        ((HibernateGenericDao)userDao).setFilterNames();
    }

    @Test
    public void retrieveUserAttributes() {
        User user = retrieveUserWithAttributes();
        assertSize(9, user.getUserAttributes().values());
    }

    public User retrieveUserWithAttributes() {
        String uuid = "12345678-1234-1234-1234-123456781234";
        User user = userDao.findByUuid(uuid);
        return user;
    }
    
    @Test
    public void firstNameUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("firstName");
        assertTrue(attribute instanceof StringAttribute);
        assertThat(attribute.getName(), is("firstName"));
        assertThat((String)attribute.getValue(), is("U1 first name"));
    }

    @Test
    public void lastNameUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("lastName");
        assertTrue(attribute instanceof StringAttribute);
        assertThat(attribute.getName(), is("lastName"));
        assertThat((String)attribute.getValue(), is("U1 last name"));
    }

    @Test
    public void descriptionUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("description");
        assertTrue(attribute instanceof LongStringAttribute);
        assertThat(attribute.getName(), is("description"));
        assertThat((String)attribute.getValue(), is("The user1..."));
    }
    
    @Test
    public void levelUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("level");
        assertTrue(attribute instanceof IntAttribute);
        assertThat(attribute.getName(), is("level"));
        assertThat((Integer)attribute.getValue(), is(5));
    }

    @Test
    public void bonusUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("bonus");
        assertTrue(attribute instanceof LongAttribute);
        assertThat(attribute.getName(), is("bonus"));
        assertThat((Long)attribute.getValue(), is(12345678987L));
    }

    @Test
    public void pictureUserAttribute() throws IOException {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("picture");
        assertTrue(attribute instanceof BinaryAttribute);
        assertThat(attribute.getName(), is("picture"));
        assertThat(((byte[])attribute.getValue()).length, is(1147));
        InputStream is = this.getClass().getResourceAsStream("/fixtures/globe-6x.png");
        byte[] buffer = IOUtils.toByteArray(is);
        assertTrue(Arrays.equals(buffer, (byte[]) attribute.getValue()));
    }

    @Test
    public void birthdateUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("birthdate");
        assertTrue(attribute instanceof DateAttribute);
        assertThat(attribute.getName(), is("birthdate"));
        final Date value = new Date(((Date)attribute.getValue()).getTime());
        ZonedDateTime v = value.toInstant().atZone(ZoneId.systemDefault());
        assertThat(v.getDayOfMonth(), is(5));
        assertThat(v.getMonthValue(), is(7));
        assertThat(v.getYear(), is(1980));
    }

    @Test
    public void lastUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("last");
        assertTrue(attribute instanceof DateTimeAttribute);
        assertThat(attribute.getName(), is("last"));
        final Date value = new Date(((Date)attribute.getValue()).getTime());
        ZonedDateTime v = value.toInstant().atZone(ZoneId.of("UTC"));
        assertThat(v.getDayOfMonth(), is(20));
        assertThat(v.getMonthValue(), is(11));
        assertThat(v.getYear(), is(2015));
        assertThat(v.getHour(), is(12));
        assertThat(v.getMinute(), is(11));
        assertThat(v.getSecond(), is(10));
        assertThat(v.getNano(), is(999000000));
    }

    @Test
    public void checkedUserAttribute() {
        User user = retrieveUserWithAttributes();
        Attribute attribute = user.getUserAttributes().get("checked");
        assertTrue(attribute instanceof BooleanAttribute);
        assertThat(attribute.getName(), is("checked"));
        assertThat((Boolean)attribute.getValue(), is(true));
    }
    
}
