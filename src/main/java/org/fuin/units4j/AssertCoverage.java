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

import org.fuin.utils4j.Utils4J;
import org.fuin.utils4j.fileprocessor.FileHandlerResult;
import org.fuin.utils4j.fileprocessor.FileProcessor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Assertion tool class for checking the test coverage.
 *
 * @deprecated Use <a href="https://www.archunit.org/">ArchUnit</a> rules instead.
 *
 * <code>{@code
 * &#64;AnalyzeClasses(packagesOf = AClassInRootPackage.class, importOptions = ImportOption.DoNotIncludeTests.class)
 * class ArchitectureTest {
 *     &#64;ArchTest
 *     static final ArchRule all_classes_should_have_tests =
 *         classes()
 *             .that()
 *                 .areTopLevelClasses()
 *                 .and().areNotInterfaces()
 *                 .and().areNotRecords()
 *                 .and().areNotEnums()
 *                 .and().doNotHaveModifier(JavaModifier.ABSTRACT)
 *                 .and().areNotAnnotatedWith(ArchIgnore.class)
 *                 .should(haveACorrespondingClassEndingWith("Test"));
 * }
 * }</code>
 */
@Deprecated
public final class AssertCoverage {

    /**
     * Private default constructor.
     */
    private AssertCoverage() {
        throw new UnsupportedOperationException("This utility class is not intended to be instanciated!");
    }

    /**
     * Asserts that a every class has at least one test class. It's assumed that the name of the test class follows the pattern
     * <code>XxxxxTest</code>, where <code>Xxxxx</code> is the name of the class that is tested. The class must contain at least one method
     * annotated with {@link Test}.
     * 
     * @param classes
     *            Set of classes - Cannot be <code>null</code>.
     */
    public static final void assertEveryClassHasATest(final Set<Class<?>> classes) {
        Utils4J.checkNotNull("classes", classes);

        final StringBuilder sb = new StringBuilder();

        for (final Class<?> clasz : classes) {
            final String testClassName = clasz.getName() + "Test";
            try {
                final Class<?> testClass = Class.forName(testClassName);
                if (!hasTestMethod(testClass)) {
                    sb.append("\nThe test class '" + testClassName + "' contains no methods annotated with @Test");
                }
            } catch (final ClassNotFoundException ex) {
                sb.append("\nNo test class found for '" + clasz.getName() + "'");
            }

        }

        if (sb.length() > 0) {
            fail(sb.toString());
        }

    }

    /**
     * Asserts that a every class in the directory (or it's sub directories) has at least one test class. It's assumed that the name of the
     * test class follows the pattern <code>XxxxxTest</code>, where <code>Xxxxx</code> is the name of the class that is tested. The class
     * must contain at least one method annotated with {@link Test}. No special class filter applies.
     * 
     * @param baseDir
     *            Root source directory like ("src/main/java").
     */
    public static final void assertEveryClassHasATest(final File baseDir) {
        assertEveryClassHasATest(baseDir, clasz -> true);
    }

    /**
     * Asserts that a every class in the directory (or it's sub directories) has at least one test class. It's assumed that the name of the
     * test class follows the pattern <code>XxxxxTest</code>, where <code>Xxxxx</code> is the name of the class that is tested. The class
     * must contain at least one method annotated with {@link org.junit.jupiter.api.Test}.
     * 
     * @param baseDir
     *            Root source directory like ("src/main/java").
     * @param classFilter
     *            Filter that decides if a class should have a corresponding test or not.
     */
    public static final void assertEveryClassHasATest(final File baseDir, final ClassFilter classFilter) {
        assertEveryClassHasATest(baseDir, true, classFilter);
    }

    /**
     * Asserts that a every class in the directory (or it's sub directories) has at least one test class. It's assumed that the name of the
     * test class follows the pattern <code>XxxxxTest</code>, where <code>Xxxxx</code> is the name of the class that is tested. The class
     * must contain at least one method annotated with {@link Test}. No special class filter applies.
     * 
     * @param baseDir
     *            Root source directory like ("src/main/java").
     * @param recursive
     *            Should sub directories be included?
     */
    public static final void assertEveryClassHasATest(final File baseDir, final boolean recursive) {
        assertEveryClassHasATest(baseDir, recursive, clasz -> true);
    }

    /**
     * Asserts that a every class in the directory (or it's sub directories) has at least one test class. It's assumed that the name of the
     * test class follows the pattern <code>XxxxxTest</code>, where <code>Xxxxx</code> is the name of the class that is tested. The class
     * must contain at least one method annotated with {@link Test}.
     * 
     * @param baseDir
     *            Root source directory like ("src/main/java").
     * @param recursive
     *            Should sub directories be included?
     * @param classFilter
     *            Filter that decides if a class should have a corresponding test or not.
     */
    public static final void assertEveryClassHasATest(final File baseDir, final boolean recursive, final ClassFilter classFilter) {
        Utils4J.checkNotNull("baseDir", baseDir);
        final Set<Class<?>> classes = new HashSet<>();
        analyzeDir(classes, baseDir, baseDir, recursive, classFilter);
        assertEveryClassHasATest(classes);
    }

    /**
     * Populates a list of classes from a given java source directory. All source files must have a ".class" file in the class path.
     * 
     * @param classes
     *            Set to populate.
     * @param baseDir
     *            Root directory like "src/main/java".
     * @param srcDir
     *            A directory inside the root directory like "a/b/c" (path of the package "a.b.c").
     * @param recursive
     *            If sub directories should be included <code>true</code> else <code>false</code>.
     * @param classFilter
     *            Filter that decides if a class should have a corresponding test or not.
     */
    static void analyzeDir(final Set<Class<?>> classes, final File baseDir, final File srcDir, final boolean recursive,
            final ClassFilter classFilter) {

        final FileProcessor fileProcessor = new FileProcessor(file -> {
            if (file.isDirectory()) {
                // Directory
                if (recursive) {
                    return FileHandlerResult.CONTINUE;
                }
                return FileHandlerResult.SKIP_SUBDIRS;
            }
            // File
            final String name = file.getName();
            if (name.endsWith(".java") && !name.equals("package-info.java")) {
                final String packageName = Utils4J.getRelativePath(baseDir, file.getParentFile()).replace(File.separatorChar, '.');
                final String simpleName = name.substring(0, name.length() - 5);
                final String className = packageName + "." + simpleName;
                final Class<?> clasz = classForName(className);
                if (isInclude(clasz, classFilter)) {
                    classes.add(clasz);
                }
            }
            return FileHandlerResult.CONTINUE;
        });

        fileProcessor.process(srcDir);

    }

    private static Class<?> classForName(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Determines if the class meets the following conditions. <br>
     * <ul>
     * <li>Class filter returns TRUE</li>
     * <li>Not an annotation</li>
     * <li>Not an enumeration</li>
     * <li>Not an interface</li>
     * <li>Not abstract</li>
     * </ul>
     * 
     * @param clasz
     *            Class to check.
     * @param classFilter
     *            Additional filter to use.
     * 
     * @return If the class meets the conditions TRUE, else FALSE.
     */
    static boolean isInclude(final Class<?> clasz, final ClassFilter classFilter) {
        final int modifiers = clasz.getModifiers();
        return classFilter.isIncludeClass(clasz) && !clasz.isAnnotation() && !clasz.isEnum() && !clasz.isInterface()
                && !Modifier.isAbstract(modifiers);
    }

    /**
     * Checks if a given class has at least one method annotated with {@link Test}.
     * 
     * @param testClass
     *            Class to check.
     * 
     * @return If there is a test method <code>true</code> else <code>false</code>.
     */
    static boolean hasTestMethod(final Class<?> testClass) {
        if (testClass.getAnnotation(TestOmitted.class) != null) {
            return true;
        }
        boolean found = false;
        final Method[] methods = testClass.getMethods();
        for (final Method method : methods) {
            final Annotation testAnnotation = method.getAnnotation(org.junit.jupiter.api.Test.class);
            if (testAnnotation != null) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Helper class to filter found classes.
     */
    public static interface ClassFilter {

        /**
         * Determines if the class should be included in the list of relevant classes.
         * 
         * @param clasz
         *            Class to check.
         * 
         * @return If the class should have a test class TRUE, else FALSE.
         */
        public boolean isIncludeClass(Class<?> clasz);

    }

    /**
     * Uses a list of classes to exclude from the check.
     */
    public static class ExcludeListClassFilter implements ClassFilter {

        private final List<Class<?>> excludedClasses;

        /**
         * Constructor with array of excluded class names.
         * 
         * @param fqClassNames
         *            Array of full qualified class names to exclude.
         */
        public ExcludeListClassFilter(final String... fqClassNames) {
            super();
            this.excludedClasses = new ArrayList<>();
            if (fqClassNames != null) {
                for (final String className : fqClassNames) {
                    final Class<?> clasz = classForName(className);
                    this.excludedClasses.add(clasz);
                }
            }
        }

        /**
         * Constructor with array of excluded classes.
         * 
         * @param excludedClasses
         *            List of classes.
         */
        public ExcludeListClassFilter(final Class<?>... excludedClasses) {
            super();
            this.excludedClasses = new ArrayList<>();
            if (excludedClasses != null) {
                for (final Class<?> clasz : excludedClasses) {
                    this.excludedClasses.add(clasz);
                }
            }
        }

        /**
         * Checks if the class is in the exclude list.
         * 
         * @param clasz
         *            Class to check.
         * 
         * @return If the class is not in the exclude list TRUE, else FALSE.
         */
        public final boolean isIncludeClass(final Class<?> clasz) {
            return !excludedClasses.contains(clasz);
        }

    }

    /**
     * Filter that combines two or more filters.
     */
    public static class AndClassFilter implements ClassFilter {

        private final ClassFilter[] classFilters;

        /**
         * Constructor with an array of sub filters.
         * 
         * @param classFilters
         *            Filter list.
         */
        public AndClassFilter(final ClassFilter... classFilters) {
            super();
            if (classFilters == null) {
                throw new IllegalArgumentException("Argument 'classFilters' cannot be null");
            }
            if (classFilters.length < 2) {
                throw new IllegalArgumentException("Argument 'classFilters' is less than two: " + classFilters.length);
            }
            this.classFilters = classFilters;
        }

        /**
         * Checks if all sub filters are returning TRUE.
         * 
         * @param clasz
         *            Class to check.
         * 
         * @return If all sub checks are positive this returns TRUE, else FALSE.
         */
        public final boolean isIncludeClass(final Class<?> clasz) {
            for (ClassFilter classFilter : classFilters) {
                if (!classFilter.isIncludeClass(clasz)) {
                    return false;
                }
            }
            return true;
        }

    }

}
