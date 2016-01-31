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

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.MethodParameterInfo;
import org.jboss.jandex.Type;

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
     * Verifies if a list of annotations contains a given one.
     * 
     * @param annotations
     *            List with annotations to check.
     * @param annotationClaszName
     *            Full qualified name of annotation class to find.
     * 
     * @return TRUE if the list contains the annotation, else FALSE.
     */
    public static boolean hasAnnotation(final List<AnnotationInstance> annotations,
            final String annotationClaszName) {
        final DotName annotationName = DotName.createSimple(annotationClaszName);
        for (final AnnotationInstance annotation : annotations) {
            if (annotation.name().equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a map for parameter annotations. The key id the index (zero based) of the parameter and the
     * value is a list of all annotations.
     * 
     * @param method
     *            Method to create a map for.
     * 
     * @return Map with parameter index (key) and list of annotations (value).
     */
    public static Map<Integer, List<AnnotationInstance>> createParameterAnnotationMap(
            final MethodInfo method) {
        final Map<Integer, List<AnnotationInstance>> result = new HashMap<>();
        for (AnnotationInstance ai : method.annotations()) {
            final AnnotationTarget at = ai.target();
            if (at.kind() == AnnotationTarget.Kind.METHOD_PARAMETER) {
                final MethodParameterInfo mp = at.asMethodParameter();
                final int pos = (int) mp.position();
                List<AnnotationInstance> list = result.get(pos);
                if (list == null) {
                    list = new ArrayList<>();
                    result.put(pos, list);
                }
                list.add(ai);
            }
        }
        return result;
    }

    /**
     * Creates the list of annotations found only on the method (not on parameters).
     * 
     * @param method
     *            Method to create a list for.
     * 
     * @return List of annotations.
     */
    public static List<AnnotationInstance> createMethodAnnotationList(final MethodInfo method) {
        final List<AnnotationInstance> result = new ArrayList<>();
        for (AnnotationInstance ai : method.annotations()) {
            final AnnotationTarget at = ai.target();
            if (at.kind() == AnnotationTarget.Kind.METHOD) {
                result.add(ai);
            }
        }
        return result;
    }

    /**
     * Returns a list of all methods the given one overrides. Such methods can be found in interfaces and
     * super classes.
     * 
     * @param index
     *            Index with all known classes.
     * @param method
     *            Method signature to find.
     * 
     * @return List of methods the given one overrides.
     */
    public static List<MethodInfo> findOverrideMethods(final Index index, final MethodInfo method) {

        return findOverrideMethods(index, method.declaringClass(), method, 0);

    }

    private static List<MethodInfo> findOverrideMethods(final Index index, final ClassInfo clasz,
            final MethodInfo methodToFind, final int level) {

        final List<MethodInfo> methods = new ArrayList<>();
        if (clasz != null) {
            
            // Super classes
            if (level > 0) {
                addIfFound(index, clasz, methodToFind, methods);
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

    private static List<MethodInfo> findInterfaceMethods(final Index index, final ClassInfo clasz,
            final MethodInfo methodToFind) {

        final List<MethodInfo> methods = new ArrayList<>();
        if (clasz != null) {
            final List<DotName> interfaceNames = clasz.interfaceNames();
            for (final DotName interfaceName : interfaceNames) {
                final ClassInfo intf = index.getClassByName(interfaceName);
                addIfFound(index, intf, methodToFind, methods);
                // Check extended super interfaces
                methods.addAll(findInterfaceMethods(index, intf, methodToFind));
            }
        }
        return methods;
    }

    private static void addIfFound(final Index index, final ClassInfo clasz, final MethodInfo methodToFind,
            final List<MethodInfo> methods) {
        if (clasz != null) {
            final MethodInfo foundMethod = clasz.method(methodToFind.name(), methodToFind.parameters()
                    .toArray(new Type[methodToFind.parameters().size()]));
            if (foundMethod != null) {
                methods.add(foundMethod);
            }
        }
    }

}
