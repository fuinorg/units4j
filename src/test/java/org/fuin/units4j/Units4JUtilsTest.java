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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Set;

import jakarta.validation.ConstraintViolation;

import org.fuin.units4j.Units4JUtils.KV;
import org.fuin.utils4j.JaxbUtils;
import org.fuin.utils4j.Utils4J;
import org.junit.ComparisonFailure;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public final class Units4JUtilsTest {

    @Test
    public final void testSerializeDeserialize() {

        // PREPARE
        final MyTestClass original = new MyTestClass(1, "Test");

        // TEST
        final MyTestClass copy = Utils4J.deserialize(Utils4J.serialize(original));

        // VERIFY
        assertThat(copy).isEqualTo(original);

    }

    @Test
    public final void testMarshalUnmarshal() {

        // PREPARE
        final MyTestClass original = new MyTestClass(1, "Test");

        // TEST
        final String xml = JaxbUtils.marshal(original, MyTestClass.class);
        final MyTestClass copy = JaxbUtils.unmarshal(xml, MyTestClass.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);

    }

    @Test
    public final void testSetPrivateField() {

        // PREPARE
        final MyTestClass original = new MyTestClass(1, "Test");
        final Integer id = Integer.valueOf(2);
        final String name = "Changed";

        // TEST
        Units4JUtils.setPrivateField(original, "id", id);
        Units4JUtils.setPrivateField(original, "name", name);

        // VERIFY
        assertThat(original.getId()).isEqualTo(id);
        assertThat(original.getName()).isEqualTo(name);

    }

    @Test
    public final void testAssertCauseMessage() {

        // PREPARE
        final Exception second = new RuntimeException("second");
        final Exception first = new RuntimeException("first", second);

        // TEST & VERIFY
        Units4JUtils.assertCauseMessage(first, "second");
        try {
            Units4JUtils.assertCauseMessage(first, "xxx");
            fail();
        } catch (final ComparisonFailure f) {
            // OK
            assertThat(f.getMessage()).isEqualTo("expected:<\"[xxx]\"> but was:<\"[second]\">");
        }

    }

    @Test
    public final void testAssertCauseCauseMessage() {

        // PREPARE
        final Exception third = new RuntimeException("third");
        final Exception second = new RuntimeException("second", third);
        final Exception first = new RuntimeException("first", second);

        // TEST & VERIFY
        Units4JUtils.assertCauseCauseMessage(first, "third");
        try {
            Units4JUtils.assertCauseCauseMessage(first, "xxx");
            fail();
        } catch (final ComparisonFailure f) {
            // OK
            assertThat(f.getMessage()).isEqualTo("expected:<\"[xxx]\"> but was:<\"[third]\">");
        }

    }

    @Test
    public final void testValidate() {

        // PREPARE
        final MyTestClass obj = new MyTestClass(0, null);

        // TEST
        final Set<ConstraintViolation<Object>> result = Units4JUtils.validate(obj);

        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

    }

    @Test
    public final void testValidator() {

        assertThat(Units4JUtils.validator()).isNotNull();

    }

    @Test
    public final void testReplaceXmlAttr() {

        // PREPARE
        final MyTestClass original = new MyTestClass(1, "Test");
        final String xml = JaxbUtils.marshal(original, MyTestClass.class);
        final Integer id = Integer.valueOf(2);
        final String name = "Changed";

        // TEST
        final String changed = Units4JUtils.replaceXmlAttr(xml, new KV("id", "" + id), new KV("name", name));

        // VERIFY
        final MyTestClass copy = JaxbUtils.unmarshal(changed, MyTestClass.class);
        assertThat(copy.getId()).isEqualTo(id);
        assertThat(copy.getName()).isEqualTo(name);

    }

    @Test
    public final void testIsExpectedType() {

        assertThat(Units4JUtils.isExpectedType(null, null)).isTrue();
        assertThat(Units4JUtils.isExpectedType(null, Integer.valueOf(1))).isFalse();
        assertThat(Units4JUtils.isExpectedType(Integer.class, null)).isFalse();
        assertThat(Units4JUtils.isExpectedType(Integer.class, Integer.valueOf(1))).isTrue();

    }

    @Test
    public final void testIsExpectedException() {

        assertThat(Units4JUtils.isExpectedException(null, null, null)).isTrue();
        assertThat(Units4JUtils.isExpectedException(null, "", null)).isTrue();
        assertThat(Units4JUtils.isExpectedException(null, "Whatever", new IllegalArgumentException("Oops"))).isFalse();
        assertThat(Units4JUtils.isExpectedException(null, null, new IllegalArgumentException("Oops"))).isFalse();
        assertThat(Units4JUtils.isExpectedException(IllegalArgumentException.class, "Whatever", new IllegalArgumentException("Whatever")))
                .isTrue();
        assertThat(Units4JUtils.isExpectedException(IllegalArgumentException.class, "Whatever", new IllegalArgumentException("Oops")))
                .isFalse();
        assertThat(Units4JUtils.isExpectedException(IllegalArgumentException.class, "Whatever", new RuntimeException("W"))).isFalse();

    }

}
// CHECKSTYLE:ON
