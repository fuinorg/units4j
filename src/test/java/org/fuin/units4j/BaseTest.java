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

import java.io.File;

import org.fuin.units4j.AssertCoverage.ClassFilter;
import org.fuin.units4j.analyzer.EmptyAnnotationVisitor;
import org.fuin.units4j.analyzer.EmptyClassVisitor;
import org.fuin.units4j.analyzer.EmptyFieldVisitor;
import org.fuin.units4j.analyzer.EmptyMethodVisitor;
import org.junit.Test;

//CHECKSTYLE:OFF Test code
public final class BaseTest {

    @Test
    public final void testCoverage() {
        AssertCoverage.assertEveryClassHasATest(new File("src/main/java"),
                new ClassFilter() {
                    @Override
                    public boolean isIncludeClass(final Class<?> clasz) {
                        if ((clasz == EmptyClassVisitor.class)
                                || (clasz == EmptyAnnotationVisitor.class)
                                || (clasz == EmptyFieldVisitor.class)
                                || (clasz == EmptyMethodVisitor.class)) {
                            return false;
                        }
                        return true;
                    }
                });
    }

}
// CHECKSTYLE:ON
