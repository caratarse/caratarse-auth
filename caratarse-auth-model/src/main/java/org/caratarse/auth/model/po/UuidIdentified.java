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

import javax.persistence.Column;

/**
 * An interface for uuid identified entities.
 * 
 * NB: You should annotate the getUuid method it in your concrete class, as JPA
 * annotations are not inherited from interfaces.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public interface UuidIdentified {

    /**
     * Getting the uuid for you entity.
     * NB: you should annotate this method in your concrete class, as the JPA
     * annotations are not inherited from interfaces.
     * 
     * @return The uuid.
     */
    @Column(length = 36, nullable = false, unique = true, updatable = false)
    String getUuid();

    void setUuid(String uuid);
    
}
