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
package org.fuin.units4j;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * JUnit 4 runner for use with unit tests that need CDI support.
 */
public final class WeldJUnit4Runner extends BlockJUnit4ClassRunner {

    private final Class<?> unitTestClass;

    private final Weld weld;

    private final WeldContainer container;

    /**
     * Constructor with unit test class.
     * 
     * @param unitTestClass
     *            Unit test class.
     * 
     * @throws InitializationError
     *             if the test class is malformed.
     */
    public WeldJUnit4Runner(final Class<Object> unitTestClass) throws InitializationError {
        super(unitTestClass);
        this.unitTestClass = unitTestClass;
        this.weld = new Weld();
        this.container = weld.initialize();
    }

    @Override
    protected final Object createTest() throws Exception {
        return container.instance().select(unitTestClass).get();
    }

}
