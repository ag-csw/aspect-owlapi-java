package test.java;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	ClassMarkedWithOWLAspectAndTest.class,
	ClassMarkedWithOWLAspectOrTest.class,
	ContainmentMethodsTest.class,
	AxiomCountTest.class,
	OWL2ProfilesTest.class,
	ModificationTest.class, 
	ExtractAxiomsTest.class, 
	ExtractEntitiesTest.class,
	ExtractAnonIndTest.class,
	ExtractMultimapsTest.class
})
public class AllTests {

}
