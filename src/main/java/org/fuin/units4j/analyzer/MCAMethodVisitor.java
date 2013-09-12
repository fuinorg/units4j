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

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * Visits a method and checks if one of a set of methods is called in that
 * method.
 */
public final class MCAMethodVisitor extends MethodAdapter {

    private final MCAClassVisitor classVisitor;

    private final List<MCAMethod> toFind;

    private MCAMethod found;

    private int line;

    /**
     * Constructor with parent and list of methods to locate.
     * 
     * @param classVisitor
     *            Parent.
     * @param toFind
     *            List of methods to find.
     */
    public MCAMethodVisitor(final MCAClassVisitor classVisitor, final List<MCAMethod> toFind) {
        super(new EmptyVisitor());
        if (classVisitor == null) {
            throw new IllegalArgumentException("Argument 'classVisitor' canot be NULL");
        }
        if (toFind == null) {
            throw new IllegalArgumentException("Argument 'toFind' canot be NULL");
        }
        this.classVisitor = classVisitor;
        this.toFind = toFind;
    }

    @Override
    public final void visitMethodInsn(final int opcode, final String owner, final String name,
            final String descr) {

        final MCAMethod m = new MCAMethod(owner, name, descr);
        final int idx = toFind.indexOf(m);
        if (idx > -1) {
            found = toFind.get(idx);
        }

    }

    @Override
    public final void visitCode() {
        found = null;
    }

    @Override
    public final void visitLineNumber(final int line, final Label start) {
        this.line = line;
    }

    @Override
    public final void visitEnd() {
        if (found != null) {
            classVisitor.addCall(found, line);
        }
    }

    /**
     * Returns the information if a method was found.
     * 
     * @return If the method was found TRUE, else FALSE.
     */
    public final boolean isFound() {
        return found != null;
    }

    /**
     * Returns the parent class visitor.
     * 
     * @return Parent.
     */
    public final MCAClassVisitor getClassVisitor() {
        return classVisitor;
    }

    /**
     * Returns the list of methods to find.
     * 
     * @return Method list.
     */
    public final List<MCAMethod> getToFind() {
        return toFind;
    }

}
