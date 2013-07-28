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

import java.io.File;

import org.fuin.units4j.dependency.Dependencies;
import org.fuin.units4j.dependency.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TESTCODE:BEGIN
public final class AssertDependenciesTest {

    private File file;

    private File classesDir;

    @Before
    public final void setUp() {
        file = new File("src/test/resources/units4j.xml");
        classesDir = new File("target/classes");
    }

    @After
    public final void tearDown() {
        file = null;
        classesDir = null;
    }

    @Test
    public final void testAssertRulesDependenciesFile() {
        final Dependencies dependencies = Utils.load(file);
        AssertDependencies.assertRules(dependencies, classesDir);
    }

    @Test
    public final void testAssertRulesFileFile() {
        AssertDependencies.assertRules(file, classesDir);
    }

    @Test
    public final void testAssertRulesClassStringFile() {
        AssertDependencies.assertRules(this.getClass(), "/units4j.xml", classesDir);
    }

}
// TESTCODE:END

