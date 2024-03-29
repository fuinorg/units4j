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

import jakarta.validation.constraints.NotNull;
import org.fuin.units4j.AssertionResult;
import org.fuin.units4j.AssertionRule;
import org.fuin.utils4j.Utils4J;
import org.jboss.jandex.*;
import org.jboss.jandex.Type.Kind;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Checks if all public, protected and package visible methods define nullability. This means every parameter and return value follows at
 * least one of the following conditions:
 * <ul>
 * <li>It has a <code>@Nullable</code> annotation</li>
 * <li>It has a <code>@NotNull</code> or <code>@NonNull</code> annotation</li>
 * <li>It is an {@link Optional} value.</li>
 * </ul>
 * The case or package of the annotations does not matter and is not checked.
 */
public final class RuleMethodHasNullabilityInfo implements AssertionRule<MethodInfo> {

    private final List<String> expectedAnnotations;

    private final DotName optionalDotName;

    /**
     * Constructor that defaults to annotations "NotNull", "NonNull" and "Nullable".
     */
    public RuleMethodHasNullabilityInfo() {
        this("NotEmpty", "NotNull", "NonNull", "Nullable");
    }

    /**
     * Constructor with annotation simple class names.
     * 
     * @param annotationNames
     *            Simple class names of annotations that satisfy the condition.
     */
    public RuleMethodHasNullabilityInfo(final String... annotationNames) {
        super();
        Utils4J.checkNotNull("annotationNames", annotationNames);

        expectedAnnotations = Arrays.asList(annotationNames);
        optionalDotName = DotName.createSimple(Optional.class.getName());

    }

    @Override
    public final AssertionResult verify(@NotNull final MethodInfo method) {
        Utils4J.checkNotNull("method", method);

        final StringBuilder sb = new StringBuilder();
        final boolean returnTypeOk = validReturnType(method, sb);
        final boolean parametersOk = validParameters(method, sb);
        if (returnTypeOk && parametersOk) {
            return AssertionResult.OK;
        }
        return new AssertionResult(sb.toString());

    }

    private boolean validParameters(final MethodInfo method, final StringBuilder sb) {

        boolean ok = true;

        final Map<Integer, List<AnnotationInstance>> map = Utils.createParameterAnnotationMap(method);
        final List<MethodParameterInfo> params = method.parameters();
        for (int i = 0; i < params.size(); i++) {
            final Type param = params.get(i).type();
            if (!typeIsPrimitive(param) && !typeIsOptional(param)) {
                final List<AnnotationInstance> annotations = map.get(i);
                if ((annotations == null) || !Utils.hasOneOfSimpleAnnotations(annotations, expectedAnnotations)) {
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
        if (!returnsVoid(method) && !returnsPrimitive(method) && !returnsOptional(method)) {
            final List<AnnotationInstance> list = Utils.createReturnTypeAnnotationList(method);
            if (!Utils.hasOneOfSimpleAnnotations(list, expectedAnnotations)) {
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

    private boolean typeIsPrimitive(final Type type) {
        return type.kind() == Kind.PRIMITIVE;
    }

    private boolean typeIsOptional(final Type type) {
        return type.name().equals(optionalDotName);
    }

    private boolean returnsPrimitive(final MethodInfo method) {
        return typeIsPrimitive(method.returnType());
    }

    private boolean returnsVoid(final MethodInfo method) {
        return method.returnType().kind() == Kind.VOID;
    }

    private boolean returnsOptional(final MethodInfo method) {
        return typeIsOptional(method.returnType());
    }

}
