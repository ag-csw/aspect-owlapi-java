package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;

import de.fuberlin.csw.OWLAspectAnd;

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
			
			om.removeOntology(onto);
	 }
	 

	@OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity", 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#BerlinerZeitung"
	})
	private void testAxiomExtractionAnd() {
		
		Set<OWLAxiom> set = onto.getABoxAxioms(Imports.EXCLUDED);
		assertEquals(2, set.size());
		
		OWLEntity entity = df.getOWLNamedIndividual(IRI.create(base + "#FelsenschlossObj"));
		Collection<OWLAxiom> coll = EntitySearcher.getReferencingAxioms(entity, onto);
		assertEquals(2, coll.size());	
	}
	
	
	@OWLAspectAnd({ 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#BerlinerZeitung"
	})
	@OWLAspectAnd({ 
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Spiegel"
	})
	private void testAxiomExtractionOr() {
		
		Set<OWLAxiom> set = onto.getABoxAxioms(Imports.EXCLUDED);
		assertEquals(4, set.size());
		
		OWLEntity entity = df.getOWLNamedIndividual(IRI.create(base + "#FelsenschlossObj"));
		Collection<OWLAxiom> coll = EntitySearcher.getReferencingAxioms(entity, onto);
		assertEquals(2, coll.size());		
	}


	
}
