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

import java.util.Collection;
import java.util.Set;

import de.fuberlin.csw.aood.owlapi.OWLAspectSparql;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;

public class ExtractAxiomsTest extends BaseTest {

	 OWLOntology onto = null;
	 String base = "http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings";
 
	 @Test
	 public void testAxiomExtractionShouldReturnAxiomsWithAspects() throws OWLOntologyCreationException {
		 
			OWLOntologyManager om = create();
			onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			testAxiomExtractionAnd();
			testAxiomExtractionOr();	 
//			testAxiomExtractionSparql();

			om.removeOntology(onto);
	 }
	 

	@OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#BerlinerZeitung"
	})
	private void testAxiomExtractionAnd() {

		
		OWLEntity entity = df.getOWLNamedIndividual(IRI.create(base + "#FelsenschlossObj"));
		Collection<OWLAxiom> coll = entity.getReferencingAxioms(onto);
		assertEquals(2, coll.size());	
	}
	
	
	@OWLAspectAnd({ 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#BerlinerZeitung"
	})
	@OWLAspectAnd({ 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	})
	private void testAxiomExtractionOr() {

		
		OWLEntity entity = df.getOWLNamedIndividual(IRI.create(base + "#FelsenschlossObj"));
		Collection<OWLAxiom> coll = entity.getReferencingAxioms(onto);
		assertEquals(2, coll.size());		
	}




	@OWLAspectSparql({
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
					"\n" +
					"CONSTRUCT {?s rdfs:subClassOf Agent}\n" +
					"WHERE {\n" +
					"	?s rdfs:subClassOf Agent .\n" +
					"}"
	})
	private void testAxiomExtractionSparql() {

		Collection<OWLAxiom> coll = onto.getAxioms();

		//assertEquals(2, coll.size());

	}


	
}
