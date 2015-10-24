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
package org.caratarse.auth.model.test;

import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.po.Resource;
import org.caratarse.auth.model.po.ResourceHierarchyLink;
import org.caratarse.auth.model.po.Service;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;
import org.caratarse.auth.model.po.UserService;
import org.lambico.test.spring.hibernate.junit4.AbstractBaseTest;
import org.lambico.test.spring.hibernate.junit4.FixtureSet;

/**
 * A base class for basic persistence tests.
 *
 * @author Lucio Benfante
 */
@FixtureSet(modelClasses = {User.class, Resource.class, ResourceHierarchyLink.class,
    Service.class, UserService.class, Authorization.class, UserAuthorization.class})
public abstract class BaseTest extends AbstractBaseTest {
}
