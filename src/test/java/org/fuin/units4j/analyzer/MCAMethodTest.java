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
package org.fuin.units4j.analyzer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

//CHECKSTYLE:OFF

/**
 * Test for {@link MCAMethod}.
 */
public final class MCAMethodTest {

    @Test
    public final void testConstructor() {

        // PREPARE
        final String className = "a.b.c.MyClass";
        final String methodSignature = "int myMethod(java.lang.String)";

        // TEST
        final MCAMethod testee = new MCAMethod(className, methodSignature);

        // VERIFY
        assertThat(testee.getClassName()).isEqualTo(className);
        assertThat(testee.getMethod().getName()).isEqualTo("myMethod");
        assertThat(testee.getMethod().getReturnType().getClassName())
                .isEqualTo("int");
        assertThat(testee.getMethod().getArgumentTypes()).hasSize(1);
        assertThat(testee.getMethod().getArgumentTypes()[0].getClassName())
                .isEqualTo("java.lang.String");

    }

}
// CHECKSTYLE:ON
