// CHECKSTYLE:OFF
/***
 * ASM examples: examples showing how ASM can be used
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.fuin.units4j.dependency;

import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DependencyVisitor
 * 
 * @author Eugene Kuleshov
 * @author Michael Schnell
 */
public class DependencyVisitor extends ClassVisitor {

    private MVisitor mv;

    private AVisitor av;

    private SVisitor sv;

    private FVisitor fv;

    private Set<String> packages = new HashSet<>();

    private Map<String, Map<String, Integer>> groups = new HashMap<>();

    private Map<String, Integer> current;

    public DependencyVisitor() {
        super(Opcodes.ASM7);
        mv = new MVisitor();
        av = new AVisitor();
        sv = new SVisitor();
        fv = new FVisitor();
    }

    public Map<String, Map<String, Integer>> getGlobals() {
        return groups;
    }

    public Set<String> getPackages() {
        return packages;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName,
            final String[] interfaces) {
        String p = getGroupKey(name);
        current = groups.get(p);
        if (current == null) {
            current = new HashMap<>();
            groups.put(p, current);
        }

        if (signature == null) {
            addInternalName(superName);
            addInternalNames(interfaces);
        } else {
            addSignature(signature);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        addDesc(desc);
        return av;
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        if (signature == null) {
            addDesc(desc);
        } else {
            addTypeSignature(signature);
        }
        if (value instanceof Type) {
            addType((Type) value);
        }
        return fv;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
            final String[] exceptions) {
        if (signature == null) {
            addMethodDesc(desc);
        } else {
            addSignature(signature);
        }
        addInternalNames(exceptions);
        return mv;
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        // addName( outerName);
        // addName( innerName);
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
        // addName(owner);
        // addMethodDesc(desc);
    }

    private String getGroupKey(String name) {
        int n = name.lastIndexOf('/');
        if (n > -1) {
            name = name.substring(0, n);
        }
        packages.add(name);
        return name;
    }

    private void addName(final String name) {
        if (name == null) {
            return;
        }
        String p = getGroupKey(name);
        if (current.containsKey(p)) {
            current.put(p, current.get(p) + 1);
        } else {
            current.put(p, 1);
        }
    }

    private void addInternalName(final String name) {
        if (name != null) {
            addType(Type.getObjectType(name));
        }
    }

    private void addInternalNames(final String[] names) {
        for (int i = 0; names != null && i < names.length; i++) {
            addInternalName(names[i]);
        }
    }

    private void addDesc(final String desc) {
        addType(Type.getType(desc));
    }

    private void addMethodDesc(final String desc) {
        addType(Type.getReturnType(desc));
        Type[] types = Type.getArgumentTypes(desc);
        for (int i = 0; i < types.length; i++) {
            addType(types[i]);
        }
    }

    private void addType(final Type t) {
        switch (t.getSort()) {
        case Type.ARRAY:
            addType(t.getElementType());
            break;
        case Type.OBJECT:
            addName(t.getInternalName());
            break;
        default:
            // do nothing
            break;
        }
    }

    private void addSignature(final String signature) {
        if (signature != null) {
            new SignatureReader(signature).accept(sv);
        }
    }

    private void addTypeSignature(final String signature) {
        if (signature != null) {
            new SignatureReader(signature).acceptType(sv);
        }
    }

    // --- Helper classes ---

    private class MVisitor extends MethodVisitor {

        public MVisitor() {
            super(Opcodes.ASM7);
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
            addDesc(desc);
            return av;
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
            addType(Type.getObjectType(type));
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            addInternalName(owner);
            addDesc(desc);
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            addInternalName(owner);
            addMethodDesc(desc);
        }

        @Override
        public void visitLdcInsn(final Object cst) {
            if (cst instanceof Type) {
                addType((Type) cst);
            }
        }

        @Override
        public void visitMultiANewArrayInsn(final String desc, final int dims) {
            addDesc(desc);
        }

        @Override
        public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end,
                final int index) {
            addTypeSignature(signature);
        }

        @Override
        public AnnotationVisitor visitAnnotationDefault() {
            return av;
        }

        @Override
        public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
            addInternalName(type);
        }

    }

    private class AVisitor extends AnnotationVisitor {

        public AVisitor() {
            super(Opcodes.ASM7);
        }

        @Override
        public void visit(final String name, final Object value) {
            if (value instanceof Type) {
                addType((Type) value);
            }
        }

        @Override
        public void visitEnum(final String name, final String desc, final String value) {
            addDesc(desc);
        }

        @Override
        public AnnotationVisitor visitAnnotation(final String name, final String desc) {
            addDesc(desc);
            return this;
        }

        @Override
        public AnnotationVisitor visitArray(final String name) {
            return this;
        }

    }

    public class SVisitor extends SignatureVisitor {

        private String signatureClassName;

        public SVisitor() {
            super(Opcodes.ASM7);
        }

        @Override
        public SignatureVisitor visitClassBound() {
            return this;
        }

        @Override
        public SignatureVisitor visitInterfaceBound() {
            return this;
        }

        @Override
        public SignatureVisitor visitSuperclass() {
            return this;
        }

        @Override
        public SignatureVisitor visitInterface() {
            return this;
        }

        @Override
        public SignatureVisitor visitParameterType() {
            return this;
        }

        @Override
        public SignatureVisitor visitReturnType() {
            return this;
        }

        @Override
        public SignatureVisitor visitExceptionType() {
            return this;
        }

        @Override
        public SignatureVisitor visitArrayType() {
            return this;
        }

        @Override
        public void visitClassType(final String name) {
            signatureClassName = name;
            addInternalName(name);
        }

        @Override
        public void visitInnerClassType(final String name) {
            signatureClassName = signatureClassName + "$" + name;
            addInternalName(signatureClassName);
        }

        @Override
        public SignatureVisitor visitTypeArgument(final char wildcard) {
            return this;
        }

    }

    private class FVisitor extends FieldVisitor {

        public FVisitor() {
            super(Opcodes.ASM7);
        }

        @Override
        public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
            addDesc(desc);
            return av;
        }

    }

}
// CHECKSTYLE:ON
