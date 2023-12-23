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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//CHECKSTYLE:OFF

/**
 * Test for {@link MCAMethodVisitor}.
 */
public final class MCAMethodVisitorTest {

    private MCAClassVisitor classVisitor;

    private MCAMethod setScale;

    private List<MCAMethod> toFind;

    private MCAMethodVisitor testee;

    @BeforeEach
    public final void setup() {
        setScale = new MCAMethod(BigDecimal.class.getName(), BigDecimal.class.getName() + " setScale(int)");
        toFind = new ArrayList<MCAMethod>();
        toFind.add(setScale);
        classVisitor = new MCAClassVisitor(toFind);
        testee = new MCAMethodVisitor(classVisitor, toFind);
    }

    @AfterEach
    public final void tearDown() {
        classVisitor = null;
        toFind = null;
        testee = null;
    }

    @Test
    public final void testConstructor() {

        // PREPARE & TEST
        // => Done in setup

        // VERIFY
        assertThat(testee.getClassVisitor()).isSameAs(classVisitor);
        assertThat(testee.getToFind()).containsOnly(setScale);

    }

    @Test
    public final void testVisitMethodInsn() {

        // PREPARE
        assertThat(testee.isFound()).isFalse();

        // TEST
        testee.visitMethodInsn(0, BigDecimal.class.getName().replace('.', '/'), setScale.getMethod().getName(),
                setScale.getMethod().getDescriptor(), false);

        // VERIFY
        assertThat(testee.isFound()).isTrue();

    }

    @Test
    public final void testVisitCode() {

        // PREPARE
        testee.visitMethodInsn(0, BigDecimal.class.getName().replace('.', '/'), setScale.getMethod().getName(),
                setScale.getMethod().getDescriptor(), false);
        assertThat(testee.isFound()).isTrue();

        // TEST
        testee.visitCode();

        // VERIFY
        assertThat(testee.isFound()).isFalse();

    }

    @Test
    public final void testAll() {

        // PREPARE
        classVisitor.visit(0, 0, "a.b.c.MyClass", null, null, null);
        classVisitor.visitMethod(0, "myMethod", "(Ljava/lang/String;)I", null, null);
        testee.visitCode();
        testee.visitMethodInsn(0, BigDecimal.class.getName().replace('.', '/'), setScale.getMethod().getName(),
                setScale.getMethod().getDescriptor(), false);

        // TEST
        testee.visitLineNumber(123, null);
        testee.visitEnd();

        // VERIFY
        assertThat(testee.getClassVisitor().getMethodCalls()).hasSize(1);

    }

}
// CHECKSTYLE:ON
