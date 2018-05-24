// CHECKSTYLE:OFF
package org.fuin.units4j.analyzer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

/**
 * An empty implementation of the ASM AnnotationVisitor.
 */
public class EmptyAnnotationVisitor extends AnnotationVisitor {

    public EmptyAnnotationVisitor() {
        super(Opcodes.ASM5);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String desc) {
        return this;
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        return this;
    }

}
// CHECKSTYLE:OFF
