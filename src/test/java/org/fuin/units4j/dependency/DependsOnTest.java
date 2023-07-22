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

import org.fuin.utils4j.jaxb.JaxbUtils;
import org.fuin.utils4j.Utils4J;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

//CHECKSTYLE:OFF Test code
public final class DependsOnTest {

    @Test
    public final void testConstructionString() {
        final String name = "org.fuin.units4j.dependency";
        final DependsOn testee = new DependsOn(name);
        assertThat(testee.getPackageName()).isEqualTo(name);
        assertThat(testee.toString()).isEqualTo(name);
        assertThat(testee.isIncludeSubPackages()).isTrue();
    }

    @Test
    public final void testConstructionStringBoolean() {
        final String name = "org.fuin.units4j.dependency";
        final boolean recursive = false;
        final DependsOn testee = new DependsOn(name, recursive);
        assertThat(testee.getPackageName()).isEqualTo(name);
        assertThat(testee.toString()).isEqualTo(name);
        assertThat(testee.isIncludeSubPackages()).isEqualTo(recursive);
    }

    @Test
    public final void testEqualsHashCode() {
        EqualsVerifier.forClass(DependsOn.class).withRedefinedSuperclass().withIgnoredFields("includeSubPackages")
                .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public final void testSerDeserialize() {

        // PREPARE
        final String name = "org.fuin.units4j.dependency";
        final DependsOn testee = new DependsOn(name);

        // TEST
        final byte[] data = Utils4J.serialize(testee);
        final DependsOn copy = Utils4J.deserialize(data);

        // VERIFY
        assertThat(copy.getPackageName()).isEqualTo(name);
        assertThat(copy.toString()).isEqualTo(name);
        assertThat(copy.isIncludeSubPackages()).isTrue();

    }

    @Test
    public final void testMarshalUnmarshalXml() {

        // PREPARE
        final String name = "org.fuin.units4j.dependency";
        final DependsOn testee = new DependsOn(name);

        // TEST
        final String xml = Utils.toXml(testee, false, false);
        assertThat(xml).isEqualTo("<dependsOn package=\"org.fuin.units4j.dependency\" includeSubPackages=\"true\"/>");
        final DependsOn copy = JaxbUtils.unmarshal(Utils.createJaxbContext(), xml, null);

        // VERIFY
        assertThat(copy.getPackageName()).isEqualTo(name);
        assertThat(copy.toString()).isEqualTo(name);
        assertThat(copy.isIncludeSubPackages()).isTrue();

    }

}
// CHECKSTYLE:ON
