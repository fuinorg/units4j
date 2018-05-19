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
package org.fuin.units4j.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import org.fuin.utils4j.Utils4J;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

//CHECKSTYLE:OFF Test code
public final class PackageTest {

    @Test
    public final void testCreation() {

        final String name = "Name";
        final String comment = "Whatever";
        final Package<DependsOn> testee = new Package<DependsOn>(name, comment);
        assertThat(testee.getName()).isEqualTo(name);
        assertThat(testee.getComment()).isEqualTo(comment);
        assertThat(testee.getDependencies()).isEmpty();

    }

    @Test
    public final void testEqualsHashCode() {

        EqualsVerifier.forClass(Package.class).withIgnoredFields("comment", "dependencies").verify();

    }
    
    @Test
    public final void testSerDeserialize() {
	
	// PREPARE
	final String name = "org.fuin.units4j.dependency";
	final String comment = "Bla";
	final Package<DependsOn> testee = new Package<>(name, comment);
	
	// TEST
	final byte[] data = Utils4J.serialize(testee);
	final Package<DependsOn> copy = Utils4J.deserialize(data); 
	
	// VERIFY
	assertThat(copy.getName()).isEqualTo(name);
	assertThat(copy.getComment()).isEqualTo(comment);
	assertThat(copy.getDependencies()).isEmpty();
	
    }
    

}
// CHECKSTYLE:ON
