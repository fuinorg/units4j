// CHECKSTYLE:OFF
package dummy.test.bad;

import dummy.bad.a.BadA;
import dummy.test.Parent;
import org.fuin.utils4j.Utils4J;

public class BadOne {

    public static void main(final String[] args) {

        // This package is not allowed
        new BadA().doNothing();

        // This package also not
        Utils4J.encodeHex(new byte[] { 1, 2, 3 });

        // We shouldn't use something in the "parent package"
        new Parent().doNothing();

    }

}
// CHECKSTYLE:ON
