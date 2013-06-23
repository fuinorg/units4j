// CHECKSTYLE:OFF
package dummy.test.good;

import org.fuin.utils4j.Utils4J;

import dummy.good.a.GoodA;

public final class GoodOne {

    public static void main(final String[] args) {
        new GoodA().doNothing();
        Utils4J.encodeHex(new byte[] { 1, 2, 3 });
    }

}
// CHECKSTYLE:ON
