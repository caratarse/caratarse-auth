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
public class UserBoTest extends BaseTest {
    @Resource
    private UserBo userBo;
    

    /**
     * Test of retrieveUserDirectAuthorizations method, of class UserBo.
     */
    @Test
    public void retrieveUserDirectAuthorizations() {
        String uuid = "12345678-1234-1234-1234-123456781234";
        String serviceName = "service1";
        List<UserAuthorization> result
                = userBo.retrieveUserDirectAuthorizations(uuid, serviceName);
        assertSize(2, result);
    }
    
    @Test
    public void retrieveDeletedUser() {
        String uuid = "32345678-1234-1234-1234-123456781234";
        User user = userBo.getUser(uuid);
        assertNull(user);
    }
    
}
