package org.fuin.units4j.archunit;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchRule;
import org.fuin.utils4j.TestOmitted;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.fuin.units4j.archunit.AllTopLevelClassesHaveATestCondition.haveACorrespondingClassEndingWith;
import static org.fuin.units4j.archunit.HaveThreadSafetyAnnotationCondition.haveAThreadSafetyAnnotation;

/**
 * Defines general ArchUnit conditions useful for other projects.
 */
@TestOmitted("Tested implicitly")
public class Units4JConditions {

    private Units4JConditions() {
    }

    /**
     * Verifies that all classes have a corresponding "*Test" class.
     */
    @PublicAPI(usage = ACCESS)
    public static final ArchRule ALL_CLASSES_SHOULD_HAVE_TESTS =
            classes()
                    .that()
                    .areTopLevelClasses()
                    .and().areNotInterfaces()
                    .and().areNotRecords()
                    .and().areNotEnums()
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .and().areNotAnnotatedWith(TestOmitted.class)
                    .should(haveACorrespondingClassEndingWith("Test"));

    /**
     * Verifies that all classes are annotated with one of the thread-safety annotations "Immutable", "ThreadSafe", "NotThreadSafe" or
     * "ThreadSafetyUndefined". The annotations are matched by their simple name, so any library defining annotations with these names is
     * accepted. Annotation types ({@code @interface}), enums, {@code package-info} and {@code @TestOmitted} classes are excluded.
     */
    @PublicAPI(usage = ACCESS)
    public static final ArchRule ALL_CLASSES_SHOULD_HAVE_A_THREAD_SAFETY_ANNOTATION =
            classes()
                    .that()
                    .areTopLevelClasses()
                    .and().areNotAnnotations()
                    .and().areNotEnums()
                    .and().doNotHaveSimpleName("package-info")
                    .and().areNotAnnotatedWith(TestOmitted.class)
                    .should(haveAThreadSafetyAnnotation());

}
