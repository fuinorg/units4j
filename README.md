# units4j
A home for additional useful unit testing modules for Java. 

[![Java Maven Build](https://github.com/fuinorg/units4j/actions/workflows/maven.yml/badge.svg)](https://github.com/fuinorg/units4j/actions/workflows/maven.yml)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=org.fuin%3Aunits4j&metric=coverage)](https://sonarcloud.io/dashboard?id=org.fuin%3Aunits4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin/units4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin/units4j/)
[![Javadocs](https://www.javadoc.io/badge/org.fuin/units4j.svg)](https://www.javadoc.io/doc/org.fuin/units4j)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 17](https://img.shields.io/badge/JDK-17-green.svg)](https://openjdk.java.net/projects/jdk/17/)

## Versions
- 0.11.0 (or later) = **Java 17** with **junit5** / **WeldJUnit4Runner removed** in favor of [weld-junit5](https://github.com/weld/weld-testing)
- 0.10.0 = **Java 11** with new **jakarta** namespace
- 0.9.x = **Java 11** before namespace change from 'javax' to 'jakarta'
- 0.8.4 (or previous) = **Java 8**



* * *

## Features

- [Asserting test coverage](#asserting-test-coverage)
- [Asserting package dependencies](#asserting-package-dependencies)
- [Asserting methods are not used](#asserting-methods-are-not-used)
- [Assert that JPA entities are valid](#assert-that-jpa-entities-are-valid)
- [Assert that methods have information if null is allowed or not](#assert-that-methods-have-information-if-null-is-allowed-or-not)
- [Assert that fields with @JsonbProperty annotation are not final](#assert-that-fields-with-jsonbproperty-annotation-are-not-final)

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

It's a good style to define a [precondition](https://en.wikipedia.org/wiki/Precondition) for method arguments 
and [postconditions](https://en.wikipedia.org/wiki/Postcondition) for return values of externally used methods.
Especially the questions "Can I pass null?" or "Does the method return null values?" is a common source of confusion.

This assertion makes sure that all return values and parameters of all public, protected and package-private methods have at least one of the following annotations:
* **@NotNull** (For example from [Java Validation API](https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/NotNull.html)) or
* **@NotEmpty** (For example from [Java Validation API](https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/NotEmpty.html)) or
* **@Nullable** (For example from the [checkerframework](https://checkerframework.org/api/org/checkerframework/checker/nullness/qual/Nullable.html) annotation. 

The package and case of those annotations does actually not matter as only simple name is checked. It is also possible to pass your own list of expected annotation simple names to the `hasNullabilityInfoOnAllMethods()` method.

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

### Assert that fields with @JsonbProperty annotation are not final
Verifies that no field that has a [@JsonbProperty](https://static.javadoc.io/javax.json.bind/javax.json.bind-api/1.0/javax/json/bind/annotation/JsonbProperty.html) annotation. 
The deserialization using a Eclipse Yasson [FieldAccessStrategy](https://github.com/eclipse-ee4j/yasson/blob/master/src/main/java/org/eclipse/yasson/FieldAccessStrategy.java) will fail otherwise silently.

Uses JBoss [Jandex](https://github.com/wildfly/jandex) to validate [JSON-B](http://json-b.net/) fields.

```Java
import static org.fuin.units4j.JandexAssert.assertThat;
```
```Java
// Collect all class files
File dir = new File("target/classes");
List<File> classFiles = Units4JUtils.findAllClasses(dir);
Index index = Units4JUtils.indexAllClasses(classFiles);

// Verify that no field that has a 'javax.json.bind.annotation.JsonbProperty' annotation. 
// The deserialization using a 'org.eclipse.yasson.FieldAccessStrategy' will fail otherwise.
assertThat(index).hasNoFinalFieldsWithJsonbPropertyAnnotation();
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
 
