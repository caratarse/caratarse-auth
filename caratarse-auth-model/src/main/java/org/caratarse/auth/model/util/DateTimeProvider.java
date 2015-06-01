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
package org.caratarse.auth.model.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A source for dates and times.
 *
 * @author Lucio Benfante
 * (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public interface DateTimeProvider {
    /**
     * Get the date of today.
     * 
     * @return The date of today.
     */
    LocalDate getCurrentDate();

    /**
     * Get the date and time of now.
     * 
     * @return The date and time of now.
     */
    LocalDateTime getCurrentDateTime();

    /**
     * Get the instant of now.
     * 
     * @return The instant of now.
     */
    Instant getCurrentInstant();
    
    /**
     * Get the instant of now as a java.util.Date.
     * No, wait, why? ...please use JodaTime!
     * Ok, if you really really really have to do, beware your mental sanity risk!
     * 
     * @return The instant of now.
     */
    Date getCurrent();
    
}
