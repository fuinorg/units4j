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

import javax.validation.constraints.NotNull;

/**
 * A collection of rules used in assertions.
 * 
 * @param <T>
 *            Type of object to validate.
 */
public final class AssertionRules<T> implements AssertionRule<T> {

    private final AssertionRule<T>[] rules;

    /**
     * Constructor with rules.
     * 
     * @param rules
     *            Sub rules.
     */
    @SafeVarargs
    public AssertionRules(@NotNull final AssertionRule<T>... rules) {
        super();
        if (rules.length == 0) {
            throw new IllegalArgumentException("Argument 'rules' cannot be an empty array");
        }
        for (final AssertionRule<?> rule : rules) {
            if (rule == null) {
                throw new IllegalArgumentException("Argument 'rules' cannot contain null elements");
            }
        }
        this.rules = (AssertionRule<T>[]) rules;
    }

    @Override
    public final AssertionResult verify(final T obj) {
        boolean ok = true;
        final StringBuffer sb = new StringBuffer();
        for (final AssertionRule<T> rule : rules) {
            final AssertionResult result = rule.verify(obj);
            if (!result.isValid()) {
                ok = false;
                sb.append(result.getErrorMessage());
                sb.append("\n");
            }
        }
        if (ok) {
            return AssertionResult.OK;
        }
        return new AssertionResult(sb.toString());
    }

}
