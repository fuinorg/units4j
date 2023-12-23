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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.fuin.utils4j.Utils4J;
import org.fuin.utils4j.jaxb.JaxbUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Utilities for the package.
 */
public final class Utils {

    /**
     * Private default constructor.
     */
    private Utils() {
        throw new UnsupportedOperationException("This utility class is not intended to be instanciated!");
    }

    /**
     * Creates a JAXB context for usage in this package.
     * 
     * @return Initialized context.
     */
    static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(Dependencies.class, Package.class, DependsOn.class, NotDependsOn.class, Dependency.class);
        } catch (final JAXBException ex) {
            throw new RuntimeException("Failed to create JAXB context", ex);
        }
    }

    /**
     * Load dependencies from a file.
     * 
     * @param file
     *            XML File to load - Cannot be <code>null</code> and must be a valid file.
     * 
     * @return New dependencies instance.
     */
    public static Dependencies load(final File file) {
        Utils4J.checkNotNull("file", file);
        Utils4J.checkValidFile(file);
        try {
            try (final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                return load(inputStream);
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
        return JaxbUtils.unmarshal(createJaxbContext(), new InputStreamReader(inputStream, Charset.forName("utf-8")), null);
    }

    /**
     * Load dependencies from a resource.
     * 
     * @param clasz
     *            Class to use for loading the resource - Cannot be <code>null</code>.
     * @param resourcePathAndName
     *            Name and path of the XML file (in the class path) - Cannot be <code>null</code>.
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
            throw new RuntimeException("Failed to unmarshal from " + resourcePathAndName, ex);
        }

    }

    /**
     * Write the dependencies to a file.
     * 
     * @param file
     *            XML File - Cannot be <code>null</code> and must be a valid file.
     * @param dependencies
     *            Dependencies to save.
     */
    public static void save(final File file, final Dependencies dependencies) {
        Utils4J.checkNotNull("file", file);
        Utils4J.checkValidFile(file);
        try {
            try (final Writer writer = new FileWriter(file)) {
                JaxbUtils.marshal(createJaxbContext(), dependencies, null, writer);
            }
        } catch (final IOException ex) {
            throw new RuntimeException("Failed to marshal: " + dependencies, ex);
        }
    }

    static <T> String toXml(T obj, boolean standalone, boolean formattedOutput) {
        try {
            final Marshaller marshaller = createJaxbContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !standalone);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
            try (final StringWriter sw = new StringWriter()) {
                marshaller.marshal(obj, sw);
                return sw.toString();
            }
        } catch (final JAXBException | IOException ex) {
            throw new RuntimeException("Failed to marshal: " + obj, ex);
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
    public static NotDependsOn findForbiddenByName(final List<NotDependsOn> forbidden, final String pkgName) {
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
