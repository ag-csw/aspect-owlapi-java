package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;

import com.google.common.collect.Multimap;

import de.fuberlin.csw.OWLAspectAnd;

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
		Multimap<OWLDataPropertyExpression, OWLLiteral> multimap = 
				EntitySearcher.getDataPropertyValues(ind, Collections.singleton(onto));
		// and without aspects this multimap has size 1 ( titleString, Frauenbad )
		assertEquals(0, multimap.size());
		
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
		Multimap<OWLObjectPropertyExpression, OWLIndividual> multimap = 
				EntitySearcher.getObjectPropertyValues(ind, onto);
		// and without aspects this multimap has size 5
		assertEquals(3, multimap.size());	
	}

}
