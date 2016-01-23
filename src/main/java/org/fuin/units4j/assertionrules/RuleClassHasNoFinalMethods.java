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

import java.lang.reflect.Modifier;
import java.util.List;

import org.fuin.units4j.AssertionResult;
import org.fuin.units4j.AssertionRule;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.MethodInfo;

/**
 * Checks if a class has no final methods.
 */
public final class RuleClassHasNoFinalMethods implements AssertionRule<ClassInfo> {

    @Override
    public final AssertionResult verify(final ClassInfo info) {

        boolean ok = true;
        final StringBuffer sb = new StringBuffer("Class " + info.name() + " has final methods:\n");

        final List<MethodInfo> methodInfos = info.methods();
        for (final MethodInfo methodInfo : methodInfos) {
            if (Modifier.isFinal(methodInfo.flags())) {
                ok = false;
                sb.append(methodInfo.toString());
                sb.append("\n");
            }            
        }
        
        if (ok) {
            return AssertionResult.OK;
        }
        return new AssertionResult(sb.toString());

    }

}
