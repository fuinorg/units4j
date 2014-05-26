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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

//CHECKSTYLE:OFF

/**
 * Test for {@link MCAMethod}.
 */
public final class MCAMethodCallTest {

    @Test
    public final void testConstructor() {

        // PREPARE
        final String calledClassName = "a.b.c.MyClass";
        final String calledMethodSignature = "int myMethod(java.lang.String)";
        final MCAMethod called = new MCAMethod(calledClassName,
                calledMethodSignature);

        final String callerClassName = "a.b.c.A";
        final String callerMethodSignature = "void a()";
        final String callerSourceFileName = "A.java";
        final int callerLine = 123;
        final MCAMethod caller = new MCAMethod(callerClassName,
                callerMethodSignature);

        // TEST

        final MCAMethodCall testee = new MCAMethodCall(called, callerClassName,
                "a", "()V", callerSourceFileName, callerLine);

        // VERIFY
        assertThat(testee.getCalled()).isEqualTo(called);
        assertThat(testee.getCaller()).isEqualTo(caller);
        assertThat(testee.getSourceFileName()).isEqualTo(callerSourceFileName);
        assertThat(testee.getLine()).isEqualTo(callerLine);

    }

}
// CHECKSTYLE:ON
