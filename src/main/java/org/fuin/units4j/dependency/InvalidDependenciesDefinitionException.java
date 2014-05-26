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
 * The {@link Dependencies} object is invalid.
 */
public class InvalidDependenciesDefinitionException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Dependencies dependencies;

    /**
     * Constructor with dependencies and message.
     * 
     * @param dependencies
     *            Invalid definition.
     * @param message
     *            Error message.
     */
    public InvalidDependenciesDefinitionException(
            final Dependencies dependencies, final String message) {
        super(message);
        Utils4J.checkNotNull("dependencies", dependencies);
        this.dependencies = dependencies;
    }

    /**
     * Returns the definition that caused the error.
     * 
     * @return Invalid definition.
     */
    public final Dependencies getDependencies() {
        return dependencies;
    }

}
