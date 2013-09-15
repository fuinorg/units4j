/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.units4j;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.math.BigDecimal;

import org.fuin.units4j.analyzer.MCAMethod;
import org.junit.Test;

//TESTCODE:BEGIN
public final class AssertUsageTest {

    @Test
    public void testAssertMethodsNotUsed() {

        final File binDir = new File("target/test-classes");
        final MCAMethod divide = new MCAMethod(BigDecimal.class.getName(),
                BigDecimal.class.getName() + " divide(" + BigDecimal.class.getName() + ")");

        try {
            AssertUsage.assertMethodsNotUsed(binDir, null, divide);
        } catch (final AssertionError err) {
            assertThat(err.getMessage()).isEqualTo(
                    "Illegal method call(s) found:\n"
                            + "Source='FindMethodCallExampleClasz.java', Line=17, "
                            + "Class='org.fuin.units4j.analyzer.FindMethodCallExampleClasz, "
                            + "Method='void <init>()' ==CALLS==> "
                            + "Class='java.math.BigDecimal, "
                            + "Method='java.math.BigDecimal divide(java.math.BigDecimal)'");
        }

        assertThat(true).isTrue();

    }

}
// TESTCODE:END
