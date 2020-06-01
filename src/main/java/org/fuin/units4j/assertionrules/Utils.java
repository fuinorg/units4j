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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.fuin.utils4j.Utils4J;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;
import org.jboss.jandex.Type.Kind;

/**
 * Utilities for the package.
 */
public final class Utils {

    /**
     * Private default constructor.
     */
    private Utils() {
        throw new UnsupportedOperationException("This utility class is not intended to be instanciated!");
    }

    /**
     * Verifies if a list of annotations contains any of the given ones.
     * 
     * @param annotations
     *            List with annotations to check.
     * @param annotationClaszNames
     *            List of expected full qualified name of annotation classes.
     * 
     * @return TRUE if the list contains at least one of the expected annotation, else FALSE.
     */
    public static boolean hasOneOfAnnotations(@NotNull final List<AnnotationInstance> annotations,
            @NotNull final List<String> annotationClaszNames) {

        for (final String annotationClaszName : annotationClaszNames) {
            if (hasAnnotation(annotations, annotationClaszName)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Verifies if a list of annotations contains a given one.
     * 
     * @param annotations
     *            List with annotations to check.
     * @param annotationClaszName
     *            Full qualified name of annotation class to find.
     * 
     * @return TRUE if the list contains the annotation, else FALSE.
     */
    public static boolean hasAnnotation(@NotNull final List<AnnotationInstance> annotations, @NotNull final String annotationClaszName) {
        Utils4J.checkNotNull("annotations", annotations);
        Utils4J.checkNotNull("annotationClaszName", annotationClaszName);

        final DotName annotationName = DotName.createSimple(annotationClaszName);
        for (final AnnotationInstance annotation : annotations) {
            if (annotation.name().equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a map for parameter annotations. The key id the index (zero based) of the parameter and the value is a list of all
     * annotations.
     * 
     * @param method
     *            Method to create a map for.
     * 
     * @return Map with parameter index (key) and list of annotations (value).
     */
    public static Map<Integer, List<AnnotationInstance>> createParameterAnnotationMap(@NotNull final MethodInfo method) {
        Utils4J.checkNotNull("method", method);

        final Map<Integer, List<AnnotationInstance>> result = new HashMap<>();
        final List<Type> parameters = method.parameters();
        for (int i = 0; i < parameters.size(); i++) {
            result.put(i, parameters.get(i).annotations());
        }
        return result;
    }

    /**
     * Creates the list of annotations found only on the return type (not on parameters).
     * 
     * @param method
     *            Method to create a list for.
     * 
     * @return List of annotations.
     */
    public static List<AnnotationInstance> createReturnTypeAnnotationList(@NotNull final MethodInfo method) {
        Utils4J.checkNotNull("method", method);

        final List<AnnotationInstance> result = new ArrayList<>();
        if (method.returnType().kind() != Kind.VOID) {
            for (AnnotationInstance ai : method.returnType().annotations()) {
                result.add(ai);
            }
        }
        return result;
    }

    /**
     * Returns a list of all methods the given one overrides. Such methods can be found in interfaces and super classes.
     * 
     * @param index
     *            Index with all known classes.
     * @param method
     *            Method signature to find.
     * 
     * @return List of methods the given one overrides.
     */
    public static List<MethodInfo> findOverrideMethods(@NotNull final Index index, @NotNull final MethodInfo method) {
        Utils4J.checkNotNull("index", index);
        Utils4J.checkNotNull("method", method);

        return findOverrideMethods(index, method.declaringClass(), method, 0);

    }

    private static List<MethodInfo> findOverrideMethods(@NotNull final Index index, final ClassInfo clasz,
            @NotNull final MethodInfo methodToFind, final int level) {

        Utils4J.checkNotNull("index", index);
        Utils4J.checkNotNull("methodToFind", methodToFind);

        final List<MethodInfo> methods = new ArrayList<>();
        if (clasz != null) {

            // Super classes
            if (level > 0) {
                addIfFound(clasz, methodToFind, methods);
            }

            // Check interfaces
            methods.addAll(findInterfaceMethods(index, clasz, methodToFind));

            // Check super class
            final DotName superName = clasz.superName();
            final ClassInfo superClass = index.getClassByName(superName);
            methods.addAll(findOverrideMethods(index, superClass, methodToFind, (level + 1)));

        }
        return methods;

    }

    private static List<MethodInfo> findInterfaceMethods(@NotNull final Index index, final ClassInfo clasz,
            @NotNull final MethodInfo methodToFind) {

        Utils4J.checkNotNull("index", index);
        Utils4J.checkNotNull("methodToFind", methodToFind);

        final List<MethodInfo> methods = new ArrayList<>();
        if (clasz != null) {
            final List<DotName> interfaceNames = clasz.interfaceNames();
            for (final DotName interfaceName : interfaceNames) {
                final ClassInfo intf = index.getClassByName(interfaceName);
                addIfFound(intf, methodToFind, methods);
                // Check extended super interfaces
                methods.addAll(findInterfaceMethods(index, intf, methodToFind));
            }
        }
        return methods;
    }

    private static void addIfFound(final ClassInfo clasz, final MethodInfo methodToFind, final List<MethodInfo> methods) {
        if (clasz != null) {
            final MethodInfo foundMethod = clasz.method(methodToFind.name(),
                    methodToFind.parameters().toArray(new Type[methodToFind.parameters().size()]));
            if (foundMethod != null) {
                methods.add(foundMethod);
            }
        }
    }

}
