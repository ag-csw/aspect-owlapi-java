# aspect-owlapi-java

An aspect-oriented extension to the OWL API.

The aspect-owlapi-java allows programmers to access ontology aspects from Java
code, using tags.

## Usage


Ontology aspects describe ontology modules, which may or may not be activated,
depending on a variety of conditions. Using this API, a programmer may specify
these conditions using the Java tags `OWLAspect`, `OWLAspectAnd`, and
`OWLAspectOr`.

Example:

```java
@OWLAspect("http://www.fu-berlin.de/csw/ontologies/aood/ontologies/aspect123")
public void doSomething () {
 Set<OWLAxiom> allAxioms = myOntology.getAxioms();
 ...}
```

This would filter the axioms returned by `myOntology.getAxioms();`, such that
the resulting set only contains axioms that are either aspect-free or have the
aspect *aspect123*.

The tags `OWLAspectAnd` and `OWLAspectOr` allow the combination of aspects. They
take sets of aspect IRIs as arguments. Their respective semantics are
self-explanatory: `OWLAspectAnd` builds the conjunction while `OWLAspectOr`
builds the disjunction of the filtered sets of axioms.

Example:    

```java
@OWLAspectOr({
 @OWLAspectAnd({"http://...#Aspect1",
  "http://...#Aspect2"}),
 @OWLAspectAnd({"http://...#Aspect2",
  "http://...#Aspect3"})
})
```

##Setup

### Prerequisites

-   Java 8

-   Maven 3.x

### Installation

Clone the repository to your local hard disk.

### Running the tests

-   Open a terminal/console/command prompt window and `cd` to the project's root
    directory.

-   Type `mvn test` and press *Enter*.

### Known Problems

After importing the project into an Eclipse installation which has the m2e
plugin installed, the following two error messages may appear in the Problems
view:

-   Plugin execution not covered by lifecycle configuration:
    org.codehaus.mojo:aspectj-maven-plugin:1.7:compile (execution: default,
    phase: compile)

-   Plugin execution not covered by lifecycle configuration:
    org.codehaus.mojo:aspectj-maven-plugin:1.7:test-compile (execution: default,
    phase: test-compile)

In this case, follow these steps in order to make the error messages disappear:

-   right click on one of the two error messages in the Problem view and choose
    *Quick Fix*.

-   In the upcoming dialog select *"Discover new m2e connectors"* and click
    *Finish*.

-   In the subsequent dialog, make sure the checkbox next to **AspectJ**
    *(AspectJ m2e configurator)* is checked and click *Finish*.

-   In the subsequent dialog, make sure the checkbox next to *"Maven Integration
    for AJDT (Optional)"* is checked and click *Next*.

-   Click *Next* again.

-   Select *"I accept the terms and the license agreements"* and click *Finish*.

-   Dismiss the security warning about unsigned software by clicking *OK*.

-   Confirm the request for restarting Eclipse by clicking *Yes* (if you have
    unsaved work, click *No* instead, save your work and then restart Eclipse
    manually).
