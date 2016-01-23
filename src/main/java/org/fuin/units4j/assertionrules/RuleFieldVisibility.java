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
package org.fuin.units4j.assertionrules;

import static org.fuin.units4j.assertionrules.Visibility.PACKAGE;
import static org.fuin.units4j.assertionrules.Visibility.PRIVATE;
import static org.fuin.units4j.assertionrules.Visibility.PROTECTED;
import static org.fuin.units4j.assertionrules.Visibility.PUBLIC;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.fuin.units4j.AssertionResult;
import org.fuin.units4j.AssertionRule;
import org.jboss.jandex.FieldInfo;

/**
 * Checks if a field has a defined visibility.
 */
public final class RuleFieldVisibility implements AssertionRule<FieldInfo> {

    private final Set<Visibility> allowed;

    /**
     * Creates the rule with a given set of allowed modifiers.
     * 
     * @param allowed
     *            Allowed modifiers.
     */
    public RuleFieldVisibility(final Visibility... allowed) {
        super();
        this.allowed = new HashSet<>();
        for (final Visibility v : allowed) {
            this.allowed.add(v);
        }
    }

    /**
     * Creates the rule with a given set of allowed modifiers.
     * 
     * @param allowed
     *            Allowed modifiers.
     */
    public RuleFieldVisibility(final Set<Visibility> allowed) {
        super();
        this.allowed = allowed;
    }

    @Override
    public final AssertionResult verify(final FieldInfo info) {

        if (Modifier.isPublic(info.flags())) {
            if (allowed.contains(PUBLIC)) {
                return AssertionResult.OK;
            }
            return new AssertionResult("Public visibility is not allowed for: " + fqn(info));
        }
        if (Modifier.isProtected(info.flags())) {
            if (allowed.contains(PROTECTED)) {
                return AssertionResult.OK;
            }
            return new AssertionResult("Protected visibility is not allowed for: " + fqn(info));
        }
        if (Modifier.isPrivate(info.flags())) {
            if (allowed.contains(PRIVATE)) {
                return AssertionResult.OK;
            }
            return new AssertionResult("Private visibility is not allowed for: " + fqn(info));
        }
        if (!allowed.contains(PACKAGE)) {
            return new AssertionResult("Package-private visibility is not allowed for: " + fqn(info));
        }
        return AssertionResult.OK;

    }

    private String fqn(final FieldInfo info) {
        return info.declaringClass().name() + "." + info.name();
    }

}
