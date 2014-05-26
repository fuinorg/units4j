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

import org.fuin.utils4j.Utils4J;

/**
 * Some kind of dependency is not valid.
 */
public final class DependencyError {

    private final String className;

    private final String referencedPackage;

    private final String comment;

    /**
     * Constructor with all data.
     * 
     * @param className
     *            Name of the class that has an invalid reference to another
     *            package.
     * @param referencedPackage
     *            The package referenced by the class.
     * @param comment
     *            Comment describing why the dependency is not OK or
     *            <code>null</code>.
     */
    public DependencyError(final String className,
            final String referencedPackage, final String comment) {
        super();
        Utils4J.checkNotNull("className", className);
        Utils4J.checkNotNull("referencedPackage", referencedPackage);
        this.className = className;
        this.referencedPackage = referencedPackage;
        this.comment = comment;
    }

    /**
     * Returns the name of the class that has an invalid reference to another
     * package.
     * 
     * @return Full qualified class name.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Returns the package that is referenced by the class.
     * 
     * @return Full qualified package name.
     */
    public final String getReferencedPackage() {
        return referencedPackage;
    }

    /**
     * Returns the comment describing why the dependency is not OK.
     * 
     * @return Comment or <code>null</code>
     */
    public final String getComment() {
        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        if (comment == null) {
            return className + " => " + referencedPackage;
        }
        return className + " => " + referencedPackage + " [" + comment + "]";
    }

}
