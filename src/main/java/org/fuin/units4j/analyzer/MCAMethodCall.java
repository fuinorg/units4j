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
package org.fuin.units4j.analyzer;

/**
 * Represents a place where another method is called.
 */
public final class MCAMethodCall {

    private final MCAMethod called;

    private final MCAMethod caller;

    private final String sourceFileName;

    private final int line;

    /**
     * Constructor with all data.
     * 
     * @param called
     *            Called method.
     * @param className
     *            Name of the class where it's called.
     * @param methodName
     *            Name of the method where it's called.
     * @param methodDescr
     *            Description of the method it's called.
     * @param sourceFileName
     *            Name of the source file.
     * @param line
     *            Line number in the source file.
     */
    public MCAMethodCall(final MCAMethod called, final String className, final String methodName,
            final String methodDescr, final String sourceFileName, final int line) {
        if (called == null) {
            throw new IllegalArgumentException("Argument 'called' canot be NULL");
        }
        this.called = called;
        this.caller = new MCAMethod(className, methodName, methodDescr);
        this.sourceFileName = sourceFileName;
        this.line = line;
    }

    /**
     * Returns the called method.
     * 
     * @return Method that is executed.
     */
    public final MCAMethod getCalled() {
        return called;
    }

    /**
     * Returns the calling method.
     * 
     * @return Method where the other method is referenced.
     */
    public final MCAMethod getCaller() {
        return caller;
    }

    /**
     * Returns the source file name.
     * 
     * @return Name of the source file or NULL.
     */
    public final String getSourceFileName() {
        return sourceFileName;
    }

    /**
     * Returns the line number in source file.
     * 
     * @return Source code line number.
     */
    public final int getLine() {
        return line;
    }

    @Override
    public final String toString() {
        return "Source='" + sourceFileName + "', Line=" + line + ", " + caller + " ==CALLS==> "
                + called;
    }
}
