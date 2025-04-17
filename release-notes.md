# Release Notes

## 0.12.0

### General
- Dependency updates
- Changed from JUnit 4 to 5 (Jupiter)
  - [AssertCoverage](src/main/java/org/fuin/units4j/AssertCoverage.java) is now deprecated in favour of [Units4JConditions](src/main/java/org/fuin/units4j/archunit/Units4JConditions.java)
  - [AssertDependencies](src/main/java/org/fuin/units4j/AssertDependencies.java) is now deprecated in favour of standard ArchUnit rules like this:
    ```java
       @ArchTest
       static final ArchRule core_access_only_to_defined_packages =
           classes()
               .that()
                   .resideInAPackage("my.package.core..")
               .should()
                   .onlyDependOnClassesThat()
                   .resideInAnyPackage("my.package.common..", "java.lang..");
    ```
  - [MultipleCommands](src/main/java/org/fuin/units4j/MultipleCommands.java) was moved to [utils4j](https://github.com/fuinorg/utils4j/) and is now deprecated
  - [TestCommand](src/main/java/org/fuin/units4j/TestCommand.java) was moved to [utils4j](https://github.com/fuinorg/utils4j/) and is now deprecated
  - [TestOmitted](src/main/java/org/fuin/units4j/TestOmitted.java) was moved to [utils4j](https://github.com/fuinorg/utils4j/) and is now deprecated
  - **Units4JUtils.setPrivateField** was moved to [utils4j](https://github.com/fuinorg/utils4j/) and is now deprecated
  - **Units4JUtils.assertCauseXxx**/**isExpectedException** methods are deprecated in favour of [AssertJ](https://joel-costigliola.github.io/assertj/) **assertThatThrownBy** method
    ```java
       assertThatThrownBy(() -> { throw new Exception("boom!"); })
          .isInstanceOf(Exception.class)
          .hasRootCauseMessage("boom");
    ```
  - **Units4JUtils.replaceXmlAttr** is now deprecated in favour of Use [XMLUnit](https://www.xmlunit.org/) with ignoring field while comparing result.

### Analyzer
- Updated to ASM 9

### ArchUnit
- Added condition [AllTopLevelClassesHaveATestCondition](src/main/java/org/fuin/units4j/archunit/AllTopLevelClassesHaveATestCondition.java)
- Added rule [Units4JConditions](src/main/java/org/fuin/units4j/archunit/Units4JConditions.java)
