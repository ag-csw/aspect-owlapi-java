# aspect-owlapi
An aspect-oriented extension to the OWL API.

## Installation

### Known Problems

After importing the project into an Eclipse installation which has the m2e plugin installed, the following two error messages may appear in the Problems view:

-   Plugin execution not covered by lifecycle configuration: org.codehaus.mojo:aspectj-maven-plugin:1.7:compile (execution: default, phase: compile)

-   Plugin execution not covered by lifecycle configuration: org.codehaus.mojo:aspectj-maven-plugin:1.7:test-compile (execution: default, phase: test-compile)

In this case, follow these steps in order to make the error messages disappear:

-   right click on one of the two error messages in the Problem view and choose *Quick Fix***.**

-   In the upcoming dialog select *"Discover new m2e connectors" *and click *Finish*.

-   In the subsequent dialog, make sure the checkbox next to **AspectJ** *(AspectJ m2e configurator)* is checked and click *Finish*.

-   In the subsequent dialog, make sure the checkbox next to *"Maven Integration for AJDT (Optional)" *is checked and click *Next*.

-   Click *Next* again.

-   Select *"I accept the terms and the license agreements"* and click *Finish*.

-   Dismiss the security warning about unsigned software by clicking *OK*.

-   Confirm the request for restarting Eclipse by clicking *Yes* (if you have unsaved work, click *No* instead, save your work and then restart Eclipse manually).
