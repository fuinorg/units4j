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

import org.junit.Test;

// CHECKSTYLE:OFF Test code
public final class DependencyErrorTest {

    @Test
    public final void testCreation() {

        final String className = "Name";
        final String referencedPackage = "a.b.c";
        final String comment = "Whatever";
        final DependencyError testee = new DependencyError(className, referencedPackage, comment);
        assertThat(testee.getClassName()).isEqualTo(className);
        assertThat(testee.getReferencedPackage()).isEqualTo(referencedPackage);
        assertThat(testee.getComment()).isEqualTo(comment);

    }

}
// CHECKSTYLE:ON
