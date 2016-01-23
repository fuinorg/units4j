/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.units4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.units4j.JandexAssert.assertThat;
import static org.fuin.units4j.Units4JUtils.index;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;
import org.junit.Test;

/**
 * Tests the {@link JandexAssert} class.
 */
// CHECKSTYLE:OFF Test code
public class JandexAssertTest {

    @Test
    public void testAssertThat() {
        
        // TEST
        final Indexer indexer = new Indexer();
        final Index actual = indexer.complete();
        final JandexAssert result = JandexAssert.assertThat(actual);
        
        // VERIFY
        assertThat(result).isNotNull();
        
    }
    
    @Test
    public void testHasOnlyValidJpaEntitiesTrue() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), MyTestClass.class.getName());
        
        // TEST + VERIFY
        assertThat(index).isNotNull();
        assertThat(index).hasOnlyValidJpaEntities();
        
    }

    @Test(expected = AssertionError.class)
    public void testHasOnlyValidJpaEntitiesFalse() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), MyInvalidEntity.class.getName());
        
        // TEST + VERIFY
        assertThat(index).isNotNull();
        assertThat(index).hasOnlyValidJpaEntities();
        
    }
    

    @Entity
    @Table(name = "INVALID_TABLE")
    public static class MyInvalidEntity implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @Min(1)
        @NotNull
        @Column(name = "ID", nullable = false)
        private Integer id;

        @NotNull
        @Column(name = "NAME", length = 50, nullable = false)
        public String name; // public field is not allowed for JPA entities
        
        public MyInvalidEntity() {
            super();
        }
        
    }    
    
}
// CHECKSTYLE:ON
