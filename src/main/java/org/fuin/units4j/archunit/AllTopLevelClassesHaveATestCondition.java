package org.fuin.units4j.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Collection;
import java.util.Set;

import static com.tngtech.archunit.lang.ConditionEvent.createMessage;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

/**
 * Defines a condition that all top level classes should have a test class.
 */
public final class AllTopLevelClassesHaveATestCondition extends ArchCondition<JavaClass> {

    private final String suffix;

    private Set<String> testedClassNames;

    /**
     * Default constructor that assumes the test classes names always end on "Test".
     */
    public AllTopLevelClassesHaveATestCondition() {
        this("Test");
    }

    /**
     * Defines the suffix for test classes.
     *
     * @param suffix Suffix for test classes like "Test".
     */
    public AllTopLevelClassesHaveATestCondition(final String suffix) {
        super("have a corresponding test class with suffix '" + suffix + "'");
        this.suffix = suffix;
        this.testedClassNames = emptySet();
    }

    @Override
    public void init(Collection<JavaClass> allClasses) {
        testedClassNames = allClasses.stream()
                .map(JavaClass::getName)
                .filter(className -> className.endsWith(suffix))
                .map(className -> className.substring(0, className.length() - suffix.length()))
                .collect(toSet());
    }

    @Override
    public void check(JavaClass clazz, ConditionEvents events) {
        if (!clazz.getName().endsWith(suffix)) {
            boolean satisfied = testedClassNames.contains(clazz.getName());
            String message = createMessage(clazz, "has " + (satisfied ? "a" : "no") + " corresponding test class");
            events.add(new SimpleConditionEvent(clazz, satisfied, message));
        }
    }

    /**
     * Defines a condition that all classes should have a corresponding "*Test" class.
     *
     * @return Condition.
     */
    public static ArchCondition<JavaClass> haveACorrespondingClassEndingWithTest() {
        return new AllTopLevelClassesHaveATestCondition();
    }

    /**
     * Defines a condition that all classes should have a corresponding "*SUFFIX" class.
     *
     * @return Condition.
     */
    public static ArchCondition<JavaClass> haveACorrespondingClassEndingWith(String suffix) {
        return new AllTopLevelClassesHaveATestCondition(suffix);
    }

}
