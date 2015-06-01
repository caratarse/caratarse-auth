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
package org.caratarse.auth.model.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * User and Service relationship.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@NamedQueries({
    @NamedQuery(name = "UserService.findByUserUuid",
            query = "from UserService us where us.user.uuid = ?"),
    @NamedQuery(name = "UserService.findByUserUuidAndServiceName",
            query = "from UserService us where us.user.uuid = ? and us.service.name = ?")
})
@Entity
public class UserService extends EntityBase {
    
    @JsonBackReference
    private User user;
    @JsonBackReference
    private Service service;

    public UserService() {
    }

    public UserService(User user, Service service) {
        this.user = user;
        this.service = service;
        
        user.getUserServices().add(this);
        service.getUserServices().add(this);
    }
    
    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
    
}
