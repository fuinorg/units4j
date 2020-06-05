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

import java.io.File;
import java.util.List;

import org.fuin.units4j.dependency.Dependencies;
import org.fuin.units4j.dependency.DependencyAnalyzer;
import org.fuin.units4j.dependency.DependencyError;
import org.fuin.units4j.dependency.InvalidDependenciesDefinitionException;
import org.fuin.utils4j.Utils4J;
import org.junit.Assert;

/**
 * Assertion tool class for checking dependencies.
 */
public final class AssertDependencies {

    /**
     * Private default constructor.
     */
    private AssertDependencies() {
        throw new UnsupportedOperationException("This utility class is not intended to be instanciated!");
    }

    private static void assertIntern(final File classesDir, final DependencyAnalyzer analyzer) {
        analyzer.analyze(classesDir);
        final List<DependencyError> dependencyErrors = analyzer.getDependencyErrors();
        if (!dependencyErrors.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dependencyErrors.size(); i++) {
                sb.append("\n");
                sb.append(dependencyErrors.get(i));
            }
            Assert.fail(sb.toString());
        }
    }

    /**
     * Asserts that a set of dependency rules is kept.
     * 
     * @param dependencies
     *            Definition of allowed or forbidden dependencies - Cannot be <code>null</code>.
     * @param classesDir
     *            Directory with the ".class" files to check - Cannot be <code>null</code> and must be a valid directory.
     */
    public static final void assertRules(final Dependencies dependencies, final File classesDir) {
        Utils4J.checkNotNull("dependencies", dependencies);
        Utils4J.checkNotNull("classesDir", classesDir);
        Utils4J.checkValidDir(classesDir);
        try {
            final DependencyAnalyzer analyzer = new DependencyAnalyzer(dependencies);
            assertIntern(classesDir, analyzer);
        } catch (final InvalidDependenciesDefinitionException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Asserts that a set of dependency rules is kept.
     * 
     * @param file
     *            The XML rules file - Cannot be <code>null</code> and must be a valid file.
     * @param classesDir
     *            Directory with the ".class" files to check - Cannot be <code>null</code> and must be a valid directory.
     */
    public static final void assertRules(final File file, final File classesDir) {
        Utils4J.checkNotNull("file", file);
        Utils4J.checkNotNull("classesDir", classesDir);
        Utils4J.checkValidFile(file);
        Utils4J.checkValidDir(classesDir);
        try {
            final DependencyAnalyzer analyzer = new DependencyAnalyzer(file);
            assertIntern(classesDir, analyzer);
        } catch (final InvalidDependenciesDefinitionException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Asserts that a set of dependency rules is kept.
     * 
     * @param clasz
     *            Class to use for loading the resource - Cannot be <code>null</code>.
     * @param dependenciesFilePathAndName
     *            XML resource (path/name) with allowed or forbidden dependencies - Cannot be <code>null</code>.
     * @param classesDir
     *            Directory with the ".class" files to check - Cannot be <code>null</code> and must be a valid directory.
     */
    public static final void assertRules(final Class<?> clasz, final String dependenciesFilePathAndName, final File classesDir) {
        Utils4J.checkNotNull("clasz", clasz);
        Utils4J.checkNotNull("dependenciesFilePathAndName", dependenciesFilePathAndName);
        Utils4J.checkNotNull("classesDir", classesDir);
        try {
            final DependencyAnalyzer analyzer = new DependencyAnalyzer(clasz, dependenciesFilePathAndName);
            assertIntern(classesDir, analyzer);
        } catch (final InvalidDependenciesDefinitionException ex) {
            throw new RuntimeException(ex);
        }
    }

}
