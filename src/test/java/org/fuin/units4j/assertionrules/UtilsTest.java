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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.fuin.units4j.Units4JUtils;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;
import org.jboss.jandex.Type.Kind;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class UtilsTest {

    private static Index index;

    @BeforeClass
    public static void beforeClass() {
        final File dir = new File("target/test-classes");
        final List<File> classFiles = Units4JUtils.findAllClasses(dir);
        index = Units4JUtils.indexAllClasses(classFiles);
    }

    @AfterClass
    public static void afterClass() {
        index = null;
    }

    @Test
    public void testFindInterfaceNone() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("methodX", Type.create(DotName.createSimple(String.class.getName()), Kind.CLASS));

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).isEmpty();

    }

    @Test
    public void testFindBaseClass() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("methodMyBaseClass1",
                Type.create(DotName.createSimple(Integer.class.getName()), Kind.CLASS));

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).hasSize(1);
        assertThat(intfMethods.get(0).name()).isEqualTo("methodMyBaseClass1");
        assertThat(intfMethods.get(0).declaringClass().name().toString()).isEqualTo(MyBaseClass1.class.getName());

    }

    @Test
    public void testFindInterface1() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("methodMyInterface1",
                Type.create(DotName.createSimple(Long.class.getName()), Kind.CLASS));

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).hasSize(1);
        assertThat(intfMethods.get(0).name()).isEqualTo("methodMyInterface1");
        assertThat(intfMethods.get(0).declaringClass().name().toString()).isEqualTo(MyInterface1.class.getName());

    }

    @Test
    public void testFindInterface2() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("methodMyInterface2",
                Type.create(DotName.createSimple(Boolean.class.getName()), Kind.CLASS));

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).hasSize(1);
        assertThat(intfMethods.get(0).name()).isEqualTo("methodMyInterface2");
        assertThat(intfMethods.get(0).declaringClass().name().toString()).isEqualTo(MyInterface2.class.getName());

    }

    @Test
    public void testFindInterface3() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("methodMyInterface3");

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).hasSize(1);
        assertThat(intfMethods.get(0).name()).isEqualTo("methodMyInterface3");
        assertThat(intfMethods.get(0).declaringClass().name().toString()).isEqualTo(MySuperInterface.class.getName());

    }

    @Test
    public void testFindInterface4() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("methodMyInterface4");

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).hasSize(1);
        assertThat(intfMethods.get(0).name()).isEqualTo("methodMyInterface4");
        assertThat(intfMethods.get(0).declaringClass().name().toString()).isEqualTo(MyInterface4.class.getName());

    }

    @Test
    public void testFindAll() {

        // PREPARE
        final ClassInfo implClass = index.getClassByName(DotName.createSimple(MyClass.class.getName()));
        final MethodInfo implMethod = implClass.method("all");

        // TEST
        final List<MethodInfo> intfMethods = Utils.findOverrideMethods(index, implMethod);

        // VERIFY
        assertThat(intfMethods).hasSize(3);
        assertThat(intfMethods.get(0).name()).isEqualTo("all");
        assertThat(intfMethods.get(0).declaringClass().name().toString()).isEqualTo(MyInterface1.class.getName());
        assertThat(intfMethods.get(1).name()).isEqualTo("all");
        assertThat(intfMethods.get(1).declaringClass().name().toString()).isEqualTo(MyInterface2.class.getName());
        assertThat(intfMethods.get(2).name()).isEqualTo("all");
        assertThat(intfMethods.get(2).declaringClass().name().toString()).isEqualTo(MyBaseClass1.class.getName());

    }

    public static class MyClass extends MyBaseClass1 implements MyInterface1, MyInterface2, MyInterface3 {

        public void methodX(String str) {
        }

        public Integer methodMyBaseClass1(Integer a) {
            return null;
        }

        public Boolean methodMyInterface1(Long b) {
            return null;
        }

        public void methodMyInterface2(Boolean c) {
        }

        public void all() {
        }

        public void methodMyInterface3() {
        }

        public void methodMyInterface4() {
        }

        public void methodMyBaseClass2() {
        }

    }

    public static abstract class MyBaseClass1 extends MyBaseClass2 {

        public abstract Integer methodMyBaseClass1(Integer a);

        public abstract void all();

    }

    public static abstract class MyBaseClass2 implements MyInterface4 {

        public abstract void methodMyBaseClass2();

    }

    public static interface MyInterface1 {

        public Boolean methodMyInterface1(Long b);

        public void all();

    }

    public static interface MyInterface2 {

        public void methodMyInterface2(Boolean c);

        public void all();

    }

    public static interface MyInterface3 extends MySuperInterface {

    }

    public static interface MySuperInterface {

        public void methodMyInterface3();

    }

    public static interface MyInterface4 {

        public void methodMyInterface4();

    }

}
// CHECKSTYLE:ON
