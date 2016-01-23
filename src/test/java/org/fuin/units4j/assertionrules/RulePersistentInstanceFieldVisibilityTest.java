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

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.fuin.units4j.AssertionResult;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class RulePersistentInstanceFieldVisibilityTest {

    @Test
    public final void testVerifyValid() throws IOException {

        // PREPARE
        final RulePersistentInstanceFieldVisibility testee = new RulePersistentInstanceFieldVisibility();

        // TEST + VERIFY
        final AssertionResult resultEntity = testee.verify(classInfo(ValidEntity.class));
        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.isValid()).isTrue();
        assertThat(resultEntity.getErrorMessage()).isEqualTo("");

        // TEST + VERIFY
        final AssertionResult resultSuper = testee.verify(classInfo(ValidSuper.class));
        assertThat(resultSuper).isNotNull();
        assertThat(resultSuper.isValid()).isTrue();
        assertThat(resultSuper.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testVerifyInvalid() throws IOException {

        // PREPARE
        final RulePersistentInstanceFieldVisibility testee = new RulePersistentInstanceFieldVisibility();

        // TEST + VERIFY
        final AssertionResult resultEntity = testee.verify(classInfo(InvalidEntity.class));
        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.isValid()).isFalse();
        assertThat(resultEntity.getErrorMessage()).isEqualTo(
                "Public visibility is not allowed for: " + InvalidEntity.class.getName() + ".publicField\n");

        // TEST + VERIFY
        final AssertionResult resultSuper = testee.verify(classInfo(InvalidSuper.class));
        assertThat(resultSuper).isNotNull();
        assertThat(resultSuper.isValid()).isFalse();
        assertThat(resultSuper.getErrorMessage()).isEqualTo(
                "Public visibility is not allowed for: " + InvalidSuper.class.getName() + ".publicField\n");

    }

    @MappedSuperclass
    @SuppressWarnings("unused")
    public static class ValidSuper {

        public static String publicStaticField;
        protected static String protectedStaticField;
        private static String privateStaticField;
        static String packageStaticField;

        private String privateField;
        protected String protectedField;
        String packageField;

    }

    @Entity
    @SuppressWarnings("unused")
    public static class ValidEntity extends ValidSuper {

        public static String publicStaticField;
        protected static String protectedStaticField;
        private static String privateStaticField;
        static String packageStaticField;

        private String privateField;
        protected String protectedField;
        String packageField;

    }

    @MappedSuperclass
    @SuppressWarnings("unused")
    public static class InvalidSuper {

        public static String publicStaticField;
        protected static String protectedStaticField;
        private static String privateStaticField;
        static String packageStaticField;

        private String privateField;
        protected String protectedField;
        String packageField;
        public String publicField;

    }

    @Entity
    @SuppressWarnings("unused")
    public static class InvalidEntity extends ValidSuper {

        public static String publicStaticField;
        protected static String protectedStaticField;
        private static String privateStaticField;
        static String packageStaticField;

        private String privateField;
        protected String protectedField;
        String packageField;
        public String publicField;

    }

}
// CHECKSTYLE:ON
