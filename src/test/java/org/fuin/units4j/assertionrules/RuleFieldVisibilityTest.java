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

import org.fuin.units4j.AssertionResult;
import org.jboss.jandex.ClassInfo;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class RuleFieldVisibilityTest {

    private static ClassInfo CLASS_INFO = classInfo(MyClass.class);

    @Test
    public final void testVerifyPublic() {

        // PREPARE
        final RuleFieldVisibility testee = new RuleFieldVisibility(Visibility.PUBLIC);

        // TEST + VERIFY
        final AssertionResult resultPublic = testee.verify(CLASS_INFO.field("publicField"));
        assertThat(resultPublic).isNotNull();
        assertThat(resultPublic.isValid()).isTrue();
        assertThat(resultPublic.getErrorMessage()).isEqualTo("");

        // TEST + VERIFY
        final AssertionResult resultProtected = testee.verify(CLASS_INFO.field("protectedField"));
        assertThat(resultProtected).isNotNull();
        assertThat(resultProtected.isValid()).isFalse();
        assertThat(resultProtected.getErrorMessage())
                .isEqualTo("Protected visibility is not allowed for: " + MyClass.class.getName() + ".protectedField");

        // TEST + VERIFY
        final AssertionResult resultPrivate = testee.verify(CLASS_INFO.field("privateField"));
        assertThat(resultPrivate).isNotNull();
        assertThat(resultPrivate.isValid()).isFalse();
        assertThat(resultPrivate.getErrorMessage())
                .isEqualTo("Private visibility is not allowed for: " + MyClass.class.getName() + ".privateField");

        // TEST + VERIFY
        final AssertionResult resultPackage = testee.verify(CLASS_INFO.field("packageField"));
        assertThat(resultPackage).isNotNull();
        assertThat(resultPackage.isValid()).isFalse();
        assertThat(resultPackage.getErrorMessage())
                .isEqualTo("Package-private visibility is not allowed for: " + MyClass.class.getName() + ".packageField");

    }

    @Test
    public final void testVerifyProtected() {

        // PREPARE
        final RuleFieldVisibility testee = new RuleFieldVisibility(Visibility.PROTECTED);

        // TEST + VERIFY
        final AssertionResult resultPublic = testee.verify(CLASS_INFO.field("publicField"));
        assertThat(resultPublic).isNotNull();
        assertThat(resultPublic.isValid()).isFalse();
        assertThat(resultPublic.getErrorMessage())
                .isEqualTo("Public visibility is not allowed for: " + MyClass.class.getName() + ".publicField");

        // TEST + VERIFY
        final AssertionResult resultProtected = testee.verify(CLASS_INFO.field("protectedField"));
        assertThat(resultProtected).isNotNull();
        assertThat(resultProtected.isValid()).isTrue();
        assertThat(resultProtected.getErrorMessage()).isEqualTo("");

        // TEST + VERIFY
        final AssertionResult resultPrivate = testee.verify(CLASS_INFO.field("privateField"));
        assertThat(resultPrivate).isNotNull();
        assertThat(resultPrivate.isValid()).isFalse();
        assertThat(resultPrivate.getErrorMessage())
                .isEqualTo("Private visibility is not allowed for: " + MyClass.class.getName() + ".privateField");

        // TEST + VERIFY
        final AssertionResult resultPackage = testee.verify(CLASS_INFO.field("packageField"));
        assertThat(resultPackage).isNotNull();
        assertThat(resultPackage.isValid()).isFalse();
        assertThat(resultPackage.getErrorMessage())
                .isEqualTo("Package-private visibility is not allowed for: " + MyClass.class.getName() + ".packageField");

    }

    @Test
    public final void testVerifyPrivate() {

        // PREPARE
        final RuleFieldVisibility testee = new RuleFieldVisibility(Visibility.PRIVATE);

        // TEST + VERIFY
        final AssertionResult resultPublic = testee.verify(CLASS_INFO.field("publicField"));
        assertThat(resultPublic).isNotNull();
        assertThat(resultPublic.isValid()).isFalse();
        assertThat(resultPublic.getErrorMessage())
                .isEqualTo("Public visibility is not allowed for: " + MyClass.class.getName() + ".publicField");

        // TEST + VERIFY
        final AssertionResult resultProtected = testee.verify(CLASS_INFO.field("protectedField"));
        assertThat(resultProtected).isNotNull();
        assertThat(resultProtected.isValid()).isFalse();
        assertThat(resultProtected.getErrorMessage())
                .isEqualTo("Protected visibility is not allowed for: " + MyClass.class.getName() + ".protectedField");

        // TEST + VERIFY
        final AssertionResult resultPrivate = testee.verify(CLASS_INFO.field("privateField"));
        assertThat(resultPrivate).isNotNull();
        assertThat(resultPrivate.isValid()).isTrue();
        assertThat(resultPrivate.getErrorMessage()).isEqualTo("");

        // TEST + VERIFY
        final AssertionResult resultPackage = testee.verify(CLASS_INFO.field("packageField"));
        assertThat(resultPackage).isNotNull();
        assertThat(resultPackage.isValid()).isFalse();
        assertThat(resultPackage.getErrorMessage())
                .isEqualTo("Package-private visibility is not allowed for: " + MyClass.class.getName() + ".packageField");

    }

    @Test
    public final void testVerifyPackage() {

        // PREPARE
        final RuleFieldVisibility testee = new RuleFieldVisibility(Visibility.PACKAGE);

        // TEST + VERIFY
        final AssertionResult resultPublic = testee.verify(CLASS_INFO.field("publicField"));
        assertThat(resultPublic).isNotNull();
        assertThat(resultPublic.isValid()).isFalse();
        assertThat(resultPublic.getErrorMessage())
                .isEqualTo("Public visibility is not allowed for: " + MyClass.class.getName() + ".publicField");

        // TEST + VERIFY
        final AssertionResult resultProtected = testee.verify(CLASS_INFO.field("protectedField"));
        assertThat(resultProtected).isNotNull();
        assertThat(resultProtected.isValid()).isFalse();
        assertThat(resultProtected.getErrorMessage())
                .isEqualTo("Protected visibility is not allowed for: " + MyClass.class.getName() + ".protectedField");

        // TEST + VERIFY
        final AssertionResult resultPrivate = testee.verify(CLASS_INFO.field("privateField"));
        assertThat(resultPrivate).isNotNull();
        assertThat(resultPrivate.isValid()).isFalse();
        assertThat(resultPrivate.getErrorMessage())
                .isEqualTo("Private visibility is not allowed for: " + MyClass.class.getName() + ".privateField");

        // TEST + VERIFY
        final AssertionResult resultPackage = testee.verify(CLASS_INFO.field("packageField"));
        assertThat(resultPackage).isNotNull();
        assertThat(resultPackage.isValid()).isTrue();
        assertThat(resultPackage.getErrorMessage()).isEqualTo("");

    }

    public static class MyClass {

        public String publicField;

        protected String protectedField;

        private String privateField;

        String packageField;

    }

}
// CHECKSTYLE:ON
