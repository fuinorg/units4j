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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

// CHECKSTYLE:OFF Test code
public final class UtilsTest {

    @Test
    public final void testSaveLoad() throws IOException {

        final File file = File.createTempFile(this.getClass().getSimpleName() + "_testSaveLoad", ".xml");
        try {

            final Dependencies dependencies = new Dependencies();

            final DependsOn dependsOn1 = new DependsOn("a.b.c", true);
            dependencies.getAlwaysAllowed().add(dependsOn1);
            final DependsOn dependsOn2 = new DependsOn("a.b.d", false);
            dependencies.getAlwaysAllowed().add(dependsOn2);

            final NotDependsOn notDependsOn1 = new NotDependsOn("a.b.e", true, "An important comment");
            dependencies.getAlwaysForbidden().add(notDependsOn1);
            final NotDependsOn notDependsOn2 = new NotDependsOn("a.b.f", false, "Whatever comment");
            dependencies.getAlwaysForbidden().add(notDependsOn2);

            final Package<DependsOn> allowedPackage = new Package<DependsOn>("org.fuin.utils4j", "Allowed comment");
            dependencies.getAllowed().add(allowedPackage);
            final Package<NotDependsOn> forbiddenPackage = new Package<NotDependsOn>("jakarta.security", "Forbidden comment");
            dependencies.getForbidden().add(forbiddenPackage);

            // Save to disk
            Utils.save(file, dependencies);

            // Load it again
            final Dependencies loaded = Utils.load(file);

            assertThat(loaded).isNotNull();
            assertThat(loaded.getAlwaysAllowed()).hasSize(2);
            assertThat(loaded.getAlwaysAllowed()).contains(dependsOn1, dependsOn2);
            assertThat(loaded.getAlwaysForbidden()).hasSize(2);
            assertThat(loaded.getAlwaysForbidden()).contains(notDependsOn1, notDependsOn2);
            assertThat(loaded.getAllowed()).hasSize(1);
            assertThat(loaded.getAllowed()).contains(allowedPackage);
            assertThat(loaded.getForbidden()).hasSize(1);
            assertThat(loaded.getForbidden()).contains(forbiddenPackage);

        } finally {
            file.deleteOnExit();
        }

    }

    @Test
    public final void testLoadInputStream() throws IOException {

        final File file = new File("src/test/resources/units4j.xml");
        final FileInputStream inputStream = new FileInputStream(file);
        try {
            final Dependencies loaded = Utils.load(inputStream);
            // We test just the load success here (no content)
            assertThat(loaded).isNotNull();
        } finally {
            inputStream.close();
        }

    }

    @Test
    public final void testLoadClassString() {
        final Dependencies loaded = Utils.load(this.getClass(), "/units4j.xml");
        // We test just the load success here (no content)
        assertThat(loaded).isNotNull();
    }

    @Test
    public final void testFindByNameDependsOnIncludeSubPackage() {

        final List<DependsOn> allowedList = new ArrayList<DependsOn>();
        final boolean includeSubPackage = true;
        final DependsOn dependsOn = new DependsOn("org.fuin", includeSubPackage);
        allowedList.add(dependsOn);

        assertThat(Utils.findAllowedByName(allowedList, "org")).isNull();
        assertThat(Utils.findAllowedByName(allowedList, "org.fuin")).isEqualTo(dependsOn);
        assertThat(Utils.findAllowedByName(allowedList, "org.fuin.utils4j")).isEqualTo(dependsOn);
        assertThat(Utils.findAllowedByName(allowedList, "org.fuin.utils4j.xyz")).isEqualTo(dependsOn);

    }

    @Test
    public final void testFindByNameDependsOnNotIncludeSubPackage() {

        final List<DependsOn> allowedList = new ArrayList<DependsOn>();
        final boolean includeSubPackage = false;
        final DependsOn dependsOn = new DependsOn("org.fuin", includeSubPackage);
        allowedList.add(dependsOn);

        assertThat(Utils.findAllowedByName(allowedList, "org")).isNull();
        assertThat(Utils.findAllowedByName(allowedList, "org.fuin")).isEqualTo(dependsOn);
        assertThat(Utils.findAllowedByName(allowedList, "org.fuin.utils4j")).isNull();
        assertThat(Utils.findAllowedByName(allowedList, "org.fuin.utils4j.xyz")).isNull();

    }

    @Test
    public final void testFindByNameNotDependsOnIncludeSubPackage() {

        final List<NotDependsOn> notAllowedList = new ArrayList<NotDependsOn>();
        final boolean includeSubPackage = true;
        final NotDependsOn notDependsOn = new NotDependsOn("org.dr", includeSubPackage, "A comment");
        notAllowedList.add(notDependsOn);

        assertThat(Utils.findForbiddenByName(notAllowedList, "org")).isNull();
        assertThat(Utils.findForbiddenByName(notAllowedList, "org.dr")).isEqualTo(notDependsOn);
        assertThat(Utils.findForbiddenByName(notAllowedList, "org.dr.evil")).isEqualTo(notDependsOn);
        assertThat(Utils.findForbiddenByName(notAllowedList, "org.dr.evil.xyz")).isEqualTo(notDependsOn);

    }

    @Test
    public final void testFindByNameNotDependsOnNotIncludeSubPackage() {

        final List<NotDependsOn> notAllowedList = new ArrayList<NotDependsOn>();
        final boolean includeSubPackage = false;
        final NotDependsOn notDependsOn = new NotDependsOn("org.dr", includeSubPackage, "A comment");
        notAllowedList.add(notDependsOn);

        assertThat(Utils.findForbiddenByName(notAllowedList, "org")).isNull();
        assertThat(Utils.findForbiddenByName(notAllowedList, "org.dr")).isEqualTo(notDependsOn);
        assertThat(Utils.findForbiddenByName(notAllowedList, "org.dr.evil")).isNull();
        assertThat(Utils.findForbiddenByName(notAllowedList, "org.dr.evil.xyz")).isNull();

    }

}
// CHECKSTYLE:ON
