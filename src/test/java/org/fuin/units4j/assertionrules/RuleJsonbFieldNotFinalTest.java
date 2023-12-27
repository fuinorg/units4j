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

import jakarta.json.bind.annotation.JsonbProperty;
import org.fuin.units4j.AssertionResult;
import org.jboss.jandex.ClassInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.units4j.Units4JUtils.classInfo;

// CHECKSTYLE:OFF Test code
public class RuleJsonbFieldNotFinalTest {

    private static ClassInfo CLASS_INFO = classInfo(MyClass.class);

    @Test
    public final void testValid() throws IOException {

        // PREPARE
        final RuleJsonbFieldNotFinal testee = new RuleJsonbFieldNotFinal();

        // TEST
        final AssertionResult resultA = testee.verify(CLASS_INFO.field("a"));
        final AssertionResult resultB = testee.verify(CLASS_INFO.field("b"));
        final AssertionResult resultC = testee.verify(CLASS_INFO.field("c"));

        // VERIFY
        assertThat(resultA).isNotNull();
        assertThat(resultA.isValid()).isTrue();
        assertThat(resultA.getErrorMessage()).isEqualTo("");
        assertThat(resultB).isNotNull();
        assertThat(resultB.isValid()).isTrue();
        assertThat(resultB.getErrorMessage()).isEqualTo("");
        assertThat(resultC).isNotNull();
        assertThat(resultC.isValid()).isTrue();
        assertThat(resultC.getErrorMessage()).isEqualTo("");

    }

    @Test
    public final void testInvalid() throws IOException {

        // PREPARE
        final RuleJsonbFieldNotFinal testee = new RuleJsonbFieldNotFinal();

        // TEST
        final AssertionResult result = testee.verify(CLASS_INFO.field("d"));

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).startsWith("Modifier 'final' is not allowed for field with '@JsonbProperty': ");

    }

    @SuppressWarnings("unused")
    public static class MyClass {
        
        private final String a = "abc";
        
        private String b;

        @JsonbProperty("c")
        private String c;
        
        @JsonbProperty("d")
        private final String d = "def";
        
    }

}
// CHECKSTYLE:ON
