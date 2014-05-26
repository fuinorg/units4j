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
package org.fuin.units4j.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Visits a class and checks if one of a set of methods is called in that class.
 */
public final class MCAClassVisitor extends ClassVisitor {

    private final List<MCAMethodCall> calls;

    private final MCAMethodVisitor methodVisitor;

    private String source;

    private String className;

    private String methodName;

    private String methodDescr;

    /**
     * Constructor with method list.
     * 
     * @param methodsToFind
     *            Method calls to find.
     */
    public MCAClassVisitor(final List<MCAMethod> methodsToFind) {
        super(Opcodes.ASM5, new EmptyClassVisitor());
        if (methodsToFind == null) {
            throw new IllegalArgumentException(
                    "Argument 'methodsToFind' canot be NULL");
        }
        calls = new ArrayList<MCAMethodCall>();
        methodVisitor = new MCAMethodVisitor(this, methodsToFind);
    }

    @Override
    public final void visit(final int version, final int access,
            final String name, final String signature, final String superName,
            final String[] interfaces) {
        className = name;
    }

    @Override
    public final void visitSource(final String source, final String debug) {
        this.source = source;
    }

    @Override
    public final MethodVisitor visitMethod(final int access, final String name,
            final String desc, final String signature, final String[] exceptions) {

        methodName = name;
        methodDescr = desc;

        return methodVisitor;
    }

    /**
     * Adds the current method to the list of callers.
     * 
     * @param found
     *            Called method.
     * @param line
     *            Line number of the call.
     */
    public final void addCall(final MCAMethod found, final int line) {
        calls.add(new MCAMethodCall(found, className, methodName, methodDescr,
                source, line));
    }

    /**
     * Returns the list of method call.
     * 
     * @return Found method calls.
     */
    public final List<MCAMethodCall> getMethodCalls() {
        return calls;
    }

    /**
     * Clears the list of method calls.
     */
    public final void clearMethodCalls() {
        calls.clear();
    }

    /**
     * Returns the list of methods to find.
     * 
     * @return Method list.
     */
    public final List<MCAMethod> getMethodsToFind() {
        return methodVisitor.getToFind();
    }

    /**
     * Returns the source file name.
     * 
     * @return Source file name.
     */
    public final String getSource() {
        return source;
    }

    /**
     * Returns the class name.
     * 
     * @return Class name.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Returns the method name.
     * 
     * @return Method name.
     */
    public final String getMethodName() {
        return methodName;
    }

    /**
     * Returns the method description.
     * 
     * @return Method description.
     */
    public final String getMethodDescr() {
        return methodDescr;
    }

}
