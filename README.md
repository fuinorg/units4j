units4j
=======

A home for additional useful unit testing modules for Java. 

[![Build Status](https://fuin-org.ci.cloudbees.com/job/units4j/badge/icon)](https://fuin-org.ci.cloudbees.com/job/units4j/)
[![Coverage Status](https://coveralls.io/repos/fuinorg/units4j/badge.svg?branch=master)](https://coveralls.io/r/fuinorg/units4j?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin/units4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin/units4j/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 1.8](https://img.shields.io/badge/JDK-1.8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

<a href="https://fuin-org.ci.cloudbees.com/job/units4j"><img src="http://www.fuin.org/images/Button-Built-on-CB-1.png" width="213" height="72" border="0" alt="Built on CloudBees"/></a>

* * *

##Features

- [Asserting test coverage](###Asserting test coverage)
- [Asserting package dependencies](###Asserting package dependencies)
- [Asserting methods are not used](###Asserting methods are **not** used)

###Asserting test coverage

A good approach is to have at least one test class for every production class.

If you have classes this rule-of-thumb does not apply, you can:
- Write a dummy test class with a comment that describes why not...
- Use an AssertCoverage.ClassFilter to exclude the classes
```Java
@Test
public final void testCoverage() {
    AssertCoverage.assertEveryClassHasATest(new File("src/main/java"));
}
```

###Asserting package dependencies
It's a good practice enforcing package dependencies to avoid high coupling and package cycles.
 
You simply define a dependency description in your "src/test/resources" folder. For an example see [units4j.xml](https://github.com/fuinorg/units4j/blob/master/src/test/resources/units4j.xml)).
```Java
@Test
public final void testAssertDependencies() {
    AssertDependencies.assertRules(getClass(), "/units4j.xml", new File("target/classes"));
}
```

###Asserting methods are **not** used
Example: Prevent a [java.lang.ArithmeticException](http://docs.oracle.com/javase/8/docs/api/java/lang/ArithmeticException.html) Non-terminating decimal expansion; no exact representable decimal result." caused by calling BigDecimal's divide or setScale without a rounding mode:
```Java
// Path to '*.class' files
final File classesDir = new File("target/classes");

// Can be used to exclude some files/packages
final FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(File file) {
        return !file.getPath().contains("my/pkg/to/exclude");
    }
};

// Define methods to find
final MCAMethod divide = new MCAMethod("java.math.BigDecimal",
        "java.math.BigDecimal divide(java.math.BigDecimal)");

final MCAMethod setScale = new MCAMethod("java.math.BigDecimal",
        "java.math.BigDecimal setScale(int)");

// Fails if any class calls one of the two methods
AssertUsage.assertMethodsNotUsed(classesDir, fileFilter, divide, setScale);
```

* * *

###Snapshots

Snapshots can be found on the [OSS Sonatype Snapshots Repository](http://oss.sonatype.org/content/repositories/snapshots/org/fuin "Snapshot Repository"). 

Add the following to your .m2/settings.xml to enable snapshots in your Maven build:

```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
 
