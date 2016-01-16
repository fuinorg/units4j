/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.units4j;

import java.util.List;

import javax.persistence.Entity;

import org.assertj.core.api.AbstractAssert;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;

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
     * <li>[OK] The class must have a public or protected, no-argument constructor. The class may have other
     * constructors.</li>
     * <li>[TODO Implement!] The class must not be declared final. No methods or persistent instance variables
     * must be declared final.</li>
     * <li>[TODO Implement!] If an entity instance is passed by value as a detached object, such as through a
     * session bean’s remote business interface, the class must implement the Serializable interface.</li>
     * <li>[TODO Implement!] Entities may extend both entity and non-entity classes, and non-entity classes
     * may extend entity classes.</li>
     * <li>[TODO Implement!] Persistent instance variables must be declared private, protected, or
     * package-private and can be accessed directly only by the entity class’s methods. Clients must access
     * the entity’s state through accessor or business methods.</li>
     * </ul>
     * 
     * @return Self.
     */
    public JandexAssert hasOnlyValidJpaEntities() {
        // Precondition
        isNotNull();

        final DotName dotName = DotName.createSimple(Entity.class.getName());
        final List<AnnotationInstance> annotations = actual.getAnnotations(dotName);
        for (final AnnotationInstance ai : annotations) {
            final AnnotationTarget target = ai.target();
            final ClassInfo info = target.asClass();
            final AssertionRules<ClassInfo> rules = new AssertionRules<ClassInfo>(
                    new RulePublicOrProtectedNoArgConstructor());
            final AssertionResult result = rules.verify(info);
            if (!result.isValid()) {
                failWithMessage(result.getErrorMessage());
            }
        }
        return this;
    }

}
