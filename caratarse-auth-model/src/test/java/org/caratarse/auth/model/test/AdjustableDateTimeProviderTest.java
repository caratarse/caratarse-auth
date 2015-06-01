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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test the AdjustableDateTimeProvider methods.
 *
 * @author Lucio Benfante
 * (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class AdjustableDateTimeProviderTest {
    
    public AdjustableDateTimeProviderTest() {
    }

    /**
     * Test of setNow method, of class AdjustableDateTimeProvider, using a LocalDate.
     */
    @Test
    public void testSetNowWithLocalDate() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        instance.setNow(LocalDate.now());
    }

    /**
     * Test of setNow method, of class AdjustableDateTimeProvider, using an java.util.Date.
     */
    @Test
    public void testSetNowWithDate() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        instance.setNow(new Date());
    }
    
    @Test
    public void testGetCurrentDate() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        LocalDate result = instance.getCurrentDate();
        LocalDate now = LocalDate.now();
        assertThat(now, equalTo(result));
    }

    @Test
    public void testGetCurrentDateTime() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        LocalDateTime result = instance.getCurrentDateTime();
        LocalDateTime now = LocalDateTime.now();
        assertTrue(now.compareTo(result) >= 0);
    }

    @Test
    public void testGetCurrentInstant() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        Instant result = instance.getCurrentInstant();
        Instant now = Instant.now();
        assertTrue(now.compareTo(result) >= 0);
    }

    @Test
    public void testGetCurrentOldDate() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        Date result = instance.getCurrent();
        Date now = new Date();
        assertTrue(now.compareTo(result) >= 0);
    }
    
    @Test
    public void testGetCurrentAdjustedDate() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        instance.setNow(LocalDate.of(2014, 3, 31));
        LocalDate result = instance.getCurrentDate();
        LocalDate expected = LocalDate.of(2014, 3, 31);
        assertThat(expected, equalTo(result));
    }

    @Test
    public void testGetCurrentAdjustedDateTime() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        instance.setNow(LocalDateTime.of(2014, 3, 31, 14, 28, 42, 56));
        LocalDateTime result = instance.getCurrentDateTime();
        LocalDateTime expected = LocalDateTime.of(2014, 3, 31, 14, 28, 42, 56);
        assertThat(result, equalTo(expected));
    }

    @Test
    public void testGetCurrentAdjustedInstant() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        LocalDateTime dateTime = LocalDateTime.of(2014, 3, 31, 14, 28, 42, 56);
        instance.setNow(dateTime.toInstant(ZoneOffset.UTC));
        Instant result = instance.getCurrentInstant();
        Instant expected = dateTime.toInstant(ZoneOffset.UTC);
        assertThat(expected, equalTo(result));
    }

    @Test
    public void testGetCurrentAdjusted() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 2, 31, 14, 28, 42);        
        instance.setNow(cal.getTime());
        Date result = instance.getCurrent();
        Date expected = cal.getTime();
        assertThat(expected, equalTo(result));
    }
    
    @Test
    public void testGetCurrentDateAfterAnAdjustementAndAReset() {
        AdjustableDateTimeProvider instance = new AdjustableDateTimeProvider();
        instance.setNow(LocalDate.of(2014, 3, 31)); // returning time as 31/3/2014
        LocalDate adjustedResult = instance.getCurrentDate();
        instance.setNow((Instant)null); // returning current time now
        LocalDate resettedResult = instance.getCurrentDate();
        assertThat(adjustedResult, not(equalTo(resettedResult)));
    }
    
}
