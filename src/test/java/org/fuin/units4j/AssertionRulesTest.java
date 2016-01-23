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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests the {@link AssertionRules} class.
 */
// CHECKSTYLE:OFF Test code
public class AssertionRulesTest {

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testVerifyOK() {
        
        // PREPARE
        final AssertionRule ruleA = new AssertionRule() {
            @Override
            public AssertionResult verify(Object obj) {
                return AssertionResult.OK;
            }
        };
        final AssertionRule ruleB = new AssertionRule() {
            @Override
            public AssertionResult verify(Object obj) {
                return AssertionResult.OK;
            }
        };
        final AssertionRules rules = new AssertionRules(ruleA, ruleB);
        
        // TEST
        final AssertionResult result = rules.verify("does not matter here");
        
        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("");
        
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testVerifyMixed() {
        
        // PREPARE
        final String errorMsg = "Error!";
        final AssertionRule ruleA = new AssertionRule() {
            @Override
            public AssertionResult verify(Object obj) {
                return AssertionResult.OK;
            }
        };
        final AssertionRule ruleB = new AssertionRule() {
            @Override
            public AssertionResult verify(Object obj) {
                return new AssertionResult(errorMsg);
            }
        };
        final AssertionRules rules = new AssertionRules(ruleA, ruleB);
        
        // TEST
        final AssertionResult result = rules.verify("does not matter here");
        
        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo(errorMsg + "\n");
        
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testVerifyError() {
        
        // PREPARE
        final String errorMsgA = "ErrorA!";
        final String errorMsgB = "ErrorB!";
        final AssertionRule ruleA = new AssertionRule() {
            @Override
            public AssertionResult verify(Object obj) {
                return new AssertionResult(errorMsgA);
            }
        };
        final AssertionRule ruleB = new AssertionRule() {
            @Override
            public AssertionResult verify(Object obj) {
                return new AssertionResult(errorMsgB);
            }
        };
        final AssertionRules rules = new AssertionRules(ruleA, ruleB);
        
        // TEST
        final AssertionResult result = rules.verify("does not matter here");
        
        // VERIFY
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo(errorMsgA + "\n" + errorMsgB + "\n");
        
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testConstructionWithNoArgs() {
        
        try {
            new AssertionRules();
            fail();
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Argument 'rules' cannot be an empty array");
        }
        
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testConstructionWithEmptyArray() {
        
        try {
            final AssertionRule[] rules = new AssertionRule[] {}; 
            new AssertionRules(rules);
            fail();
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Argument 'rules' cannot be an empty array");
        }
        
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testConstructionWithNullInArray() {
        
        try {
            final AssertionRule[] rules = new AssertionRule[] { null }; 
            new AssertionRules(rules);
            fail();
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("Argument 'rules' cannot contain null elements");
        }
        
    }
    
}
// CHECKSTYLE:ON
