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

import javax.validation.constraints.NotNull;

import org.fuin.units4j.AssertionResult;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;
import org.jboss.jandex.Type.Kind;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class RuleMethodHasNullabilityInfoTest {

    private static ClassInfo CLASS_INFO = classInfo(MyClass.class);

    private RuleMethodHasNullabilityInfo testee;

    @Before
    public void setup() {
        testee = new RuleMethodHasNullabilityInfo();
    }

    @After
    public void teardown() {
        testee = null;
    }

    @Test
    public final void testOkMethod1() {

        final AssertionResult result = testee.verify(CLASS_INFO.method("okMethod1"));
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testOkMethod2() {

        final AssertionResult result = testee.verify(CLASS_INFO.method("okMethod2"));
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testOkMethod3() {

        final AssertionResult result = testee.verify(CLASS_INFO.method("okMethod3"));
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testOkMethod4() {

        final AssertionResult result = testee.verify(CLASS_INFO.method("okMethod4",
                Type.create(DotName.createSimple(int.class.getName()), Kind.PRIMITIVE)));
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testokMethod5() {

        final AssertionResult result = testee.verify(CLASS_INFO.method("okMethod5",
                Type.create(DotName.createSimple(Integer.class.getName()), Kind.CLASS)));
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testFailedMethod1() {

        final MethodInfo method = CLASS_INFO.method("failedMethod1");
        final AssertionResult result = testee.verify(method);
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo(
                MyClass.class.getName() + "\t" + method + "\t" + "Return type (java.lang.Boolean)\n");

    }

    @Test
    public final void testFailedMethod2() {

        final MethodInfo method = CLASS_INFO.method("failedMethod2", type(Integer.class));
        final String location = MyClass.class.getName() + "\t" + method + "\t";

        final AssertionResult result = testee.verify(method);
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo(location + "Parameter #0 (java.lang.Integer)\n");

    }

    @Test
    public final void testFailedMethod3() {

        final MethodInfo method = CLASS_INFO.method("failedMethod3", type(Integer.class),
                type(boolean.class), type(Long.class), type(String.class));
        final String location = MyClass.class.getName() + "\t" + method + "\t";

        final AssertionResult result = testee.verify(method);
        assertThat(result).isNotNull();
        assertThat(result.getErrorMessage()).isEqualTo(
                location + "Return type (java.lang.Double)\n" + location
                        + "Parameter #0 (java.lang.Integer)\n" + location
                        + "Parameter #3 (java.lang.String)\n");

    }

    private Type type(final Class<?> clasz) {
        return Type.create(DotName.createSimple(clasz.getName()), Kind.CLASS);
    }

    public static class MyClass {

        public void okMethod1() {
            // Do nothing
        }

        public boolean okMethod2() {
            return true;
        }

        @NotNull
        public Boolean okMethod3() {
            return true;
        }

        public void okMethod4(int abc) {
            // Do nothing
        }

        public void okMethod5(@NotNull Integer abc) {
            // Do nothing
        }

        public Boolean failedMethod1() {
            return true;
        }

        public void failedMethod2(Integer abc) {
            // Do nothing
        }

        public Double failedMethod3(Integer one, boolean two, @NotNull Long three, String four) {
            return Double.MAX_VALUE;
        }

    }

}
// CHECKSTYLE:ON
