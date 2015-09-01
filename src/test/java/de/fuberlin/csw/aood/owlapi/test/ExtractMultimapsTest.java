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
package de.fuberlin.csw.aood.owlapi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;

public class ExtractMultimapsTest extends BaseTest {
	
	 OWLOntology onto = null;
	 String base = "http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings";

	 @Test
	 public void testAnonIndExtractionShouldReturnAnonIndsWithAspects() throws OWLOntologyCreationException {
		 
			OWLOntologyManager om = create();
			onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			testMultimapExtractionAnd();
			testMultimapExtractionOr();	 
			
			om.removeOntology(onto);
	 }

	@OWLAspectAnd({ 
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	})
	private void testMultimapExtractionAnd() {
		OWLIndividual ind = df.getOWLNamedIndividual(IRI.create(base + "#FrauenbadTitle"));
		Map<OWLDataPropertyExpression, Set<OWLLiteral>> map =
                ind.getDataPropertyValues(onto);
		// and without aspects this multimap has size 1 ( titleString, Frauenbad )
		assertEquals(0, map.size());
		
	}

	@OWLAspectAnd({ 
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity",
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#NYTimes"
	})
	@OWLAspectAnd({ 
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity",
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	})
	private void testMultimapExtractionOr() {
		OWLIndividual ind = df.getOWLNamedIndividual(IRI.create(base + "#FrauenbadObj"));
		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> map =
                ind.getObjectPropertyValues(onto);
		// and without aspects this multimap has size 5
		assertEquals(3, map.size());
	}

}
