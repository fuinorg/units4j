package org.fuin.units4j.archunit;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.fuin.utils4j.TestOmitted;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.tngtech.archunit.lang.ConditionEvent.createMessage;

/**
 * Verifies that a class is annotated with at least one annotation out of a given set, identified by the annotation's <em>simple name</em>.
 * Matching by name (instead of by annotation class) deliberately avoids a compile-time dependency on the library that actually defines the
 * annotations - any annotation with a matching simple name satisfies the condition, regardless of its package.
 * <p>
 * The default set are the thread-safety annotations {@code Immutable}, {@code ThreadSafe}, {@code NotThreadSafe} and
 * {@code ThreadSafetyUndefined}.
 */
@TestOmitted("Tested implicitly")
public final class HaveThreadSafetyAnnotationCondition extends ArchCondition<JavaClass> {

    /** Simple names of the standard thread-safety annotations. */
    public static final Set<String> THREAD_SAFETY_ANNOTATION_NAMES =
            Set.of("Immutable", "ThreadSafe", "NotThreadSafe", "ThreadSafetyUndefined");

    private final Set<String> annotationSimpleNames;

    /**
     * Default constructor using the standard thread-safety annotation names: {@code Immutable}, {@code ThreadSafe}, {@code NotThreadSafe}
     * and {@code ThreadSafetyUndefined}.
     */
    public HaveThreadSafetyAnnotationCondition() {
        this(THREAD_SAFETY_ANNOTATION_NAMES);
    }

    /**
     * Constructor with a custom set of accepted annotation simple names.
     *
     * @param annotationSimpleNames Simple names of the annotations that satisfy the condition (e.g. "Immutable").
     */
    public HaveThreadSafetyAnnotationCondition(final String... annotationSimpleNames) {
        this(new LinkedHashSet<>(Arrays.asList(annotationSimpleNames)));
    }

    private HaveThreadSafetyAnnotationCondition(final Set<String> annotationSimpleNames) {
        super("be annotated with one of " + annotationSimpleNames);
        this.annotationSimpleNames = annotationSimpleNames;
    }

    @Override
    public void check(final JavaClass clazz, final ConditionEvents events) {
        boolean satisfied = false;
        for (final JavaAnnotation<?> annotation : clazz.getAnnotations()) {
            if (annotationSimpleNames.contains(annotation.getRawType().getSimpleName())) {
                satisfied = true;
                break;
            }
        }
        final String message = createMessage(clazz, (satisfied ? "is" : "is not") + " annotated with one of " + annotationSimpleNames);
        events.add(new SimpleConditionEvent(clazz, satisfied, message));
    }

    /**
     * Condition that a class is annotated with one of the standard thread-safety annotations ({@code Immutable}, {@code ThreadSafe},
     * {@code NotThreadSafe} or {@code ThreadSafetyUndefined}), matched by simple name.
     *
     * @return Condition.
     */
    public static ArchCondition<JavaClass> haveAThreadSafetyAnnotation() {
        return new HaveThreadSafetyAnnotationCondition();
    }

    /**
     * Condition that a class is annotated with at least one annotation having one of the given simple names.
     *
     * @param annotationSimpleNames Accepted annotation simple names.
     *
     * @return Condition.
     */
    public static ArchCondition<JavaClass> haveAnAnnotationWithSimpleName(final String... annotationSimpleNames) {
        return new HaveThreadSafetyAnnotationCondition(annotationSimpleNames);
    }

}
