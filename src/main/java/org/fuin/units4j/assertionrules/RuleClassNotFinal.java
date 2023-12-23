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
import org.jboss.jandex.ClassInfo;

import java.lang.reflect.Modifier;

/**
 * Checks if a class is not final.
 */
public final class RuleClassNotFinal implements AssertionRule<ClassInfo> {

    @Override
    public final AssertionResult verify(final ClassInfo info) {

        if (Modifier.isFinal(info.flags())) {
            return new AssertionResult("Class is final: " + info.name());
        }

        return AssertionResult.OK;

    }

}
