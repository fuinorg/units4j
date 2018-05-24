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
package org.fuin.units4j.assertionrules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.units4j.Units4JUtils.classInfo;

import java.io.IOException;

import org.fuin.units4j.AssertionResult;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class RuleClassHasNoFinalMethodsTest {

    @Test
    public final void testVerifyValidProtectedConstructor() throws IOException {

        // PREPARE
        final RuleClassHasNoFinalMethods testee = new RuleClassHasNoFinalMethods();

        // TEST
        final AssertionResult result = testee.verify(classInfo(ClassWithoutFinalMethods.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testVerifyInvalidClass() throws IOException {

        // PREPARE
        final RuleClassHasNoFinalMethods testee = new RuleClassHasNoFinalMethods();

        // TEST
        final AssertionResult result = testee.verify(classInfo(ClassWithFinalMethods.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo(
                "Class " + ClassWithFinalMethods.class.getName() + " has final methods:\n" + "void a()\n" + "java.lang.Integer b()\n");

    }

    public static class ClassWithoutFinalMethods {

        public void a() {
        }

        public Integer b() {
            return null;
        }

        protected boolean c() {
            return true;
        }

    }

    public static class ClassWithFinalMethods {
        public final void a() {
        }

        public final Integer b() {
            return null;
        }

        protected boolean c() {
            return true;
        }
    }

}
// CHECKSTYLE:ON
