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

import org.fuin.utils4j.ToDebugStringCapable;
import org.fuin.utils4j.Utils4J;

// CHECKSTYLE:OFF
/**
 * Package and it's allowed or disallowed dependencies (this depends on the context of this object. Two objects are considered equal if the
 * names are equal.
 * 
 * @param <DEP_TYPE>
 *            Type of dependency.
 */
// CHECKSTYLE:ON
public final class Package<DEP_TYPE extends Dependency> implements ToDebugStringCapable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final String comment;

    private final List<DEP_TYPE> dependencies;

    /**
     * Constructor with name.
     * 
     * @param name
     *            Name of the package - Cannot be <code>null</code>.
     */
    public Package(final String name) {
        this(name, null);
    }

    /**
     * Constructor with name and comment.
     * 
     * @param name
     *            Name of the package - Cannot be <code>null</code>.
     * @param comment
     *            Comment why restrictions apply.
     */
    public Package(final String name, final String comment) {
        super();
        Utils4J.checkNotNull("name", name);
        this.name = name;
        this.comment = comment;
        this.dependencies = new ArrayList<DEP_TYPE>();
    }

    /**
     * Returns the package name.
     * 
     * @return Name of the package.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the list of related packages.
     * 
     * @return List of package dependencies.
     */
    public final List<DEP_TYPE> getDependencies() {
        if (dependencies == null) {
            // This can only happen with serialization
            return Collections.emptyList();
        }
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // CHECKSTYLE:OFF
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        // CHECKSTYLE:ON
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Package<?> other = (Package<?>) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a comment why restrictions apply.
     * 
     * @return Description of the restriction for all dependencies in the package.
     */
    public final String getComment() {
        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String toDebugString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("name=" + name + ", ");
        sb.append("comment=" + comment + ", ");
        sb.append("dependencies={");
        final List<DEP_TYPE> list = getDependencies();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(list.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

}
