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

import jakarta.persistence.Transient;
import org.fuin.units4j.AssertionResult;
import org.fuin.units4j.AssertionRule;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.FieldInfo;

import java.lang.reflect.Modifier;
import java.util.List;

import static org.fuin.units4j.assertionrules.Visibility.*;

/**
 * Checks if all persistent fields are declared private, protected, or package-private.
 */
public final class RulePersistentInstanceFieldVisibility implements AssertionRule<ClassInfo> {

    private final RuleFieldVisibility fieldRule;

    /**
     * Default constructor.
     */
    public RulePersistentInstanceFieldVisibility() {
        super();
        this.fieldRule = new RuleFieldVisibility(PRIVATE, PROTECTED, PACKAGE);
    }

    @Override
    public final AssertionResult verify(final ClassInfo info) {

        final StringBuilder sb = new StringBuilder();
        boolean ok = true;

        final List<FieldInfo> fields = info.fields();
        for (final FieldInfo field : fields) {
            // Every non-static non-final entity field is persistent by default unless
            // explicitly specified otherwise (e.g. by using the @Transient annotation)
            if (!Modifier.isStatic(field.flags()) && !Modifier.isFinal(field.flags())
                    && !Utils.hasAnnotation(field.annotations(), Transient.class.getName())) {
                final AssertionResult fieldResult = fieldRule.verify(field);
                if (!fieldResult.isValid()) {
                    ok = false;
                    sb.append(fieldResult.getErrorMessage());
                    sb.append("\n");
                }

            }

        }
        if (ok) {
            return AssertionResult.OK;
        }
        return new AssertionResult(sb.toString());

    }

}
