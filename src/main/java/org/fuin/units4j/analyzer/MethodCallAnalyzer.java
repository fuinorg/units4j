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
package org.fuin.units4j.analyzer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;

/**
 * Locates calls to a given method by analyzing Java ".class" files.
 */
public final class MethodCallAnalyzer {

    private MCAClassVisitor cv;

    /**
     * Constructor with open array.
     * 
     * @param methodsToFind
     *            List of methods to find.
     */
    public MethodCallAnalyzer(final MCAMethod... methodsToFind) {
        super();
        if (methodsToFind == null) {
            throw new IllegalArgumentException("Argument 'methodsToFind' cannot be null");
        }
        if (methodsToFind.length == 0) {
            throw new IllegalArgumentException("Argument 'methodsToFind' cannot be empty");
        }
        this.cv = new MCAClassVisitor(Arrays.asList(methodsToFind));
    }

    /**
     * Constructor with list.
     * 
     * @param methodsToFind
     *            List of methods to find.
     */
    public MethodCallAnalyzer(final List<MCAMethod> methodsToFind) {
        super();
        if (methodsToFind.size() == 0) {
            throw new IllegalArgumentException("Argument 'methodsToFind' cannot be empty");
        }
        this.cv = new MCAClassVisitor(methodsToFind);
    }

    /**
     * Locate method calls in classes of a JAR file.
     * 
     * @param file
     *            File to search.
     * 
     * @throws IOException
     *             Error reading the file.
     */
    public final void findCallingMethodsInJar(final File file) throws IOException {

        final JarFile jarFile = new JarFile(file);
        final Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(".class")) {
                final InputStream in = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                try {
                    new ClassReader(in).accept(cv, 0);
                } finally {
                    in.close();
                }
            }
        }
    }

    /**
     * Locate method calls in classes of a directory.
     * 
     * @param dir
     *            Directory to search (including sub directories).
     */
    public final void findCallingMethodsInDir(final File dir) {
        try {
            final File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(final File file) {
                    return file.getName().endsWith(".class") || file.isDirectory();
                }
            });
            for (final File file : files) {
                if (file.getName().endsWith(".class")) {
                    final InputStream in = new BufferedInputStream(new FileInputStream(file), 1024);
                    try {
                        new ClassReader(in).accept(cv, 0);
                    } finally {
                        in.close();
                    }
                } else {
                    findCallingMethodsInDir(file);
                }
            }
        } catch (final IOException ex) {
            throw new RuntimeException("Error reading: " + dir, ex);
        }
    }

    /**
     * Returns the list of methods to find.
     * 
     * @return Method list.
     */
    public List<MCAMethod> getMethodsToFind() {
        return cv.getMethodsToFind();
    }

    /**
     * Returns the list of found method calls.
     * 
     * @return List.
     */
    public final List<MCAMethodCall> getMethodCalls() {
        return cv.getMethodCalls();
    }

    /**
     * Clears the list of found method calls.
     */
    public final void clearMethodCalls() {
        cv.clearMethodCalls();
    }

}
