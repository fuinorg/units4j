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
 * Checks if a method defines nullability for all public, protected and package visible methods. This means it has
 * <code>javax.validation.constraints.NotNull</code> or <code>org.fuin.objects4j.common.Nullable</code> annotations for parameters and
 * return values.
 */
public final class RuleMethodHasNullabilityInfo implements AssertionRule<MethodInfo> {

    private final String notNullFqn;

    private final String nullableFqn;

    /**
     * Constructor that defaults to annotations "javax.validation.constraints.NotNull" and "org.fuin.objects4j.common.Nullable".
     */
    public RuleMethodHasNullabilityInfo() {
        this("javax.validation.constraints.NotNull", "org.fuin.objects4j.common.Nullable");
    }

    /**
     * Constructor with annotation class names.
     * 
     * @param notNullFqn
     *            Full qualified class name of the @NotNull annotation.
     * @param nullableFqn
     *            Full qualified class name of the @Nullable annotation.
     */
    public RuleMethodHasNullabilityInfo(final String notNullFqn, final String nullableFqn) {
        super();
        if (notNullFqn == null) {
            throw new IllegalArgumentException("Argument 'notNullFqn' cannot be null");
        }
        if (nullableFqn == null) {
            throw new IllegalArgumentException("Argument 'nullableFqn' cannot be null");
        }

        this.notNullFqn = notNullFqn.trim();
        this.nullableFqn = nullableFqn.trim();

        if (this.notNullFqn.length() == 0) {
            throw new IllegalArgumentException("Argument 'notNullFqn' cannot be empty");
        }
        if (this.nullableFqn.length() == 0) {
            throw new IllegalArgumentException("Argument 'nullableFqn' cannot be empty");
        }

    }

    @Override
    public final AssertionResult verify(final MethodInfo method) {

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
        final List<Type> params = method.parameters();
        for (int i = 0; i < params.size(); i++) {
            final Type param = params.get(i);
            if (param.kind() != Kind.PRIMITIVE) {
                final List<AnnotationInstance> annotations = map.get(i);
                if ((annotations == null) || !contains(annotations, notNullFqn, nullableFqn)) {
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
            if (!contains(list, notNullFqn, nullableFqn)) {
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

    private boolean contains(final List<AnnotationInstance> annotations, final String notNullFqn, final String nullableFqn) {
        return Utils.hasAnnotation(annotations, notNullFqn) || Utils.hasAnnotation(annotations, nullableFqn);
    }

}
