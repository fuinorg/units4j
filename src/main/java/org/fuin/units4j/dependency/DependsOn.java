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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allowed dependency.
 */
@XmlRootElement(name = "dependsOn")
public final class DependsOn extends Dependency {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for unmarshalling.
     */
    protected DependsOn() {
        super();
    }

    /**
     * Constructor with name. Sub package dependencies are included.
     * 
     * @param packageName
     *            Full qualified name of the package - Cannot be <code>null</code>.
     */
    public DependsOn(final String packageName) {
        super(packageName);
    }

    /**
     * Constructor with name.
     * 
     * @param packageName
     *            Full qualified name of the package - Cannot be <code>null</code>.
     * @param includeSubPackages
     *            If sub package dependencies are included <code>true</code> else <code>false</code>.
     */
    public DependsOn(final String packageName, final boolean includeSubPackages) {
        super(packageName, includeSubPackages);
    }

    @Override
    public final String toString() {
        return getPackageName();
    }

    @Override
    public final String toDebugString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("packageName=" + getPackageName() + ", ");
        sb.append("includeSubPackages=" + isIncludeSubPackages() + ", ");
        return sb.toString();
    }

}
