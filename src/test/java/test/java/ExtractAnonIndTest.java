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

import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fuberlin.csw.OWLAspectAnd;

public class ExtractAnonIndTest extends BaseTest {
	 OWLOntologyManager om = create();
	 OWLOntology onto = null;
	 String base = "http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings";

	 @Test
	 public void testAnonIndExtractionShouldReturnAnonIndsWithAspects() throws OWLOntologyCreationException {
			onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			OWLNamedIndividual namedInd = df.getOWLNamedIndividual(IRI.create(base + "#SistineMadonnaObj"));
			// we invent some object property and anon. ind. to test
			// since we have no anonymous individuals in our ontology
			OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(base + "someInexistentObjProperty"));
			OWLAnonymousIndividual anonInd = df.getOWLAnonymousIndividual("anInd1");
			OWLAxiom axiomUsingAnonInd = df.getOWLObjectPropertyAssertionAxiom(objProp, namedInd, anonInd);
			// we add this axiom without any aspects
			om.addAxiom(onto, axiomUsingAnonInd);
			
			testAnonIndExtractionAnd();
			testAnonIndExtractionOr(axiomUsingAnonInd);	 
			
			om.removeOntology(onto);
	 }

	@OWLAspectAnd({"someAspect1"})
	private void testAnonIndExtractionAnd() {
		// we have added one anonymous individual above, 
		// but it didn't have any aspects
		// so this function here shall return set of zero anon. inds.	
		Set<OWLAnonymousIndividual> anInds = onto.getAnonymousIndividuals();
		assertEquals(0, anInds.size());	
	}

	@OWLAspectAnd({"someAspect1"})
	@OWLAspectAnd({"someAspect2"})
	private void testAnonIndExtractionOr(OWLAxiom axiomUsingAnonInd) {
		// no anon. inds with aspects yet
		assertEquals(0, onto.getAnonymousIndividuals().size());	
		// add this axiom again under current aspects
		om.addAxiom(onto, axiomUsingAnonInd);
		// now the set shall not be empty anymore
		assertEquals(1, onto.getAnonymousIndividuals().size());	
	}

}
