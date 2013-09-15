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

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import org.fuin.units4j.analyzer.MCAMethod;
import org.fuin.units4j.analyzer.MCAMethodCall;
import org.fuin.units4j.analyzer.MethodCallAnalyzer;
import org.fuin.utils4j.Utils4J;
import org.junit.Assert;

/**
 * Assertion tool class for checking usage of some code.
 */
public final class AssertUsage {

    /**
     * Private default constructor.
     */
    private AssertUsage() {
        throw new UnsupportedOperationException(
                "This utility class is not intended to be instanciated!");
    }

    /**
     * Asserts that a set of methods is not used.
     * 
     * @param classesDir
     *            Directory with the ".class" files to check - Cannot be
     *            <code>null</code> and must be a valid directory.
     * @param filter
     *            File filter or NULL (process all '*.class' files).
     * @param methodsToFind
     *            List of methods to find.
     */
    public static final void assertMethodsNotUsed(final File classesDir, final FileFilter filter,
            final MCAMethod... methodsToFind) {

        Utils4J.checkNotNull("methodsToFind", methodsToFind);
        assertMethodsNotUsed(classesDir, filter, Arrays.asList(methodsToFind));

    }

    /**
     * Asserts that a set of methods is not used.
     * 
     * @param classesDir
     *            Directory with the ".class" files to check - Cannot be
     *            <code>null</code> and must be a valid directory.
     * @param filter
     *            File filter or NULL (process all '*.class' files).
     * @param methodsToFind
     *            List of methods to find - Cannot be NULL.
     */
    public static final void assertMethodsNotUsed(final File classesDir, final FileFilter filter,
            final List<MCAMethod> methodsToFind) {

        Utils4J.checkNotNull("classesDir", classesDir);
        Utils4J.checkValidDir(classesDir);
        Utils4J.checkNotNull("methodsToFind", methodsToFind);

        final MethodCallAnalyzer analyzer = new MethodCallAnalyzer(methodsToFind);
        analyzer.findCallingMethodsInDir(classesDir, filter);

        final List<MCAMethodCall> methodCalls = analyzer.getMethodCalls();
        if (methodCalls.size() > 0) {
            final StringBuffer sb = new StringBuffer("Illegal method call(s) found:");
            for (final MCAMethodCall methodCall : methodCalls) {
                sb.append("\n");
                sb.append(methodCall);
            }
            Assert.fail(sb.toString());
        }

    }

}
