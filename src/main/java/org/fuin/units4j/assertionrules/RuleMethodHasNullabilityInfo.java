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

import java.util.List;
import java.util.Map;

import org.fuin.units4j.AssertionResult;
import org.fuin.units4j.AssertionRule;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;
import org.jboss.jandex.Type.Kind;

/**
 * Checks if a method defines nullability for all public, protected and package visible methods. This means it
 * has <code>javax.validation.constraints.NotNull</code> or <code>org.fuin.objects4j.common.Nullable</code>
 * annotations for parameters and return values.
 */
public final class RuleMethodHasNullabilityInfo implements AssertionRule<MethodInfo> {

    private static final String NOT_NULL = "javax.validation.constraints.NotNull";

    private static final String NULLABLE = "org.fuin.objects4j.common.Nullable";

    @Override
    public final AssertionResult verify(final MethodInfo method) {

        final StringBuilder sb = new StringBuilder();
        final boolean ok = validReturnType(method, sb) & validParameters(method, sb);
        if (ok) {
            return AssertionResult.OK;
        }
        return new AssertionResult(sb.toString());

    }

    private boolean validParameters(final MethodInfo method, final StringBuilder sb) {

        boolean ok = true;

        final Map<Integer, List<AnnotationInstance>> map = Utils.createParameterAnnotationMap(method);
        final List<Type> params = method.parameters();
        for (int i = 0; i < params.size(); i++) {
            final Type param = params.get(i);
            if (param.kind() != Kind.PRIMITIVE) {
                final List<AnnotationInstance> annotations = map.get(i);
                if ((annotations == null) || !hasNotNullOrNullable(annotations)) {
                    ok = false;
                    sb.append(method.declaringClass());
                    sb.append("\t");
                    sb.append(method);
                    sb.append("\t");
                    sb.append("Parameter #" + i + " (" + params.get(i).name() + ")\n");
                }
            }
        }

        return ok;
    }

    private boolean validReturnType(final MethodInfo method, final StringBuilder sb) {
        if ((method.returnType().kind() != Kind.VOID) && method.returnType().kind() != Kind.PRIMITIVE) {
            final List<AnnotationInstance> list = Utils.createMethodAnnotationList(method);
            if (!hasNotNullOrNullable(list)) {
                sb.append(method.declaringClass());
                sb.append("\t");
                sb.append(method);
                sb.append("\t");
                sb.append("Return type (" + method.returnType() + ")\n");
                return false;
            }
        }
        return true;
    }

    private boolean hasNotNullOrNullable(final List<AnnotationInstance> annotations) {
        return Utils.hasAnnotation(annotations, NOT_NULL) || Utils.hasAnnotation(annotations, NULLABLE);
    }

}
