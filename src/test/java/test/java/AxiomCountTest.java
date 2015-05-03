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

import org.junit.Test;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;

import de.fuberlin.csw.OWLAspectAnd;

public class AxiomCountTest extends BaseTest {
		
	  @Test
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Wikipedia"
	  })
	  public void testAxiomCountMethodMarkedWithAnd() throws OWLOntologyCreationException {
			OWLOntologyManager om = create();
			OWLOntology onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			// we test all possible count methods which are not deprecated.
			// Deprecated is e.g. onto.getAxiomCount(boolean imports); Its result is false in our program.
			int count1 = onto.getAxiomCount();
			int count2 = onto.getAxiomCount(Imports.INCLUDED);
			int count3 = onto.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION);
			int count4 = onto.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION, Imports.EXCLUDED);

			// We can check correctness this way 
			// since we have already tested the correctness of getAxioms() with aspects
			int count1Correct = onto.getAxioms().size();
			int count2Correct = onto.getAxioms(Imports.INCLUDED).size();
			int count3Correct = onto.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION).size();
			int count4Correct = onto.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, Imports.EXCLUDED).size();

			assertEquals(count1, count1Correct);
			assertEquals(count2, count2Correct);
			assertEquals(count3, count3Correct);
			assertEquals(count4, count4Correct);	

	}
	  
	  @Test
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OwlELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	  })
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/temporal/example#YearsSince2004"
	  })
	  public void testAxiomCountOr() throws OWLOntologyCreationException {	
			OWLOntologyManager om = create();
			OWLOntology onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			// we test all possible count methods which are not deprecated.
			// Deprecated is e.g. onto.getAxiomCount(boolean imports); Its result is false in our program.
			int count1 = onto.getAxiomCount();
			int count2 = onto.getAxiomCount(Imports.INCLUDED);
			int count3 = onto.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION);
			int count4 = onto.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION, Imports.EXCLUDED);

			// We can check correctness this way 
			// since we have already tested the correctness of getAxioms() with aspects
			int count1Correct = onto.getAxioms().size();
			int count2Correct = onto.getAxioms(Imports.INCLUDED).size();
			int count3Correct = onto.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION).size();
			int count4Correct = onto.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, Imports.EXCLUDED).size();

			assertEquals(count1, count1Correct);
			assertEquals(count2, count2Correct);
			assertEquals(count3, count3Correct);
			assertEquals(count4, count4Correct);
	  }
	  
}
