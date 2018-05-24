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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

// CHECKSTYLE:OFF Test code
public final class DependencyAnalyzerTest {

    @Test
    @SuppressWarnings("unchecked")
    public final void testDependencyCreationAnalyzerFile() throws InvalidDependenciesDefinitionException {

        final DependsOn dependsOn1 = new DependsOn("a.b.c");
        final DependsOn dependsOn2 = new DependsOn("a.b.d");
        final NotDependsOn notDependsOn1 = new NotDependsOn("a.b.e");
        final NotDependsOn notDependsOn2 = new NotDependsOn("a.b.f");
        final Package<DependsOn> allowed = new Package<DependsOn>("my.package.x");
        final DependsOn allowed1 = new DependsOn("org.fuin.utils4j");
        final Package<NotDependsOn> forbidden = new Package<NotDependsOn>("my.package.y");
        final NotDependsOn forbidden1 = new NotDependsOn("javax.security");

        final DependencyAnalyzer testee = new DependencyAnalyzer(this.getClass(), "/example-dependencies.xml");

        assertThat(testee.getDependencies()).isNotNull();
        assertThat(testee.getDependencyErrors()).isEmpty();

        final Dependencies dep = testee.getDependencies();
        assertThat(dep.getAlwaysAllowed()).hasSize(2);
        assertThat(dep.getAlwaysAllowed()).contains(dependsOn1, dependsOn2);
        assertThat(dep.getAlwaysForbidden()).hasSize(2);
        assertThat(dep.getAlwaysForbidden()).contains(notDependsOn1, notDependsOn2);
        assertThat(dep.getAllowed()).hasSize(1);
        assertThat(dep.getAllowed()).contains(allowed);
        assertThat(dep.getForbidden()).hasSize(1);
        assertThat(dep.getForbidden()).contains(forbidden);

        final List<DependsOn> dependsOnList = dep.getAllowed().get(0).getDependencies();
        assertThat(dependsOnList).hasSize(1);
        assertThat(dependsOnList).contains(allowed1);
        final List<NotDependsOn> notDependsOnList = dep.getForbidden().get(0).getDependencies();
        assertThat(notDependsOnList).hasSize(1);
        assertThat(notDependsOnList).contains(forbidden1);

    }

    @Test
    public final void testDependencyCreationAnalyzerDependencies() throws InvalidDependenciesDefinitionException {

        final Dependencies dependencies = new Dependencies();
        final DependencyAnalyzer testee = new DependencyAnalyzer(dependencies);

        assertThat(testee.getDependencies()).isSameAs(dependencies);

    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testAnalyzeForbidden() throws InvalidDependenciesDefinitionException, IOException {

        final DependencyAnalyzer testee = new DependencyAnalyzer(this.getClass(), "/dummy-forbidden.xml");

        final List<Package<NotDependsOn>> forbidden = testee.getDependencies().getForbidden();
        assertThat(forbidden).hasSize(1);
        assertThat(forbidden).contains(new Package<NotDependsOn>("dummy.test.bad"));
        final Package<NotDependsOn> forbiddenPkg = forbidden.get(0);
        assertThat(forbiddenPkg.getDependencies()).hasSize(2);
        assertThat(forbiddenPkg.getDependencies()).contains(new NotDependsOn("dummy.bad"), new NotDependsOn("dummy.test"));

        testee.analyze(new File("target/test-classes/dummy/test/bad"));
        final List<DependencyError> errors = testee.getDependencyErrors();
        for (DependencyError error : errors) {
            System.err.println(error);
        }
        assertThat(errors).hasSize(3);

        final List<String> messages = new ArrayList<String>();
        messages.add(errors.get(0).toString());
        messages.add(errors.get(1).toString());
        messages.add(errors.get(2).toString());

        assertThat(messages).contains("dummy.test.bad.BadOne => dummy.test " + "[You should not use something from the parent package!]",
                "dummy.test.bad.BadOne => dummy.bad.a [This is an evil package!]",
                "dummy.test.bad.BadOne => org.fuin.utils4j " + "[This is a nice utility but not allowed here!]");

    }

    @Test
    public final void testAlwaysForbidden() throws InvalidDependenciesDefinitionException, IOException {

        final DependencyAnalyzer testee = new DependencyAnalyzer(this.getClass(), "/dummy-always-forbidden.xml");
        testee.analyze(new File("target/test-classes/dummy/test/bad"));
        final List<DependencyError> errors = testee.getDependencyErrors();
        assertThat(errors).hasSize(3);

        final List<String> messages = new ArrayList<String>();
        messages.add(errors.get(0).toString());
        messages.add(errors.get(1).toString());
        messages.add(errors.get(2).toString());

        assertThat(messages).contains("dummy.test.bad.BadOne => dummy.bad.a", "dummy.test.bad.BadOne => dummy.test",
                "dummy.test.bad.BadOne => org.fuin.utils4j");

    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testAnalyzeValid() throws InvalidDependenciesDefinitionException, IOException {

        final DependencyAnalyzer testee = new DependencyAnalyzer(this.getClass(), "/dummy-allowed.xml");

        final List<Package<DependsOn>> allowed = testee.getDependencies().getAllowed();
        assertThat(allowed).hasSize(1);
        assertThat(allowed).contains(new Package<DependsOn>("dummy.test.good"));
        final Package<DependsOn> allowedPkg = allowed.get(0);
        assertThat(allowedPkg.getDependencies()).hasSize(2);
        assertThat(allowedPkg.getDependencies()).contains(new DependsOn("dummy.good"), new DependsOn("org.fuin.utils4j"));

        testee.analyze(new File("target/test-classes/dummy/test/good"));
        final List<DependencyError> errors = testee.getDependencyErrors();
        assertThat(errors).isEmpty();

    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testNothingAllowed() throws InvalidDependenciesDefinitionException, IOException {

        final DependencyAnalyzer testee = new DependencyAnalyzer(this.getClass(), "/dummy-nothing-allowed.xml");

        final List<Package<DependsOn>> allowed = testee.getDependencies().getAllowed();
        assertThat(allowed).hasSize(1);
        assertThat(allowed).contains(new Package<DependsOn>("dummy.nothing"));
        final Package<DependsOn> allowedPkg = allowed.get(0);
        assertThat(allowedPkg.getDependencies()).hasSize(0);

        testee.analyze(new File("target/test-classes/dummy/nothing"));
        final List<DependencyError> errors = testee.getDependencyErrors();
        assertThat(errors).isEmpty();

    }

}
// CHECKSTYLE:ON
