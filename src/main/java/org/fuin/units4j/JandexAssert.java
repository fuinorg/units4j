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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.assertj.core.api.AbstractAssert;
import org.fuin.units4j.assertionrules.RuleClassHasNoFinalMethods;
import org.fuin.units4j.assertionrules.RuleClassNotFinal;
import org.fuin.units4j.assertionrules.RuleMethodHasNullabilityInfo;
import org.fuin.units4j.assertionrules.RulePersistentInstanceFieldVisibility;
import org.fuin.units4j.assertionrules.RulePublicOrProtectedNoArgConstructor;
import org.fuin.units4j.assertionrules.Utils;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.MethodInfo;

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
            final AssertionRules<ClassInfo> rules = new AssertionRules<ClassInfo>(new RulePublicOrProtectedNoArgConstructor(),
                    new RuleClassNotFinal(), new RuleClassHasNoFinalMethods(), new RulePersistentInstanceFieldVisibility());
            final AssertionResult result = rules.verify(info);
            if (!result.isValid()) {
                failWithMessage(result.getErrorMessage());
            }
        }

        return this;

    }

    /**
     * Checks if all public, protected and package visible methods define nullability. This means they have either
     * <code>javax.validation.constraints.NotNull</code> or <code>org.fuin.objects4j.common.Nullable</code> annotations for parameters and
     * return values.
     * 
     * @return Self.
     */
    public JandexAssert hasNullabilityInfoOnAllMethods() {
        // Precondition
        isNotNull();

        final RuleMethodHasNullabilityInfo rule = new RuleMethodHasNullabilityInfo();

        final StringBuilder sb = new StringBuilder();
        boolean ok = true;

        final Collection<ClassInfo> classes = actual.getKnownClasses();
        for (final ClassInfo clasz : classes) {
            final List<MethodInfo> methods = clasz.methods();
            for (final MethodInfo method : methods) {
                if (!ignored(method) && !Modifier.isPrivate(method.flags())) {
                    final List<MethodInfo> overrideMethods = Utils.findOverrideMethods(actual, method);
                    if (overrideMethods.size() == 0) {
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
            failWithMessage("A parameter or the return value has neither a " + "@NotNull nor a @Nullable annotation:\n" + sb.toString());
        }

        return this;
    }

    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
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
        if (method.name().equals("toString") && (method.parameters().size() == 0)) {
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
        if (method.declaringClass().name().toString().contains("$") && method.name().equals("get") && (method.parameters().size() == 0)) {
            return true;
        }
        if (method.declaringClass().name().toString().contains("$") && method.name().equals("<init>")
                && method.declaringClass().flags() == 32) {
            return true;
        }
        return false;
    }

    private boolean isSynthetic(final short flags) {
        return (flags & SYNTHETIC) > 0;
    }

    private boolean isBridge(final short flags) {
        return (flags & BRIDGE) > 0;
    }

}
