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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

// CHECKSTYLE:OFF Test code
public class AbstractPersistenceTestTest extends AbstractPersistenceTest {

    @Test
    public final void testPersistence() {

        // PREPARE
        beginTransaction();
        getEm().persist(new MyTestClass(1, "John Doe"));
        getEm().persist(new MyTestClass(2, "Jane Doe"));
        commitTransaction();

        // TEST
        beginTransaction();
        final MyTestClass john = getEm().find(MyTestClass.class, 1);
        assertThat(john).isNotNull();
        final MyTestClass jane = getEm().find(MyTestClass.class, 2);
        assertThat(jane).isNotNull();
        getEm().remove(jane);
        commitTransaction();

        // VERIFY
        beginTransaction();
        final List<MyTestClass> resultList = getEm().createQuery(
                "select t from MyTestClass t", MyTestClass.class)
                .getResultList();
        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0).getId()).isEqualTo(Integer.valueOf(1));
        assertThat(resultList.get(0).getName()).isEqualTo("John Doe");
        commitTransaction();

    }

}
// CHECKSTYLE:ON

