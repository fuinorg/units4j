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
package org.fuin.units4j.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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

    @Test(expected = InvalidDependenciesDefinitionException.class)
    public void testValidate() throws InvalidDependenciesDefinitionException {

        final Dependencies testee = new Dependencies();
        testee.getAllowed().add(new Package<DependsOn>("a.b.c"));
        testee.getForbidden().add(new Package<NotDependsOn>("a.b.c"));
        testee.validate();

    }

}
// CHECKSTYLE:ON
