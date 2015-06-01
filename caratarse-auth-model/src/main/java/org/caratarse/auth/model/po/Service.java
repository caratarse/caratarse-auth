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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * The service entity.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@Entity
public class Service extends EntityBase {

    private String name;
    private String description;
    @JsonIgnore
    private List<UserService> userServices;
    @JsonIgnore
    private List<Authorization> authorizations;
    
    public Service() {
    }

    public Service(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "service")
    @Filters({@Filter(name = "limitByNotDeleted")})
    public List<UserService> getUserServices() {
        if (userServices == null) {
            userServices = new LinkedList<UserService>();
        }
        return userServices;
    }

    public void setUserServices(List<UserService> userServices) {
        this.userServices = userServices;
    }

    @OneToMany(mappedBy = "service")
    @Filters({@Filter(name = "limitByNotDeleted")})
    public List<Authorization> getAuthorizations() {
        if (authorizations == null) {
            authorizations = new LinkedList<Authorization>();
        }
        return authorizations;
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
    }

    @Override
    protected void doRecursiveDelete() {
        super.doRecursiveDelete();
        final List<UserService> userServices = getUserServices();
        if (userServices != null && !userServices.isEmpty()) {
            for (UserService userService : userServices) {
                userService.delete();
            }
        }
        List<Authorization> authorizations = getAuthorizations();
        if (authorizations != null && !authorizations.isEmpty()) {
            for (Authorization authorization : authorizations) {
                authorization.delete();
            }
        }
    }

}
