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
package org.fuin.units4j;

import javax.validation.constraints.NotNull;

/**
 * Result of a verification.
 */
public final class AssertionResult {

    /** OK result. */
    public static final AssertionResult OK = new AssertionResult();

    private final boolean valid;

    private final String errorMessage;

    /**
     * Private OK constructor.
     */
    private AssertionResult() {
        super();
        this.valid = true;
        this.errorMessage = "";
    }

    /**
     * Error constructor.
     * 
     * @param errorMessage
     *            error message in case the object is invalid.
     */
    public AssertionResult(@NotNull final String errorMessage) {
        super();
        this.valid = false;
        this.errorMessage = errorMessage;
    }

    /**
     * Returns the information if the rule was observed.
     * 
     * @return TRUE if the object is valid.
     */
    public final boolean isValid() {
        return valid;
    }

    /**
     * Returns the error message in case the object is invalid.
     * 
     * @return Error message.
     */
    @NotNull
    public final String getErrorMessage() {
        return errorMessage;
    }

}
