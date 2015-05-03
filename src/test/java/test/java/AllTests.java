/*******************************************************************************
 * This file is part of the Corporate Smart Content Project at Freie Universitaet Berlin, Corporate Semantic Web Group.
 * 
 * This work has been partially supported by the "InnoProfile-Corporate Semantic Web" project funded by the German Federal Ministry of Education and Research (BMBF) and the BMBF Innovation Initiative for the New German Laender - Entrepreneurial Regions.
 * 
 * http://www.corporate-smart-content.de/
 * 
 * Freie Universitaet Berlin
 * Copyright (c) 2013-2016
 * 
 * Institut fuer Informatik
 * Working Group Corporate Semantic Web
 * Koenigin-Luise-Strasse 24-26
 * 14195 Berlin
 * 
 * http://www.mi.fu-berlin.de/en/inf/groups/ag-csw/
 ******************************************************************************/
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
