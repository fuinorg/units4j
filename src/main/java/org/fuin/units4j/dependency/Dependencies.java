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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Package dependency rules. Caution: A package cannot be in both lists!
 */
@XmlRootElement(name = "dependencies")
public final class Dependencies implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElementWrapper(name="alwaysAllowed")
    @XmlElement(name = "dependsOn")
    private final List<DependsOn> alwaysAllowed;

    @XmlElementWrapper(name="alwaysForbidden")
    @XmlElement(name = "notDependsOn")
    private final List<NotDependsOn> alwaysForbidden;

    @XmlElementWrapper(name="allowed")
    @XmlElement(name = "package")
    private final List<Package<DependsOn>> allowed;

    @XmlElementWrapper(name="forbidden")
    @XmlElement(name = "package")
    private final List<Package<NotDependsOn>> forbidden;

    /**
     * Default constructor.
     */
    public Dependencies() {
        super();
        this.alwaysAllowed = new ArrayList<DependsOn>();
        this.alwaysForbidden = new ArrayList<NotDependsOn>();
        this.allowed = new ArrayList<Package<DependsOn>>();
        this.forbidden = new ArrayList<Package<NotDependsOn>>();
    }

    /**
     * Returns a list of packages like "java.lang" that are always Ok to depend on.
     * 
     * @return List of packages.
     */
    public final List<DependsOn> getAlwaysAllowed() {
        if (alwaysAllowed == null) {
            // This can only happen with serialization
            return Collections.emptyList();
        }
        return alwaysAllowed;
    }

    /**
     * Returns a list of packages that are always forbidden to depend on.
     * 
     * @return List of packages.
     */
    public final List<NotDependsOn> getAlwaysForbidden() {
        if (alwaysForbidden == null) {
            // This can only happen with serialization
            return Collections.emptyList();
        }
        return alwaysForbidden;
    }

    /**
     * Checks if the package is always OK.
     * 
     * @param packageName
     *            Name of the package to check.
     * 
     * @return If the package is always allowed <code>true</code> else <code>false</code>.
     */
    public final boolean isAlwaysAllowed(final String packageName) {
        if (packageName.equals("java.lang")) {
            return true;
        }
        return Utils.findAllowedByName(getAlwaysAllowed(), packageName) != null;
    }

    /**
     * Checks if the package is always forbidden.
     * 
     * @param packageName
     *            Name of the package to check.
     * 
     * @return If the package is always forbidden <code>true</code> else <code>false</code>.
     */
    public final boolean isAlwaysForbidden(final String packageName) {
        return Utils.findForbiddenByName(getAlwaysForbidden(), packageName) != null;
    }

    /**
     * Returns the list of allowed package dependencies.
     * 
     * @return List of explicit allowed dependencies - All other dependencies are considered to be an error.
     */
    public final List<Package<DependsOn>> getAllowed() {
        if (allowed == null) {
            // This can only happen with serialization
            return Collections.emptyList();
        }
        return allowed;
    }

    /**
     * Returns the list of forbidden package dependencies.
     * 
     * @return List of explicit forbidden dependencies - All other dependencies are considered to be valid.
     */
    public final List<Package<NotDependsOn>> getForbidden() {
        if (forbidden == null) {
            // This can only happen with serialization
            return Collections.emptyList();
        }
        return forbidden;
    }

    /**
     * Checks if the definition is valid - Especially if no package is in both lists.
     * 
     * @throws InvalidDependenciesDefinitionException
     *             This instance is invalid.
     */
    public final void validate() throws InvalidDependenciesDefinitionException {

        int errorCount = 0;
        final StringBuilder sb = new StringBuilder("Duplicate package entries in 'allowed' and 'forbidden': ");
        final List<Package<NotDependsOn>> list = getForbidden();
        for (int i = 0; i < list.size(); i++) {
            final String name = list.get(i).getName();
            final Package<DependsOn> dep = new Package<DependsOn>(name);
            if (getAllowed().indexOf(dep) > -1) {
                if (errorCount > 0) {
                    sb.append(", ");
                }
                sb.append(name);
                errorCount++;
            }
        }
        if (errorCount > 0) {
            throw new InvalidDependenciesDefinitionException(this, sb.toString());
        }
    }

    /**
     * Find an entry in the allowed list by package name.
     * 
     * @param packageName
     *            Name to find.
     * 
     * @return Package or <code>null</code> if no entry with the given name was found.
     */
    public final Package<DependsOn> findAllowedByName(final String packageName) {
        final List<Package<DependsOn>> list = getAllowed();
        for (final Package<DependsOn> pkg : list) {
            if (pkg.getName().equals(packageName)) {
                return pkg;
            }
        }
        return null;
    }

    /**
     * Find an entry in the forbidden list by package name.
     * 
     * @param packageName
     *            Name to find.
     * 
     * @return Package or <code>null</code> if no entry with the given name was found.
     */
    public final Package<NotDependsOn> findForbiddenByName(final String packageName) {
        final List<Package<NotDependsOn>> list = getForbidden();
        for (final Package<NotDependsOn> pkg : list) {
            if (pkg.getName().equals(packageName)) {
                return pkg;
            }
        }
        return null;
    }

}
