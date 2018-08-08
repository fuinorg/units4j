units4j
=======

A home for additional useful unit testing modules for Java. 

[![Build Status](https://jenkins.fuin.org/job/units4j/badge/icon)](https://jenkins.fuin.org/job/units4j/)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=org.fuin%3Aunits4j&metric=coverage)](https://sonarcloud.io/dashboard?id=org.fuin%3Aunits4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin/units4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin/units4j/)
[![Javadocs](https://www.javadoc.io/badge/org.fuin/units4j.svg)](https://www.javadoc.io/doc/org.fuin/units4j)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 1.8](https://img.shields.io/badge/JDK-1.8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

* * *

## Features

- [Asserting test coverage](#asserting-test-coverage)
- [Asserting package dependencies](#asserting-package-dependencies)
- [Asserting methods are not used](#asserting-methods-are-not-used)
- [Assert that JPA entities are valid](#assert-that-jpa-entities-are-valid)
- [Assert that methods have information if null is allowed or not](#assert-that-methods-have-information-if-null-is-allowed-or-not)

* * *

### Asserting test coverage

A good approach is to have at least one test class for every production class.

If you have classes this rule-of-thumb does not apply, you can:
- Write a dummy test class with a comment that describes why not...
- Use an AssertCoverage.ClassFilter to exclude the classes
```Java
@Test
public void testCoverage() {
    AssertCoverage.assertEveryClassHasATest(new File("src/main/java"));
}
```

### Asserting package dependencies
It's a good practice enforcing package dependencies to avoid high coupling and package cycles.
 
You simply define a dependency description in your "src/test/resources" folder. For an example see [units4j.xml](https://github.com/fuinorg/units4j/blob/master/src/test/resources/units4j.xml)).
```Java
@Test
public void testAssertDependencies() {
    AssertDependencies.assertRules(getClass(), "/units4j.xml", new File("target/classes"));
}
```

### Asserting methods are **not** used
Example: Prevent a [java.lang.ArithmeticException](http://docs.oracle.com/javase/8/docs/api/java/lang/ArithmeticException.html) Non-terminating decimal expansion; no exact representable decimal result." caused by calling BigDecimal's divide or setScale without a rounding mode:
```Java
// Path to '*.class' files
File classesDir = new File("target/classes");

// Can be used to exclude some files/packages
FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(File file) {
        return !file.getPath().contains("my/pkg/to/exclude");
    }
};

// Define methods to find
MCAMethod divide = new MCAMethod("java.math.BigDecimal", "java.math.BigDecimal divide(java.math.BigDecimal)");
MCAMethod setScale = new MCAMethod("java.math.BigDecimal","java.math.BigDecimal setScale(int)");

// Fails if any class calls one of the two methods
AssertUsage.assertMethodsNotUsed(classesDir, fileFilter, divide, setScale);
```

### Assert that JPA entities are valid
Uses JBoss [Jandex](https://github.com/wildfly/jandex) to validate JPA entity classes.

```Java
import static org.fuin.units4j.JandexAssert.assertThat;
```
```Java
// Collect all class files
File dir = new File("target/classes");
List<File> classFiles = Units4JUtils.findAllClasses(dir);
Index index = Units4JUtils.indexAllClasses(classFiles);

// Verify that all classes annotated with @Entity or @MappedSuperclass observe 
// the rules for JPA entities (Class not final + No final methods + ...).
assertThat(index).hasOnlyValidJpaEntities();
```

### Assert that methods have information if null is allowed or not

**WORK IN PROGRESS** See [Issue #9](https://github.com/fuinorg/units4j/issues/9)

It's a good style to define a [precondition](https://en.wikipedia.org/wiki/Precondition) for method arguments 
and [postconditions](https://en.wikipedia.org/wiki/Postcondition) for return values of externally used methods.
Especially the questions "Can I pass null?" or "Does the method return null values?" is a common source of confusion.
This assertion makes sure that all return values and parameters of all public, protected and package-private methods have either
a [@NotNull](https://docs.oracle.com/javaee/6/api/javax/validation/constraints/NotNull.html) or
a [@Nullable](https://github.com/fuinorg/objects4j/blob/master/src/main/java/org/fuin/objects4j/common/Nullable.java) annotation.

Example:
```Java
public interface MyInterface {
    
    // Post condition says the return value is never null
    @NotNull
    public Boolean myMethodA();
    
    // Pre condition says the first value cannot be null, but it's OK for the second argument
    public void myMethodB(@NotNull Integer abc, @Nullable String def);

    // Post condition says the return value may be null
    @Nullable
    public Long myMethodC();
    
}   
```

Test:
```Java
import static org.fuin.units4j.JandexAssert.assertThat;
```
```Java
// Collect all class files
File dir = new File("target/classes");
List<File> classFiles = Units4JUtils.findAllClasses(dir);
Index index = Units4JUtils.indexAllClasses(classFiles);

// Verify that all methods make a statement if null is allowed or not 
assertThat(index).hasNullabilityInfoOnAllMethods();
```


* * *


### Snapshots

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
 
