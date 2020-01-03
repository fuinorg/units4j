// CHECKSTYLE:OFF
package org.fuin.units4j.analyzer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * An empty implementation of the ASM MethodVisitor.
 */
public class EmptyMethodVisitor extends MethodVisitor {

    private EmptyAnnotationVisitor an;

    public EmptyMethodVisitor() {
        super(Opcodes.ASM7);
        an = new EmptyAnnotationVisitor();
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return an;
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return an;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
        return an;
    }

}
// CHECKSTYLE:OFF
