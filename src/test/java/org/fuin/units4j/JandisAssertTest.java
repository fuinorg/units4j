/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.units4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.units4j.JandisAssert.assertThat;

import java.io.File;
import java.util.List;

import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;
import org.junit.Test;

/**
 * Tests the {@link JandisAssert} class.
 */
// CHECKSTYLE:OFF Test code
public class JandisAssertTest {

    @Test
    public void testAssertThat() {
        
        // TEST
        final Indexer indexer = new Indexer();
        final Index actual = indexer.complete();
        final JandisAssert result = JandisAssert.assertThat(actual);
        
        // VERIFY
        assertThat(result).isNotNull();
        
    }
    
    @Test
    public void testHasOnlyValidJpaEntities() {

        // PREPARE
        final File dir = new File("target/test-classes");
        final List<File> classFiles = Units4JUtils.findAllClasses(dir);
        final Index index = Units4JUtils.indexAllClasses(classFiles);

        // TEST + VERIFY
        assertThat(index).hasOnlyValidJpaEntities();
        
    }
    
    
}
// CHECKSTYLE:ON
