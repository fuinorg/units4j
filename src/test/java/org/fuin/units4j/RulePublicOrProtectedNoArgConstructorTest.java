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
package org.fuin.units4j;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class RulePublicOrProtectedNoArgConstructorTest {

    @Test
    public final void testVerifyValidPublicConstructor() throws IOException {

        // PREPARE
        final RulePublicOrProtectedNoArgConstructor testee = new RulePublicOrProtectedNoArgConstructor();

        // TEST
        final AssertionResult result = testee.verify(classInfo(PublicConstructorClass.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testVerifyValidProtectedConstructor() throws IOException {

        // PREPARE
        final RulePublicOrProtectedNoArgConstructor testee = new RulePublicOrProtectedNoArgConstructor();

        // TEST
        final AssertionResult result = testee.verify(classInfo(ProtectedConstructorClass.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testVerifyValidNoConstructor() throws IOException {

        // PREPARE
        final RulePublicOrProtectedNoArgConstructor testee = new RulePublicOrProtectedNoArgConstructor();

        // TEST
        final AssertionResult result = testee.verify(classInfo(NoConstructorClass.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testVerifyInvalidPackageConstructor() throws IOException {

        // PREPARE
        final RulePublicOrProtectedNoArgConstructor testee = new RulePublicOrProtectedNoArgConstructor();

        // TEST
        final AssertionResult result = testee.verify(classInfo(PackageConstructorClass.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).startsWith("Missing public or protected no arg constructor:");

    }

    @Test
    public final void testVerifyInvalidPrivateConstructor() throws IOException {

        // PREPARE
        final RulePublicOrProtectedNoArgConstructor testee = new RulePublicOrProtectedNoArgConstructor();

        // TEST
        final AssertionResult result = testee.verify(classInfo(PrivateConstructorClass.class));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).startsWith("Missing public or protected no arg constructor:");

    }

    private ClassInfo classInfo(final Class<?> clasz) throws IOException {
        final String className = clasz.getName();
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(
                className.replace('.', '/') + ".class");
        final Indexer indexer = new Indexer();
        indexer.index(stream);
        final Index index = indexer.complete();
        return index.getClassByName(DotName.createSimple(className));
    }

    public static class PublicConstructorClass {
        public PublicConstructorClass() {
        }
    }

    public static class ProtectedConstructorClass {
        protected ProtectedConstructorClass() {
        }
    }

    public static class PackageConstructorClass {
        PackageConstructorClass() {
        }
    }

    public static class PrivateConstructorClass {
        private PrivateConstructorClass() {
        }
    }

    public static class NoConstructorClass {
    }

}
// CHECKSTYLE:ON
