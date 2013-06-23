/**
 * Copyright (C) 2009 Future Invent Informationsmanagement GmbH. All rights
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

import org.fuin.utils4j.ToDebugStringCapable;
import org.fuin.utils4j.Utils4J;

/**
 * A dependency to a package.
 */
public abstract class Dependency implements ToDebugStringCapable {

    private final String packageName;

    private final Boolean includeSubPackages;

    /**
     * Constructor with name and comment.
     * 
     * @param packageName
     *            Full qualified name of the package - Cannot be
     *            <code>null</code>.
     */
    public Dependency(final String packageName) {
        this(packageName, true);
    }

    /**
     * Constructor with all possible arguments.
     * 
     * @param packageName
     *            Full qualified name of the package - Cannot be
     *            <code>null</code>.
     * @param includeSubPackages
     *            If sub package dependencies are included <code>true</code>
     *            else <code>false</code>.
     */
    public Dependency(final String packageName, final boolean includeSubPackages) {
        super();
        Utils4J.checkNotNull("packageName", packageName);
        this.packageName = packageName;
        this.includeSubPackages = includeSubPackages;
    }

    /**
     * Returns the name of the package.
     * 
     * @return Full qualified name of the package.
     */
    public final String getPackageName() {
        return packageName;
    }

    /**
     * Does this include the sub packages?
     * 
     * @return If sub package dependencies are included <code>true</code>
     *         (default) else <code>false</code>.
     */
    public final boolean isIncludeSubPackages() {
        if (includeSubPackages == null) {
            return true;
        }
        return includeSubPackages;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dependency other = (Dependency) obj;
        if (packageName == null) {
            if (other.packageName != null)
                return false;
        } else if (!packageName.equals(other.packageName))
            return false;
        return true;
    }
    // CHECKSTYLE:ON

}
