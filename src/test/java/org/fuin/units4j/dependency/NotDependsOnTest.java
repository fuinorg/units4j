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
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

// CHECKSTYLE:OFF Test code
public final class NotDependsOnTest {

    @Test
    public final void testConstructionString() {
        final String name = "org.fuin.units4j.dependency";
        final NotDependsOn testee = new NotDependsOn(name);
        assertThat(testee.getPackageName()).isEqualTo(name);
        assertThat(testee.toString()).isEqualTo(name);
        assertThat(testee.getComment()).isNull();
        assertThat(testee.isIncludeSubPackages()).isTrue();
    }

    @Test
    public final void testConstructionStringString() {
        final String name = "org.fuin.units4j.dependency";
        final String comment = "Whatever";
        final NotDependsOn testee = new NotDependsOn(name, comment);
        assertThat(testee.getPackageName()).isEqualTo(name);
        assertThat(testee.toString()).isEqualTo(name);
        assertThat(testee.getComment()).isEqualTo(comment);
        assertThat(testee.isIncludeSubPackages()).isTrue();
    }

    @Test
    public final void testConstructionStringBooleanString() {
        final String name = "org.fuin.units4j.dependency";
        final boolean recursive = true;
        final String comment = "Whatever";
        final NotDependsOn testee = new NotDependsOn(name, recursive, comment);
        assertThat(testee.getPackageName()).isEqualTo(name);
        assertThat(testee.isIncludeSubPackages()).isEqualTo(recursive);
        assertThat(testee.getComment()).isEqualTo(comment);
    }

    @Test
    public final void testEqualsHashCode() {
        EqualsVerifier.forClass(NotDependsOn.class).withRedefinedSuperclass().verify();
    }

}
// CHECKSTYLE:ON
