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

import java.util.Collections;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;

public class ContainmentMethodsTest extends BaseTest {
	
	  OWLOntology onto = null;

	  @Test
	  @OWLAspectAnd({"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#ProvenanceAspect1239"})
	  public void testContainsMethodMarkedWithAnd() throws OWLOntologyCreationException {
			OWLOntologyManager om = create();
			onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			String base = "http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings";

			OWLNamedIndividual madonna = df.getOWLNamedIndividual(IRI.create(base + "#SistineMadonnaObj"));
			OWLNamedIndividual germany = df.getOWLNamedIndividual(IRI.create(base + "#Germany"));
			OWLObjectProperty hasLocation = df.getOWLObjectProperty(IRI.create(base + "#hasLocationCountry"));
			OWLAxiom axiom = df.getOWLObjectPropertyAssertionAxiom(hasLocation, madonna, germany);
			
			testContainmentAndTrue(axiom);
			testContainmentAndFalse(axiom);
			testContainmentOrTrue(axiom);
			testContainmentOrFalse(axiom);
						
			// this kind of requests is not supported (yet)
//			OWLAxiomSearchFilter filter = null;
//			Object key = null;
//			boolean cont = onto.contains(filter, key, Imports.EXCLUDED);
//			System.out.println(cont); 
			
	  }
	  
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Wikipedia"
	  })
	  public void testContainmentAndTrue(OWLAxiom axiom) {
		  
			boolean contains1 = onto.containsAxiom(axiom);
			boolean contains2 = onto.containsAxiom(axiom, false);
			boolean contains3 = onto.containsAxiomIgnoreAnnotations(axiom, false);

			
			assertEquals(contains1, true);
			assertEquals(contains2, true);
			assertEquals(contains3, true);

	  }
	  
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OwlELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	  })
	  public void testContainmentAndFalse(OWLAxiom axiom) {
		  
			boolean contains1 = onto.containsAxiom(axiom);
			boolean contains2 = onto.containsAxiom(axiom, false);
			boolean contains3 = onto.containsAxiomIgnoreAnnotations(axiom, false);
			
			assertEquals(contains1, false);
			assertEquals(contains2, false);
			assertEquals(contains3, false);


	  }
	  
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OwlELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	  })
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Wikipedia"
	  })
	  public void testContainmentOrTrue(OWLAxiom axiom) {
		
			boolean contains1 = onto.containsAxiom(axiom);
			boolean contains2 = onto.containsAxiom(axiom, false);
			boolean contains3 = onto.containsAxiomIgnoreAnnotations(axiom, false);

            assertEquals(contains1, true);
			assertEquals(contains2, true);
			assertEquals(contains3, true);

	  }
	  
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OwlELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	  })
	  @OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#DieWelt"
	  })	  public void testContainmentOrFalse(OWLAxiom axiom) {
		
			boolean contains1 = onto.containsAxiom(axiom);
			boolean contains2 = onto.containsAxiom(axiom, false);
			boolean contains3 = onto.containsAxiomIgnoreAnnotations(axiom, false);

			assertEquals(contains1, false);
			assertEquals(contains2, false);
			assertEquals(contains3, false);
	  }
	  
}
