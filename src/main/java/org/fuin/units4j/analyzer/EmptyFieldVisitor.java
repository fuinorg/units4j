// CHECKSTYLE:OFF
package org.fuin.units4j.analyzer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * An empty implementation of the ASM FieldVisitor.
 */
public class EmptyFieldVisitor extends FieldVisitor {

    private EmptyAnnotationVisitor an;

    public EmptyFieldVisitor() {
        super(Opcodes.ASM9);
        an = new EmptyAnnotationVisitor();
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return an;
    }

}
// CHECKSTYLE:OFF
