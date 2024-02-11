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

import java.lang.annotation.*;

/**
 * States that the annotated test class contains no tests by intention. This avoids test failures when test coverage checks are done.
 *
 * @deprecated Use class with same name from <a href="https://github.com/fuinorg/utils4j">Utils4J</a>.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface TestOmitted {

    /**
     * Short description why the test class was not implemented.
     * 
     * @return Explanation why the test was skipped
     */
    String value();

}
