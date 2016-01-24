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
     * <li>The class must have a public or protected, no-argument constructor. The class may have other
     * constructors.</li>
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
            final AssertionRules<ClassInfo> rules = new AssertionRules<ClassInfo>(
                    new RulePublicOrProtectedNoArgConstructor(), new RuleClassNotFinal(),
                    new RuleClassHasNoFinalMethods(), new RulePersistentInstanceFieldVisibility());
            final AssertionResult result = rules.verify(info);
            if (!result.isValid()) {
                failWithMessage(result.getErrorMessage());
            }
        }

        return this;

    }

    /**
     * Checks if all public, protected and package visible methods define nullability. This means they have
     * either {@link javax.validation.constraints.NotNull} or {@link org.fuin.objects4j.common.Nullable}
     * annotations for parameters and return values.
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
                final AssertionResult result = rule.verify(method);
                if (!result.isValid()) {
                    ok = false;
                    sb.append(result.getErrorMessage());
                }
            }
        }

        if (!ok) {
            failWithMessage("A parameter or the return value has neither a "
                    + "@NotNull nor a @Nullable annotation:\n" + sb.toString());
        }

        return this;
    }

}
