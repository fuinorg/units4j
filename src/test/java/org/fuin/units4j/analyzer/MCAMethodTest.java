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
package org.fuin.units4j.analyzer;

import static org.assertj.core.api.Assertions.assertThat;
import nl.jqno.equalsverifier.EqualsVerifier;

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

    @Test
    public final void testEqualsHashCode() {
        
        EqualsVerifier.forClass(MCAMethod.class).withIgnoredFields("method").verify();
        
    }

    @Test
    public final void testCompareTo() {
        
        // PREPARE
        final String classNameA = "a.b.c.A";
        final String methodSignatureA = "int a(java.lang.String)";
        final MCAMethod methodA = new MCAMethod(classNameA, methodSignatureA);

        final String classNameB = "a.b.c.B";
        final String methodSignatureB = "int a(java.lang.String)";
        final MCAMethod methodB = new MCAMethod(classNameB, methodSignatureB);

        final String methodSignatureC = "int b(java.lang.String)";
        final MCAMethod methodC = new MCAMethod(classNameB, methodSignatureC);
        
        // TEST + VERIFY
        assertThat(methodA.compareTo(methodB)).isEqualTo(-1);
        assertThat(methodB.compareTo(methodA)).isEqualTo(1);
        assertThat(methodA.compareTo(methodA)).isEqualTo(0);
        
        assertThat(methodB.compareTo(methodC)).isEqualTo(-1);
        assertThat(methodC.compareTo(methodB)).isEqualTo(1);
        
    }
    
}
// CHECKSTYLE:ON
