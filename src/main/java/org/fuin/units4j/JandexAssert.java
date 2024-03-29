/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.units4j;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import org.assertj.core.api.AbstractAssert;
import org.fuin.units4j.assertionrules.Utils;
import org.fuin.units4j.assertionrules.*;
import org.jboss.jandex.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Assertion class for JBoss Jandex.
 */
public final class JandexAssert extends AbstractAssert<JandexAssert, Index> {

    private static final int SYNTHETIC = 0x1000;
    private static final int BRIDGE = 0x0040;

    /**
     * Constructor to build assertion class with the object we want to make assertions on.
     * 
     * @param actual
     *            Index to use.
     */
    public JandexAssert(final Index actual) {
        super(actual, JandexAssert.class);
    }

    /**
     * Fluent entry point to assertion class, use it with static import.
     * 
     * @param actual
     *            Actual index to use.
     * 
     * @return New assertion instance.
     */
    public static JandexAssert assertThat(final Index actual) {
        return new JandexAssert(actual);
    }

    /**
     * Verifies that all class that are annotated with {@link Entity} observe the rules for JPA entities.
     * 
     * <ul>
     * <li>The class must have a public or protected, no-argument constructor. The class may have other constructors.</li>
     * <li>The class must not be declared final.</li>
     * <li>No methods or persistent instance variables must be declared final.</li>
     * <li>Persistent instance variables must be declared private, protected, or package-private.</li>
     * </ul>
     * 
     * @return Self.
     */
    public JandexAssert hasOnlyValidJpaEntities() {
        // Precondition
        isNotNull();

        final List<AnnotationInstance> annotations = new ArrayList<>();
        annotations.addAll(actual.getAnnotations(DotName.createSimple(Entity.class.getName())));
        annotations.addAll(actual.getAnnotations(DotName.createSimple(MappedSuperclass.class.getName())));
        for (final AnnotationInstance ai : annotations) {
            final AnnotationTarget target = ai.target();
            final ClassInfo info = target.asClass();
            final AssertionRules<ClassInfo> rules = new AssertionRules<>(new RulePublicOrProtectedNoArgConstructor(),
                    new RuleClassNotFinal(), new RuleClassHasNoFinalMethods(), new RulePersistentInstanceFieldVisibility());
            final AssertionResult result = rules.verify(info);
            if (!result.isValid()) {
                failWithMessage(result.getErrorMessage());
            }
        }

        return this;

    }

    /**
     * Checks if all public, protected and package visible methods define nullability. This means every parameter and return value follows
     * at least one of the following conditions:
     * <ul>
     * <li>It has a <code>@Nullable</code> annotation</li>
     * <li>It has a <code>@NotNull</code> or <code>@NonNull</code> annotation</li>
     * <li>It is an {@link Optional} value.</li>
     * </ul>
     * The case or package of the annotations does not matter and is not checked.
     * 
     * @return Self.
     */
    public JandexAssert hasNullabilityInfoOnAllMethods() {
        return hasNullabilityInfoOnAllMethods(new RuleMethodHasNullabilityInfo());
    }

    /**
     * Checks if all public, protected and package visible methods define nullability.
     * 
     * @param rule
     *            Nullability rule to use.
     * 
     * @return Self.
     */
    public JandexAssert hasNullabilityInfoOnAllMethods(final RuleMethodHasNullabilityInfo rule) {
        // Precondition
        isNotNull();

        final StringBuilder sb = new StringBuilder();
        boolean ok = true;

        final Collection<ClassInfo> classes = actual.getKnownClasses();
        for (final ClassInfo clasz : classes) {
            final List<MethodInfo> methods = clasz.methods();
            for (final MethodInfo method : methods) {
                if (!ignored(method) && !Modifier.isPrivate(method.flags())) {
                    final List<MethodInfo> overrideMethods = Utils.findOverrideMethods(actual, method);
                    if (overrideMethods.isEmpty()) {
                        // Only check methods that DON'T override an interface or super method
                        final AssertionResult result = rule.verify(method);
                        if (!result.isValid()) {
                            ok = false;
                            sb.append(result.getErrorMessage());
                        }
                    }
                }
            }
        }

        if (!ok) {
            failWithMessage(
                    "A parameter or the return value has neither a " + "@NotNull/@NonNull nor a @Nullable annotation:\n" + sb.toString());
        }

        return this;
    }

    /**
     * Checks if no field that has a {@code jakarta.json.bind.annotation.JsonbProperty annotation} is final. The deserialization using a
     * {@code org.eclipse.yasson.FieldAccessStrategy} will fail otherwise.
     * 
     * @return Self.
     */
    public JandexAssert hasNoFinalFieldsWithJsonbPropertyAnnotation() {
        // Precondition
        isNotNull();

        final RuleJsonbFieldNotFinal rule = new RuleJsonbFieldNotFinal();

        final StringBuilder sb = new StringBuilder();
        boolean ok = true;

        final Collection<ClassInfo> classes = actual.getKnownClasses();
        for (final ClassInfo clasz : classes) {
            final List<FieldInfo> fields = clasz.fields();
            for (final FieldInfo field : fields) {
                final AssertionResult result = rule.verify(field);
                if (!result.isValid()) {
                    ok = false;
                    sb.append(result.getErrorMessage());
                }
            }
        }

        if (!ok) {
            failWithMessage(
                    "At least one field has a 'final' modifier and is annotated with '@JsonbProperty' at the same time:\n" + sb.toString());
        }

        return this;
    }

    private boolean ignored(final MethodInfo method) {
        if (isSynthetic(method.flags()) || isBridge(method.flags())) {
            return true;
        }
        if (method.name().startsWith("access$")) {
            return true;
        }
        if (method.name().equals("equals") && (method.parameters().size() == 1)) {
            return true;
        }
        if (method.name().equals("compareTo") && (method.parameters().size() == 1)) {
            return true;
        }
        if (method.name().equals("toString") && (method.parameters().isEmpty())) {
            return true;
        }
        if (method.declaringClass().superName().toString().equals(Enum.class.getName())) {
            if (method.name().equals("values")) {
                return true;
            }
            if (method.name().equals("valueOf") && (method.parameters().size() == 1)) {
                return true;
            }
        }
        if (method.declaringClass().name().toString().contains("$") && method.name().equals("get") && (method.parameters().isEmpty())) {
            return true;
        }
        return (method.declaringClass().name().toString().contains("$") && method.name().equals("<init>")
                && method.declaringClass().flags() == 32);
    }

    private boolean isSynthetic(final short flags) {
        return (flags & SYNTHETIC) > 0;
    }

    private boolean isBridge(final short flags) {
        return (flags & BRIDGE) > 0;
    }

}
