// CHECKSTYLE:OFF Test class
package org.fuin.units4j.analyzer;

import java.math.BigDecimal;

public class FindMethodCallExampleClasz {

    private BigDecimal bd = new BigDecimal(1);

    static {
        BigDecimal.ONE.setScale(2);
    }

    public FindMethodCallExampleClasz() {
        super();
        bd.divide(new BigDecimal(3));
    }

    public void a() {
        bd.setScale(2);
    }

    public boolean b(final Integer a, final Boolean b, final String c) {
        bd.setScale(2);
        return true;
    }

    public static class InnerExampleClasz extends FindMethodCallExampleClasz {

        public void a() {
            BigDecimal.ONE.setScale(2);
        }

    }

}
// CHECKSTYLE:ON
