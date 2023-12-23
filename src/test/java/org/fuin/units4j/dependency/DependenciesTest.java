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
package org.fuin.units4j.dependency;

import org.fuin.utils4j.Utils4J;
import org.fuin.utils4j.jaxb.JaxbUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// CHECKSTYLE:OFF Test code
public final class DependenciesTest {

    @Test
    public void testCreation() {
        final Dependencies testee = new Dependencies();
        assertThat(testee.getAlwaysAllowed()).isEmpty();
        assertThat(testee.getAlwaysForbidden()).isEmpty();
        assertThat(testee.getAllowed()).isEmpty();
        assertThat(testee.getForbidden()).isEmpty();

    }

    @Test
    public void testValidate() throws InvalidDependenciesDefinitionException {

        assertThatThrownBy(() -> {
            final Dependencies testee = new Dependencies();
            testee.getAllowed().add(new Package<DependsOn>("a.b.c"));
            testee.getForbidden().add(new Package<NotDependsOn>("a.b.c"));
            testee.validate();
        }).isInstanceOf(InvalidDependenciesDefinitionException.class);        

    }

    @Test
    public final void testSerDeserialize() {

        // PREPARE
        final Dependencies testee = new Dependencies();
        final Package<DependsOn> abc = new Package<DependsOn>("a.b.c");
        testee.getAllowed().add(abc);
        final Package<NotDependsOn> def = new Package<NotDependsOn>("d.e.f");
        testee.getForbidden().add(def);

        // TEST
        final byte[] data = Utils4J.serialize(testee);
        final Dependencies copy = Utils4J.deserialize(data);

        // VERIFY
        assertThat(copy.getAlwaysAllowed()).isEmpty();
        assertThat(copy.getAlwaysForbidden()).isEmpty();
        assertThat(copy.getAllowed()).containsExactly(abc);
        assertThat(copy.getForbidden()).containsExactly(def);

    }

    @Test
    public final void testMarshalUnmarshalXml() {

        // PREPARE
        final Dependencies testee = new Dependencies();

        final DependsOn dependsOn1 = new DependsOn("a.b.c", true);
        testee.getAlwaysAllowed().add(dependsOn1);
        final DependsOn dependsOn2 = new DependsOn("a.b.d", false);
        testee.getAlwaysAllowed().add(dependsOn2);

        final NotDependsOn notDependsOn1 = new NotDependsOn("a.b.e", true, "An important comment");
        testee.getAlwaysForbidden().add(notDependsOn1);
        final NotDependsOn notDependsOn2 = new NotDependsOn("a.b.f", false, "Whatever comment");
        testee.getAlwaysForbidden().add(notDependsOn2);

        final Package<DependsOn> allowedPackage = new Package<DependsOn>("org.fuin.utils4j", "Allowed comment");
        testee.getAllowed().add(allowedPackage);
        final Package<NotDependsOn> forbiddenPackage = new Package<NotDependsOn>("javax.security", "Forbidden comment");
        testee.getForbidden().add(forbiddenPackage);

        // TEST
        final String xml = JaxbUtils.marshal(Utils.createJaxbContext(), testee, null);
        final Dependencies copy = JaxbUtils.unmarshal(Utils.createJaxbContext(), xml, null);

        // VERIFY
        assertThat(copy).isNotNull();
        assertThat(copy.getAlwaysAllowed()).hasSize(2);
        assertThat(copy.getAlwaysAllowed()).contains(dependsOn1, dependsOn2);
        assertThat(copy.getAlwaysForbidden()).hasSize(2);
        assertThat(copy.getAlwaysForbidden()).contains(notDependsOn1, notDependsOn2);
        assertThat(copy.getAllowed()).hasSize(1);
        assertThat(copy.getAllowed()).contains(allowedPackage);
        assertThat(copy.getForbidden()).hasSize(1);
        assertThat(copy.getForbidden()).contains(forbiddenPackage);

    }
        
}
// CHECKSTYLE:ON
