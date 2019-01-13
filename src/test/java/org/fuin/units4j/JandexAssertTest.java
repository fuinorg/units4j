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
package org.fuin.units4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.units4j.JandexAssert.assertThat;
import static org.fuin.units4j.Units4JUtils.index;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;
import org.junit.Test;

/**
 * Tests the {@link JandexAssert} class.
 */
// CHECKSTYLE:OFF Test code
public class JandexAssertTest {

    @Test
    public void testAssertThat() {

        // TEST
        final Indexer indexer = new Indexer();
        final Index actual = indexer.complete();
        final JandexAssert result = JandexAssert.assertThat(actual);

        // VERIFY
        assertThat(result).isNotNull();

    }

    @Test
    public void testHasOnlyValidJpaEntitiesTrue() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), MyTestClass.class.getName());

        // TEST + VERIFY
        assertThat(index).hasOnlyValidJpaEntities();

    }

    @Test
    public void testHasOnlyValidJpaEntitiesFalse() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), MyInvalidEntity.class.getName());

        // TEST + VERIFY
        try {
            assertThat(index).hasOnlyValidJpaEntities();
        } catch (final AssertionError ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("Public visibility is not allowed for: org.fuin.units4j.JandexAssertTest$MyInvalidEntity.name\n");
        }
    }

    @Test
    public void testHasNullabilityInfoOnAllMethodsTrue() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), ValidNullabilityClass.class.getName());

        // TEST + VERIFY
        assertThat(index).hasNullabilityInfoOnAllMethods();

    }

    @Test
    public void testHasNullabilityInfoOnAllMethodsFalse() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), InvalidNullabilityClass.class.getName());

        // TEST + VERIFY
        try {
            assertThat(index).hasNullabilityInfoOnAllMethods();
        } catch (final AssertionError ex) {
            assertThat(ex.getMessage()).isEqualTo("A parameter or the return value has neither a @NotNull nor a @Nullable annotation:\n"
                    + "org.fuin.units4j.JandexAssertTest$InvalidNullabilityClass\tvoid <init>(java.lang.String xyz)\tParameter #0 (java.lang.String)\n"
                    + "org.fuin.units4j.JandexAssertTest$InvalidNullabilityClass\tvoid b(java.lang.String b)\tParameter #0 (java.lang.String)\n"
                    + "org.fuin.units4j.JandexAssertTest$InvalidNullabilityClass\tjava.lang.String c(java.lang.String c)\tReturn type (java.lang.String)\n"
                    + "org.fuin.units4j.JandexAssertTest$InvalidNullabilityClass\tjava.lang.String c(java.lang.String c)\tParameter #0 (java.lang.String)\n");
        }

    }

    @Test
    public void testHasNullabilityInfoOnAllMethodsInterface() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), ValidNullabilityInterface.class.getName());

        // TEST + VERIFY
        assertThat(index).hasNullabilityInfoOnAllMethods();

    }

    @Test
    public void testHasNoFinalFieldsWithJsonbPropertyAnnotation() {

        // PREPARE
        final Index index = index(getClass().getClassLoader(), ClassWithFinalJsonbFields.class.getName());

        // TEST + VERIFY
        try {
            assertThat(index).hasNoFinalFieldsWithJsonbPropertyAnnotation();
        } catch (final AssertionError ex) {
            assertThat(ex.getMessage())
                    .isEqualTo("At least one field has a 'final' modifier and is annotated with '@JsonbProperty' at the same time:\n"
                            + "Modifier 'final' is not allowed for field with '@JsonbProperty': org.fuin.units4j.JandexAssertTest$ClassWithFinalJsonbFields.d");
        }
    }

    @Entity
    @Table(name = "INVALID_TABLE")
    public static class MyInvalidEntity implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @Min(1)
        @NotNull
        @Column(name = "ID", nullable = false)
        private Integer id;

        @NotNull
        @Column(name = "NAME", length = 50, nullable = false)
        public String name; // public field is not allowed for JPA entities

        public MyInvalidEntity() {
            super();
        }

    }

    public static class ValidNullabilityClass {

        public ValidNullabilityClass(@NotNull final String xyz) {
            super();
        }

        public void a() {
        }

        public void b(@NotNull final String b) {
        }

        @NotNull
        public String c(@NotNull final String c) {
            return null;
        }

    }

    public static class InvalidNullabilityClass {

        public InvalidNullabilityClass(final String xyz) {
            super();
        }

        public void a() {
        }

        public void b(final String b) {
        }

        public String c(final String c) {
            return null;
        }

    }

    public static interface ValidNullabilityInterface {

        public void okMethod1();

        public boolean okMethod2();

        @NotNull
        public Boolean okMethod3();

        public void okMethod4(int abc);

        public void okMethod5(@NotNull Integer abc);

    }

    @SuppressWarnings("unused")
    public static class ClassWithFinalJsonbFields {

        private final String a = "abc";

        private String b;

        @JsonbProperty("c")
        private String c;

        @JsonbProperty("d")
        private final String d = "def";

    }

}
// CHECKSTYLE:ON
