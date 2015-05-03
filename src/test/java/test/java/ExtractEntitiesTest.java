package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fuberlin.csw.OWLAspectAnd;

public class ExtractEntitiesTest extends BaseTest {
	
	 OWLOntology onto = null;
	 String base = "http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings";

	 @Test
	 public void testAnonIndExtractionShouldReturnAnonIndsWithAspects() throws OWLOntologyCreationException {
		 
			OWLOntologyManager om = create();
			onto = loadAdvisedOntologyWithAspects(om); 
			assertNotNull(onto);
			
			testEntityExtractionAnd();
			testEntityExtractionOr();	 
			
			// Including imports does not function yet...
			
			om.removeOntology(onto);
	 }

	@OWLAspectAnd({"someInexistentAspect"})
	private void testEntityExtractionAnd() {
		Set<OWLEntity> allEntities = onto.getSignature();
		assertEquals(0, allEntities.size());
	}
	
	@OWLAspectAnd({"someInexistentAspect"})
	@OWLAspectAnd({
		  "http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity" 
	})
	private void testEntityExtractionOr() {
		Set<OWLEntity> allEntities = onto.getSignature();
		// in our example all 20 entities have declaration axioms marked with EL profile
		assertEquals(20, allEntities.size());	
	}

}
