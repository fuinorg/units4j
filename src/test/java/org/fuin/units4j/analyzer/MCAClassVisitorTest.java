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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;

// CHECKSTYLE:OFF

/**
 * Test for {@link MCAClassVisitor}.
 */
public final class MCAClassVisitorTest {

    private MCAMethod setScale;

    private List<MCAMethod> methodsToFind;

    private MCAClassVisitor testee;

    @Before
    public final void setup() {
        setScale = new MCAMethod(BigDecimal.class.getName(), BigDecimal.class.getName()
                + " setScale(int)");
        methodsToFind = new ArrayList<MCAMethod>();
        methodsToFind.add(setScale);
        testee = new MCAClassVisitor(methodsToFind);
    }

    @After
    public final void tearDown() {
        setScale = null;
        methodsToFind = null;
        testee = null;
    }

    @Test
    public final void testConstructor() {

        // PREPARE & TEST
        // => Done in setup

        // VERIFY
        assertThat(testee.getMethodCalls()).isEmpty();
        assertThat(testee.getMethodsToFind()).containsOnly(setScale);

    }

    @Test
    public final void testVisit() {

        // PREPARE
        final String name = "abc";

        // TEST
        testee.visit(0, 0, name, null, null, null);

        // VERIFY
        assertThat(testee.getClassName()).isEqualTo(name);

    }

    @Test
    public final void testVisitSource() {

        // PREPARE
        final String source = "ClassXy.java";

        // TEST
        testee.visitSource(source, null);

        // VERIFY
        assertThat(testee.getSource()).isEqualTo(source);

    }

    @Test
    public final void testVisitMethod() {

        // PREPARE
        final String name = "myMethod";
        final String desc = "(Ljava/lang/String;)I";

        // TEST
        final MethodVisitor result = testee.visitMethod(0, name, desc, null, null);

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(testee.getMethodName()).isEqualTo(name);
        assertThat(testee.getMethodDescr()).isEqualTo(desc);

    }

    @Test
    public final void testAddCall() {

        // PREPARE
        final MCAMethod method = new MCAMethod("a.b.c.MyClass", "int myMethod(java.lang.String)");
        final int line = 123;
        final String source = "MyClass.java";
        testee.visit(0, 0, "a.b.c.MyClass", null, null, null);
        testee.visitSource(source, null);
        testee.visitMethod(0, "myMethod", "(Ljava/lang/String;)I", null, null);

        // TEST
        testee.addCall(setScale, line);

        // VERIFY
        assertThat(testee.getMethodCalls()).hasSize(1);
        final MCAMethodCall call = testee.getMethodCalls().get(0);
        assertThat(call.getCalled()).isEqualTo(setScale);
        assertThat(call.getCaller()).isEqualTo(method);
        assertThat(call.getLine()).isEqualTo(line);
        assertThat(call.getSourceFileName()).isEqualTo(source);

    }

}
// CHECKSTYLE:ON
