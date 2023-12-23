// CHECKSTYLE:OFF
package org.fuin.units4j.analyzer;

import org.objectweb.asm.*;

/**
 * An empty implementation of the ASM ClassVisitor.
 */
public class EmptyClassVisitor extends ClassVisitor {

    private EmptyAnnotationVisitor an;

    private EmptyMethodVisitor me;

    private EmptyFieldVisitor fi;

    public EmptyClassVisitor() {
        super(Opcodes.ASM7);
        an = new EmptyAnnotationVisitor();
        me = new EmptyMethodVisitor();
        fi = new EmptyFieldVisitor();
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return an;
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        return fi;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
            final String[] exceptions) {
        return me;
    }

}
// CHECKSTYLE:OFF
