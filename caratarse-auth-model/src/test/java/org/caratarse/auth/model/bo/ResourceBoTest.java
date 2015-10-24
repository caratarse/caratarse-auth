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
import org.caratarse.auth.model.dao.ResourceDao;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;
import org.caratarse.auth.model.test.BaseTest;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.lambico.test.ExtraAssert.*;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class ResourceBoTest extends BaseTest {
    @Resource
    private ResourceBo resourceBo;
    @Resource
    private ResourceDao resourceDao;
    
    @Test
    public void insertInHierarchyAtLevel0() {
        org.caratarse.auth.model.po.Resource child = resourceDao.findByName("Pakistan").get(0);
        int result = resourceBo.insertInHierarchy(null, child);
        assertThat(result, is(equalTo(1)));
    }

    @Test
    public void insertInHierarchyAtLevel1() {
        org.caratarse.auth.model.po.Resource parent = resourceDao.findByName("India").get(0);
        org.caratarse.auth.model.po.Resource child = resourceDao.findByName("Karnataka").get(0);
        int result = resourceBo.insertInHierarchy(parent, child);
        assertThat(result, is(equalTo(2)));
    }

    @Test
    public void insertInHierarchyAtLevel2() {
        org.caratarse.auth.model.po.Resource parent = resourceDao.findByName("Tamilnadu").get(0);
        org.caratarse.auth.model.po.Resource child = resourceDao.findByName("Coimbatore").get(0);
        int result = resourceBo.insertInHierarchy(parent, child);
        assertThat(result, is(equalTo(3)));
    }

    @Test
    public void insertInHierarchyAtLevel3() {
        org.caratarse.auth.model.po.Resource parent = resourceDao.findByName("Chennai").get(0);
        org.caratarse.auth.model.po.Resource child = resourceDao.findByName("T.nagar").get(0);
        int result = resourceBo.insertInHierarchy(parent, child);
        assertThat(result, is(equalTo(4)));
    }
    
}
