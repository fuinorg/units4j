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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.List;

import org.fuin.utils4j.Utils4J;

import com.thoughtworks.xstream.XStream;

/**
 * Utilities for the package.
 */
public final class Utils {

    /**
     * Private default constructor.
     */
    private Utils() {
        throw new UnsupportedOperationException(
                "This utility class is not intended to be instanciated!");
    }

    /**
     * Creates a ready configured XStream instance.
     * 
     * @return New XStream instance.
     */
    public static XStream createXStream() {
        final XStream xstream = new XStream();
        xstream.alias("dependencies", Dependencies.class);
        xstream.alias("package", Package.class);
        xstream.alias("dependsOn", DependsOn.class);
        xstream.alias("notDependsOn", NotDependsOn.class);
        xstream.aliasField("package", Dependency.class, "packageName");
        xstream.useAttributeFor(Package.class, "name");
        xstream.useAttributeFor(Package.class, "comment");
        xstream.useAttributeFor(Dependency.class, "packageName");
        xstream.useAttributeFor(Dependency.class, "includeSubPackages");
        xstream.useAttributeFor(NotDependsOn.class, "comment");
        xstream.addImplicitCollection(Package.class, "dependencies");
        return xstream;
    }

    /**
     * Load dependencies from a file.
     * 
     * @param file
     *            XML File to load - Cannot be <code>null</code> and must be a
     *            valid file.
     * 
     * @return New dependencies instance.
     */
    public static Dependencies load(final File file) {
        Utils4J.checkNotNull("file", file);
        Utils4J.checkValidFile(file);
        try {
            final InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                return load(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Load dependencies from an input stream.
     * 
     * @param inputStream
     *            XML input stream to read - Cannot be <code>null</code>.
     * 
     * @return New dependencies instance.
     */
    public static Dependencies load(final InputStream inputStream) {
        Utils4J.checkNotNull("inputStream", inputStream);
        final XStream xstream = createXStream();
        final Reader reader = new InputStreamReader(inputStream);
        return (Dependencies) xstream.fromXML(reader);
    }

    /**
     * Load dependencies from a resource.
     * 
     * @param clasz
     *            Class to use for loading the resource - Cannot be
     *            <code>null</code>.
     * @param resourcePathAndName
     *            Name and path of the XML file (in the class path) - Cannot be
     *            <code>null</code>.
     * 
     * @return New dependencies instance.
     */
    public static Dependencies load(final Class<?> clasz, final String resourcePathAndName) {
        Utils4J.checkNotNull("clasz", clasz);
        Utils4J.checkNotNull("resourcePathAndName", resourcePathAndName);

        try {
            final URL url = clasz.getResource(resourcePathAndName);
            if (url == null) {
                throw new RuntimeException("Resource '" + resourcePathAndName + "' not found!");
            }
            final InputStream in = url.openStream();
            try {
                return load(in);
            } finally {
                in.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Write the dependencies to a file.
     * 
     * @param file
     *            XML File - Cannot be <code>null</code> and must be a valid
     *            file.
     * @param dependencies
     *            Dependencies to save.
     */
    public static void save(final File file, final Dependencies dependencies) {
        Utils4J.checkNotNull("file", file);
        Utils4J.checkValidFile(file);
        final XStream xstream = createXStream();
        try {
            final Writer writer = new FileWriter(file);
            try {
                xstream.toXML(dependencies, writer);
            } finally {
                writer.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Find a dependency in a list by a package name.
     * 
     * @param allowed
     *            List to search.
     * @param pkgName
     *            Name of the package to find - Cannot be <code>null</code>.
     * 
     * @return Entry or <code>null</code> if nothing was found.
     */
    public static DependsOn findAllowedByName(final List<DependsOn> allowed, final String pkgName) {
        if (allowed == null) {
            return null;
        }
        Utils4J.checkNotNull("pkgName", pkgName);
        for (int i = 0; i < allowed.size(); i++) {
            final DependsOn dep = allowed.get(i);
            if (pkgName.startsWith(dep.getPackageName())) {
                if (dep.isIncludeSubPackages()) {
                    return dep;
                } else {
                    if (pkgName.equals(dep.getPackageName())) {
                        return dep;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find a dependency in a list by a package name.
     * 
     * @param forbidden
     *            List to search.
     * @param pkgName
     *            Name of the package to find - Cannot be <code>null</code>.
     * 
     * @return Entry or <code>null</code> if nothing was found.
     */
    public static NotDependsOn findForbiddenByName(final List<NotDependsOn> forbidden,
            final String pkgName) {
        if (forbidden == null) {
            return null;
        }
        Utils4J.checkNotNull("pkgName", pkgName);
        for (int i = 0; i < forbidden.size(); i++) {
            final NotDependsOn dep = forbidden.get(i);
            if (pkgName.startsWith(dep.getPackageName())) {
                if (dep.isIncludeSubPackages()) {
                    return dep;
                } else {
                    if (pkgName.equals(dep.getPackageName())) {
                        return dep;
                    }
                }
            }
        }
        return null;
    }

}
