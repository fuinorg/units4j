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
package org.fuin.units4j.analyzer;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

/**
 * Method of a class.
 */
public final class MCAMethod implements Comparable<MCAMethod> {

    private final String className;

    private final String methodSignature;

    private final Method method;

    /**
     * Constructor with class and method.
     * 
     * @param className
     *            Full qualified class name.
     * @param methodSignature
     *            Method signature.
     */
    public MCAMethod(final String className, final String methodSignature) {
        super();
        if (className == null) {
            throw new IllegalArgumentException(
                    "Argument 'className' canot be NULL");
        }
        if (methodSignature == null) {
            throw new IllegalArgumentException(
                    "Argument 'methodSignature' canot be NULL");
        }
        this.className = className;
        this.methodSignature = methodSignature;
        this.method = Method.getMethod(methodSignature);
    }

    /**
     * Constructor with ASM arguments.
     * 
     * @param asmClassName
     *            ASM class name with '/'.
     * @param asmMethodName
     *            Method name.
     * @param asmMethodDescr
     *            ASM method description.
     */
    public MCAMethod(final String asmClassName, final String asmMethodName,
            final String asmMethodDescr) {

        if (asmClassName == null) {
            throw new IllegalArgumentException(
                    "Argument 'asmClassName' canot be NULL");
        }
        if (asmMethodName == null) {
            throw new IllegalArgumentException(
                    "Argument 'asmMethodName' canot be NULL");
        }
        if (asmMethodDescr == null) {
            throw new IllegalArgumentException(
                    "Argument 'asmMethodDescr' canot be NULL");
        }

        this.className = asmClassName.replace('/', '.');
        final int p = asmMethodDescr.indexOf(')');
        if (p < 0) {
            throw new IllegalStateException("Couldn't find closing bracket: "
                    + asmMethodDescr);
        }
        final StringBuilder sb = new StringBuilder("(");
        final Type[] argTypes = Type.getArgumentTypes(asmMethodDescr);
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(argTypes[i].getClassName());
            }
        }
        sb.append(")");
        final String returnType = Type.getReturnType(asmMethodDescr)
                .getClassName();
        this.methodSignature = returnType + " " + asmMethodName + sb;
        this.method = Method.getMethod(methodSignature);
    }

    /**
     * Returns the full qualified class name.
     * 
     * @return FQN of the class.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Returns the method.
     * 
     * @return Method.
     */
    public final Method getMethod() {
        return method;
    }

    // CHECKSTYLE:OFF Generated code

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((className == null) ? 0 : className.hashCode());
        result = prime * result
                + ((methodSignature == null) ? 0 : methodSignature.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MCAMethod other = (MCAMethod) obj;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        if (methodSignature == null) {
            if (other.methodSignature != null)
                return false;
        } else if (!methodSignature.equals(other.methodSignature))
            return false;
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public final int compareTo(final MCAMethod other) {
        final int i = className.compareTo(other.className);
        if (i != 0) {
            return i;
        }
        return methodSignature.compareTo(other.methodSignature);
    }

    @Override
    public final String toString() {
        return "Class='" + className + ", Method='" + methodSignature + "'";
    }

}
