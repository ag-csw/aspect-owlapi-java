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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fuberlin.csw.OWLAspectAnd;

/**
 * this class only tests that the class annotations of type OWLAspectOr 
 * are being accessed and handled correctly. 
 * so we just test only an example with simple functions getAxioms() and getAxiomCount()
 * We test different kinds of functionality using annotations on methods in other tests
 */
@OWLAspectAnd({
	"http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
	"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#DieWelt"
})
@OWLAspectAnd({
	"http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
	"http://www.corporate-semantic-web.de/ontologies/aspect/owl/temporal/example#YearsSince1955"
})
public class ClassMarkedWithOWLAspectOrTest extends BaseTest {

	@Test
	public void testAnnotationsOr() throws OWLException {
		OWLOntologyManager om = create();
		OWLOntology onto = loadAdvisedOntologyWithAspects(om);
		assertNotNull(onto);
		
		int axsSetSize = onto.getAxioms().size();
		int axCount = onto.getAxiomCount();
		int realSize = 67;

		assertEquals(axCount, axsSetSize);
		
		assertTrue((realSize > axsSetSize) && (realSize > axCount));
		
		// we know what the results in our concrete ontology should be
		assertEquals(axCount, 4);
		assertEquals(axsSetSize, 4);
		
	}

}
