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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * User and Authorization relationship.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@NamedQueries({
    @NamedQuery(name = "UserAuthorization.findByUserUuidAndServiceName",
            query = "from UserAuthorization ua where ua.user.uuid = ? and ua.authorization.service.name = ?")
})
@Entity
public class UserAuthorization extends EntityBase {

    @JsonBackReference(value = "user-authorization")
    private User user;
    @JsonBackReference
    private Authorization authorization;
    private Permissions permissions;

    public UserAuthorization() {
    }

    public UserAuthorization(User user, Authorization authorization) {
        this(user, authorization, Permissions.R);
    }

    public UserAuthorization(User user, Authorization authorization, Permissions permissions) {
        this.user = user;
        this.authorization = authorization;
        this.permissions = permissions;
        user.getUserAuthorizations().add(this);
        authorization.getUserAuthorizations().add(this);
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    @Embedded
    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }


}
