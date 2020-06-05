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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuin.utils4j.Utils4J;
import org.fuin.utils4j.fileprocessor.FileHandler;
import org.fuin.utils4j.fileprocessor.FileHandlerResult;
import org.fuin.utils4j.fileprocessor.FileProcessor;
import org.objectweb.asm.ClassReader;

/**
 * Analyzes package dependencies.
 */
public final class DependencyAnalyzer {

    private final List<DependencyError> dependencyErrors;

    private final Dependencies dependencies;

    /**
     * Constructor with XML file.
     * 
     * @param dependenciesFile
     *            XML file with allowed or forbidden dependencies - Cannot be <code>null</code>.
     * 
     * @throws InvalidDependenciesDefinitionException
     *             Formal correct (XML) but invalid dependency definition.
     */
    public DependencyAnalyzer(final File dependenciesFile) throws InvalidDependenciesDefinitionException {
        this(Utils.load(dependenciesFile));
    }

    /**
     * Constructor with XML resource.
     * 
     * @param clasz
     *            Class to use for loading the resource - Cannot be <code>null</code>.
     * @param dependenciesFilePathAndName
     *            XML resource (path/name) with allowed or forbidden dependencies - Cannot be <code>null</code>.
     * 
     * @throws InvalidDependenciesDefinitionException
     *             Formal correct (XML) but invalid dependency definition.
     */
    public DependencyAnalyzer(final Class<?> clasz, final String dependenciesFilePathAndName)
            throws InvalidDependenciesDefinitionException {
        this(Utils.load(clasz, dependenciesFilePathAndName));
    }

    /**
     * Constructor with dependency definition.
     * 
     * @param dependencies
     *            Definition of allowed or forbidden dependencies - Cannot be <code>null</code>.
     * 
     * @throws InvalidDependenciesDefinitionException
     *             Formal correct (XML) but invalid dependency definition.
     */
    public DependencyAnalyzer(final Dependencies dependencies) throws InvalidDependenciesDefinitionException {
        super();
        Utils4J.checkNotNull("dependencies", dependencies);
        this.dependencies = dependencies;
        this.dependencies.validate();
        dependencyErrors = new ArrayList<>();
    }

    /**
     * Checks the dependencies for a package from the "allowed" section.
     * 
     * @param dependencies
     *            Dependency definition to use.
     * @param allowedPkg
     *            Package with allowed imports.
     * @param classInfo
     *            Information extracted from the class.
     * 
     * @return List of errors - may be empty but is never <code>null</code>.
     */
    private List<DependencyError> checkAllowedSection(final Dependencies dependencies, final Package<DependsOn> allowedPkg,
            final ClassInfo classInfo) {

        final List<DependencyError> errors = new ArrayList<>();

        final Iterator<String> it = classInfo.getImports().iterator();
        while (it.hasNext()) {
            final String importedPkg = it.next();
            if (!importedPkg.equals(allowedPkg.getName()) && !dependencies.isAlwaysAllowed(importedPkg)) {
                final DependsOn dep = Utils.findAllowedByName(allowedPkg.getDependencies(), importedPkg);
                if (dep == null) {
                    errors.add(new DependencyError(classInfo.getName(), importedPkg, allowedPkg.getComment()));
                }
            }
        }
        return errors;

    }

    /**
     * Checks the dependencies for a package from the "forbidden" section.
     * 
     * @param dependencies
     *            Dependency definition to use.
     * @param forbiddenPkg
     *            Package with forbidden imports.
     * @param classInfo
     *            Information extracted from the class.
     * 
     * @return List of errors - may be empty but is never <code>null</code>.
     */
    private static List<DependencyError> checkForbiddenSection(final Dependencies dependencies, final Package<NotDependsOn> forbiddenPkg,
            final ClassInfo classInfo) {

        final List<DependencyError> errors = new ArrayList<>();

        final Iterator<String> it = classInfo.getImports().iterator();
        while (it.hasNext()) {
            final String importedPkg = it.next();
            if (!importedPkg.equals(classInfo.getPackageName())) {
                final NotDependsOn ndo = Utils.findForbiddenByName(dependencies.getAlwaysForbidden(), importedPkg);
                if (ndo != null) {
                    errors.add(new DependencyError(classInfo.getName(), importedPkg, ndo.getComment()));
                } else {
                    final NotDependsOn dep = Utils.findForbiddenByName(forbiddenPkg.getDependencies(), importedPkg);
                    if (dep != null) {
                        final String comment;
                        if (dep.getComment() == null) {
                            comment = forbiddenPkg.getComment();
                        } else {
                            comment = dep.getComment();
                        }
                        errors.add(new DependencyError(classInfo.getName(), importedPkg, comment));
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Checks if any of the imports is listed in the "alwaysForbidden" section.
     * 
     * @param dependencies
     *            Dependencies to use.
     * @param classInfo
     *            Information extracted from the class.
     * 
     * @return List of errors - may be empty but is never <code>null</code>.
     */
    private static List<DependencyError> checkAlwaysForbiddenSection(final Dependencies dependencies, final ClassInfo classInfo) {

        final List<DependencyError> errors = new ArrayList<DependencyError>();

        final Iterator<String> importedPackages = classInfo.getImports().iterator();
        while (importedPackages.hasNext()) {
            final String importedPackage = importedPackages.next();
            final NotDependsOn ndo = Utils.findForbiddenByName(dependencies.getAlwaysForbidden(), importedPackage);
            if (ndo != null) {
                errors.add(new DependencyError(classInfo.getName(), importedPackage, ndo.getComment()));
            }
        }

        return errors;
    }

    /**
     * Analyze the dependencies for all classes in the directory and it's sub directories.
     * 
     * @param classesDir
     *            Directory where the "*.class" files are located (something like "bin" or "classes").
     */
    public final void analyze(final File classesDir) {

        final FileProcessor fileProcessor = new FileProcessor(new FileHandler() {
            @Override
            public final FileHandlerResult handleFile(final File classFile) {
                if (!classFile.getName().endsWith(".class")) {
                    return FileHandlerResult.CONTINUE;
                }
                try {
                    final ClassInfo classInfo = new ClassInfo(classFile);

                    final Package<DependsOn> allowedPkg = dependencies.findAllowedByName(classInfo.getPackageName());
                    if (allowedPkg == null) {
                        final Package<NotDependsOn> forbiddenPkg = dependencies.findForbiddenByName(classInfo.getPackageName());
                        if (forbiddenPkg == null) {
                            dependencyErrors.addAll(checkAlwaysForbiddenSection(dependencies, classInfo));
                        } else {
                            dependencyErrors.addAll(checkForbiddenSection(dependencies, forbiddenPkg, classInfo));
                        }
                    } else {
                        dependencyErrors.addAll(checkAllowedSection(dependencies, allowedPkg, classInfo));
                    }
                } catch (final IOException ex) {
                    throw new RuntimeException("Error handling file: " + classFile, ex);
                }

                return FileHandlerResult.CONTINUE;

            }
        });

        dependencyErrors.clear();
        fileProcessor.process(classesDir);

    }

    /**
     * Returns the list of dependency errors from last call to {@link #analyze(File)}.
     * 
     * @return List of errors - Always non-<code>null</code> but may be empty.
     */
    public final List<DependencyError> getDependencyErrors() {
        return dependencyErrors;
    }

    /**
     * Returns the dependency definition.
     * 
     * @return Definition of allowed or forbidden dependencies - Never <code>null</code>.
     */
    public final Dependencies getDependencies() {
        return dependencies;
    }

    /**
     * Information about a class extracted from a class file.
     */
    private static class ClassInfo {

        private final String packageName;

        private final String simpleName;

        private final Set<String> imports;

        /**
         * Constructor with class file to load.
         * 
         * @param classFile
         *            Java ".class" file.
         * 
         * @throws IOException
         *             Error reading the file.
         */
        public ClassInfo(final File classFile) throws IOException {
            final InputStream in = new BufferedInputStream(new FileInputStream(classFile));
            try {
                final DependencyVisitor visitor = new DependencyVisitor();
                new ClassReader(in).accept(visitor, 0);
                final Map<String, Map<String, Integer>> globals = visitor.getGlobals();
                final Set<String> jarPackages = globals.keySet();
                packageName = jarPackages.iterator().next().replace('/', '.');
                simpleName = nameOnly(classFile.getName());
                imports = new HashSet<String>();
                final Iterator<String> it = visitor.getPackages().iterator();
                while (it.hasNext()) {
                    imports.add(it.next().replace('/', '.'));
                }
            } finally {
                in.close();
            }
        }

        /**
         * Returns the name of the package.
         * 
         * @return The package of the class.
         */
        public final String getPackageName() {
            return packageName;
        }

        /**
         * Returns a list of imported package names.
         * 
         * @return Names imported by the class.
         */
        public final Set<String> getImports() {
            return imports;
        }

        /**
         * Full qualified name of the class.
         * 
         * @return Class name.
         */
        public final String getName() {
            return packageName + "." + simpleName;
        }

        /**
         * Returns the name of the file without path an extension.
         * 
         * @param filename
         *            Filename to extract the name from.
         * 
         * @return Simple name.
         */
        private static String nameOnly(final String filename) {
            final int p = filename.lastIndexOf('.');
            if (p == -1) {
                return filename;
            }
            return filename.substring(0, p);
        }

    }

}
