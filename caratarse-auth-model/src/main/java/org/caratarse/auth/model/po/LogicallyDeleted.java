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

import java.util.Date;

/**
 * Interfaces for entities that support logical deletion.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public interface LogicallyDeleted {

    /**
     * The logically deleted flag.
     *
     * @return true if the entity should be considered logically deleted.
     */
    boolean checkDeleted();

    /**
     * Get the deleted timestamp for logically deleted objects. (null if not deleted)
     * 
     * @return not null if the object should be considered logically deleted.
     */
    Date getDeleted();
    
    /**
     * Set the timestamp for logically deleted objects. 
     * 
     * @param deleted not null if the instance should be considered logically deleted.
     */
    public void setDeleted(Date deleted);
    
}
