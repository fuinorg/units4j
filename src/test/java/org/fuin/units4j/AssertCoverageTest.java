/**
 * Copyright (C) 2009 Future Invent Informationsmanagement GmbH. All rights
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

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.fuin.units4j.AssertCoverage.ClassFilter;
import org.fuin.utils4j.Utils4J;
import org.junit.Test;

import assertcoverage.AbstractExampleClass;
import assertcoverage.AnnotationExampleClass;
import assertcoverage.EnumExampleClass;
import assertcoverage.ExampleClass;
import assertcoverage.ExampleExcludedClass;
import assertcoverage.InterfaceExampleClass;

// TESTCODE:BEGIN
public final class AssertCoverageTest {

    private static final ClassFilter ALWAYS_TRUE = new ClassFilter() {
        public final boolean isIncludeClass(final Class<?> clasz) {
            return true;
        }
    };

    private static final ClassFilter ALWAYS_FALSE = new ClassFilter() {
        public final boolean isIncludeClass(final Class<?> clasz) {
            return false;
        }
    };

    @Test
    public final void testAssertEveryClassHasATest() {

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AssertCoverage.class);
        AssertCoverage.assertEveryClassHasATest(classes);

    }

    @Test(expected = AssertionError.class)
    public final void testAssertEveryClassHasATestError() {

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AssertCoverageTest.class);
        AssertCoverage.assertEveryClassHasATest(classes);

    }

    @Test
    public final void testAnalyzeDirUnfiltered() {

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final File baseDir = new File("src/test/java");
        final File srcDir = new File(baseDir, "org/fuin/units4j");
        AssertCoverage.analyzeDir(classes, baseDir, srcDir, false, ALWAYS_TRUE);

        assertThat(classes).contains(AssertCoverageTest.class);

    }

    @Test
    public final void testAnalyzeDirFilteredNoResult() {

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final File baseDir = new File("src/test/java");
        final File srcDir = new File(baseDir, "assertcoverage");

        AssertCoverage.analyzeDir(classes, baseDir, srcDir, false, new ClassFilter() {
            public boolean isIncludeClass(final Class<?> clasz) {
                return !clasz.getSimpleName().equals("ExampleClass")
                        && !clasz.equals(ExampleExcludedClass.class);
            }
        });
        assertThat(classes.size()).isEqualTo(0);

    }

    @Test
    public final void testAnalyzeDirFilteredSingleResult() {

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final File baseDir = new File("src/test/java");
        final File srcDir = new File(baseDir, "assertcoverage");

        AssertCoverage.analyzeDir(classes, baseDir, srcDir, false, ALWAYS_TRUE);
        assertThat(classes).contains(ExampleClass.class, ExampleExcludedClass.class);

    }

    @Test
    public final void testAnalyzeDirRecursive() {

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final File baseDir = new File("src/test/java");
        final File srcDir = new File(baseDir, "org/fuin/units4j");
        AssertCoverage.analyzeDir(classes, baseDir, srcDir, true, ALWAYS_TRUE);

        assertThat(classes).contains(AssertCoverageTest.class, BaseTest.class);

    }

    @Test
    public final void testIsInclude() {

        assertThat(AssertCoverage.isInclude(ExampleClass.class, ALWAYS_TRUE)).isTrue();
        assertThat(AssertCoverage.isInclude(ExampleClass.class, ALWAYS_FALSE)).isFalse();

        assertThat(AssertCoverage.isInclude(AbstractExampleClass.class, ALWAYS_TRUE)).isFalse();
        assertThat(AssertCoverage.isInclude(AnnotationExampleClass.class, ALWAYS_TRUE)).isFalse();
        assertThat(AssertCoverage.isInclude(EnumExampleClass.class, ALWAYS_TRUE)).isFalse();
        assertThat(AssertCoverage.isInclude(InterfaceExampleClass.class, ALWAYS_TRUE)).isFalse();

    }

    @Test
    public final void testHasTestMethod() {

        assertThat(AssertCoverage.hasTestMethod(ExampleClass.class)).isFalse();
        assertThat(AssertCoverage.hasTestMethod(AssertCoverageTest.class)).isTrue();

    }

    @Test
    public final void testExcludeListClassFilterOpenArray() {

        final AssertCoverage.ExcludeListClassFilter testee = new AssertCoverage.ExcludeListClassFilter(
                ExampleExcludedClass.class.getName());

        assertThat(testee.isIncludeClass(ExampleClass.class)).isTrue();
        assertThat(testee.isIncludeClass(ExampleExcludedClass.class)).isFalse();

    }

    @Test
    public final void testExcludeListClassFilterProperties() {

        final Properties excludes = Utils4J.loadProperties(ExampleExcludedClass.class,
                "excludeClasses.properties");
        final Set<Object> keySet = excludes.keySet();
        final String[] fqnClassNames = keySet.toArray(new String[keySet.size()]);

        final AssertCoverage.ExcludeListClassFilter testee = new AssertCoverage.ExcludeListClassFilter(
                fqnClassNames);

        assertThat(testee.isIncludeClass(ExampleClass.class)).isTrue();
        assertThat(testee.isIncludeClass(ExampleExcludedClass.class)).isFalse();

    }

    @Test
    public final void testExcludeListClassFilterClasses() {

        final AssertCoverage.ExcludeListClassFilter testee = new AssertCoverage.ExcludeListClassFilter(
                ExampleExcludedClass.class);

        assertThat(testee.isIncludeClass(ExampleClass.class)).isTrue();
        assertThat(testee.isIncludeClass(ExampleExcludedClass.class)).isFalse();

    }

    @Test
    public final void testAndClassFilter() {

        AssertCoverage.AndClassFilter testee;

        testee = new AssertCoverage.AndClassFilter(ALWAYS_FALSE, ALWAYS_TRUE);
        assertThat(testee.isIncludeClass(ExampleClass.class)).isFalse();
        assertThat(testee.isIncludeClass(ExampleExcludedClass.class)).isFalse();

        testee = new AssertCoverage.AndClassFilter(ALWAYS_TRUE, ALWAYS_TRUE);
        assertThat(testee.isIncludeClass(ExampleClass.class)).isTrue();
        assertThat(testee.isIncludeClass(ExampleExcludedClass.class)).isTrue();

        testee = new AssertCoverage.AndClassFilter(ALWAYS_FALSE, ALWAYS_FALSE);
        assertThat(testee.isIncludeClass(ExampleClass.class)).isFalse();
        assertThat(testee.isIncludeClass(ExampleExcludedClass.class)).isFalse();

    }

}
// TESTCODE:END

