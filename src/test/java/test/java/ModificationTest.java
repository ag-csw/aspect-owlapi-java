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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.CollectionFactory;

import de.fuberlin.csw.OWLAspectAnd;

public class ModificationTest extends BaseTest {
	
	OWLOntologyManager om = create();
	OWLOntology onto = null;
	
    @Test
    public void testOntologyModification() throws OWLOntologyCreationException {
		
		onto = loadAdvisedOntologyWithAspects(om); 
		assertNotNull(onto);
		
		String base = "http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings";

		OWLNamedIndividual madonna = df.getOWLNamedIndividual(IRI.create(base + "#SistineMadonnaObj"));
		OWLNamedIndividual germany = df.getOWLNamedIndividual(IRI.create(base + "#Germany"));
		OWLObjectProperty hasLocation = df.getOWLObjectProperty(IRI.create(base + "#hasLocationCountry"));
		OWLAxiom ax = df.getOWLObjectPropertyAssertionAxiom(hasLocation, madonna, germany);

		testModificationAnd(ax);
		testModificationOr(ax);
		
    }

	@OWLAspectAnd({
    	"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#Wikipedia",
    	"http://www.corporate-semantic-web.de/ontologies/aspect/owl/temporal/example#YearsSince1955"
    })
    private void testModificationAnd(OWLAxiom ax) {
		// should be 1, since we know that
		// there is already such an axiom with such aspects in the ontology
		int count = onto.getAxiomCount(); 
		assertEquals(1, count);
		
		// The number doesn't change, since there was such an axiom with such aspects already
		om.applyChange(new AddAxiom(onto, ax));
		assertEquals(count, onto.getAxiomCount());
		
		om.applyChange(new RemoveAxiom(onto, ax));
		assertEquals(count-1, onto.getAxiomCount());
		
		om.addAxiom(onto, ax);
		assertEquals(count, onto.getAxiomCount());
		
		om.removeAxiom(onto, ax);
		assertEquals(count-1, onto.getAxiomCount());
		
		List<OWLOntologyChange> applyChanges = new ArrayList<OWLOntologyChange>();
		applyChanges.add(new AddAxiom(onto, ax));
		om.applyChanges(applyChanges);
		assertEquals(count, onto.getAxiomCount());
		
		om.addAxioms(onto, CollectionFactory.createSet(ax));
		// the number doesn't increase, because there is one such axiom inside already
		assertEquals(count, onto.getAxiomCount()); 
		
		om.removeAxioms(onto, CollectionFactory.createSet(ax));
		assertEquals(count-1, onto.getAxiomCount());
	}

	@OWLAspectAnd({
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example#ProvenanceAspect12345"
	})
	@OWLAspectAnd({
		"http://www.corporate-semantic-web.de/ontologies/aspect/owl/temporal/example#TemporalAspect12345"
	})
	private void testModificationOr(OWLAxiom ax) {	
		// should be 0, since we know that
		// there are no axioms with such aspects yet
		int count = onto.getAxiomCount(); 
		assertEquals(0, count);
		
		om.applyChange(new AddAxiom(onto, ax));
		assertEquals(count+1, onto.getAxiomCount());
		
		om.applyChange(new RemoveAxiom(onto, ax));
		assertEquals(count, onto.getAxiomCount());
		
		om.addAxiom(onto, ax);
		assertEquals(count+1, onto.getAxiomCount());
		
		om.removeAxiom(onto, ax);
		assertEquals(count, onto.getAxiomCount());
		
		List<OWLOntologyChange> applyChanges = new ArrayList<OWLOntologyChange>();
		applyChanges.add(new AddAxiom(onto, ax));
		om.applyChanges(applyChanges);
		assertEquals(count+1, onto.getAxiomCount());
		
		om.addAxioms(onto, CollectionFactory.createSet(ax));
		// the number doesn't increase, because there is one such axiom inside already
		assertEquals(count+1, onto.getAxiomCount()); 
		
		om.removeAxioms(onto, CollectionFactory.createSet(ax));
		assertEquals(count, onto.getAxiomCount());
	}
}
