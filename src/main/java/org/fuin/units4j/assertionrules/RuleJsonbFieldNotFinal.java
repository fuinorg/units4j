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

import org.fuin.units4j.AssertionResult;
import org.fuin.units4j.AssertionRule;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Checks if a field that has a {@code jakarta.json.bind.annotation.JsonbProperty annotation} is not final. The deserialization using a
 * {@code org.eclipse.yasson.FieldAccessStrategy} will fail otherwise.
 */
public final class RuleJsonbFieldNotFinal implements AssertionRule<FieldInfo> {

    @Override
    public final AssertionResult verify(final FieldInfo info) {
        
        if (Modifier.isFinal(info.flags()) && hasJsonbPropertyAnnotation(info.annotations())) {
            return new AssertionResult("Modifier 'final' is not allowed for field with '@JsonbProperty': " + fqn(info));
        }

        return AssertionResult.OK;

    }

    private boolean hasJsonbPropertyAnnotation(final List<AnnotationInstance> annotations) {
        for (final AnnotationInstance annotation : annotations) {
            if (annotation.name().compareTo(DotName.createSimple("jakarta.json.bind.annotation.JsonbProperty")) == 0) {
                return true;
            }
        }
        return false;
    }

    private String fqn(final FieldInfo info) {
        return info.declaringClass().name() + "." + info.name();
    }

}
