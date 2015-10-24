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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * The authorization entity.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@NamedQueries({
    @NamedQuery(name = "Authorization.findByName", 
            query = "from Authorization a where a.name = ?")
})
@Entity
public class Authorization extends EntityBase {

    private String name;
    private String description;
    @JsonIgnore
    private List<UserAuthorization> userAuthorizations;

    public Authorization() {
    }

    public Authorization(String name, String description) {
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

    @OneToMany(mappedBy = "authorization")
    @Filters({@Filter(name = "limitByNotDeleted")})
    @JsonManagedReference
    public List<UserAuthorization> getUserAuthorizations() {
        if (userAuthorizations == null) {
            userAuthorizations = new LinkedList<UserAuthorization>();
        }
        return userAuthorizations;
    }

    public void setUserAuthorizations(List<UserAuthorization> userAuthorizations) {
        this.userAuthorizations = userAuthorizations;
    }

    @Override
    protected void doRecursiveDelete() {
        super.doRecursiveDelete();
        List<UserAuthorization> userAuthorizations = getUserAuthorizations();
        if (userAuthorizations != null && !userAuthorizations.isEmpty()) {
            for (UserAuthorization userAuthorization : userAuthorizations) {
                userAuthorization.delete();
            }
        }
    }

}
