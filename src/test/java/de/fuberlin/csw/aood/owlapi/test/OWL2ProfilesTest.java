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

import java.util.HashSet;
import java.util.Set;

import de.fuberlin.csw.aood.owlapi.OWLAspectSparql;
import de.fuberlin.csw.aood.owlapi.util.ModelConverter;
import de.fuberlin.csw.aood.owlapi.util.QueryExecutor;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWL2RLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;

public class OWL2ProfilesTest extends BaseTest {
	
	OWLOntologyManager om = create();
	OWLOntology onto = null;
	
	@Test
	public void testOWL2Profiles() {



		try {
			// this is original painting ontology
			// without any annotations yet
			onto = om.loadOntology(paintingIRI); 
			assertNotNull(onto);

            //doTest();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}








	private void doTest() {


		// Available profiles: DL, EL, QL, RL, OWL2 (Full) 
		OWL2ELProfile el = new OWL2ELProfile(); 
		OWL2RLProfile rl = new OWL2RLProfile();
		OWL2QLProfile ql = new OWL2QLProfile();
		
		Set<OWLAxiom> notInEL = new HashSet<>();
		Set<OWLAxiom> inEL = new HashSet<>();

		Set<OWLAxiom> notInRL = new HashSet<>();
		Set<OWLAxiom> inRL = new HashSet<>();
		
		Set<OWLAxiom> notInQL = new HashSet<>();
		Set<OWLAxiom> inQL = new HashSet<>();
		
		
//		System.out.println("alle anfangs: "+onto.getAxiomCount());

		
//		System.out.println(rl.checkOntology(onto).getViolations().size());
		for (OWLProfileViolation v : rl.checkOntology(onto).getViolations()) {
//			System.out.println(v);
			notInRL.add(v.getAxiom());
		}
		for (OWLAxiom ax : onto.getAxioms()) {
			if (!notInRL.contains(ax)) {
				inRL.add(ax);
			}
		}
		
//		System.out.println(ql.checkOntology(onto).getViolations().size());
		for (OWLProfileViolation v : ql.checkOntology(onto).getViolations()) {
			notInQL.add(v.getAxiom());
		}
		for (OWLAxiom ax : onto.getAxioms()) {
			if (!notInQL.contains(ax)) {
				inQL.add(ax);
			}
		}

		
//		System.out.println(el.checkOntology(onto).getViolations().size());
		for (OWLProfileViolation v : el.checkOntology(onto).getViolations()) {
//			System.out.println(v);
			notInEL.add(v.getAxiom());
		}
		for (OWLAxiom ax : onto.getAxioms()) {
			if (!notInEL.contains(ax)) {
				inEL.add(ax);
			}
		}
	
		assoRL(inRL);
		assoQL(inQL);
		assoEL(inEL);
		
		// assert that there are no profile violations in the respectively annotated parts
		try {
			OWLOntology ontoEL = om.createOntology();
			om.addAxioms(ontoEL, gettoEL());
			assertEquals(0, el.checkOntology(ontoEL).getViolations().size());
			
			OWLOntology ontoRL = om.createOntology();
			om.addAxioms(ontoRL, gettoRL());
			assertEquals(0, rl.checkOntology(ontoRL).getViolations().size());
			
			OWLOntology ontoQL = om.createOntology();
			om.addAxioms(ontoQL, gettoQL());
			assertEquals(0, ql.checkOntology(ontoQL).getViolations().size());
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		// We know how many there should be !
		
		assertEquals(onto.getAxiomCount(), 1378);
		assertEquals(gettoQL().size(), 1316);
		assertEquals(gettoRL().size(), 1363);
		assertEquals(gettoEL().size(), 1372); // there were 8 violations but only from 6 violating axs
	
//		System.out.println("alle: "+onto.getAxiomCount());
//
//		System.out.println("ql: "+gettoQL().size());
//		System.out.println("rl: "+gettoRL().size());
//		System.out.println("el: "+gettoEL().size());
			
//		System.out.println(onto.getAxiomCount());
//		System.out.println("viol el "+el.checkOntology(onto).getViolations().size());
//		System.out.println("viol rl "+rl.checkOntology(onto).getViolations().size());
//		System.out.println("viol ql "+ql.checkOntology(onto).getViolations().size());
		
	}

	@OWLAspectAnd("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLRLComplexity")
	private void assoRL(Set<OWLAxiom> axs) {
		om.addAxioms(onto, axs);
	}
	
	@OWLAspectAnd("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLRLComplexity")
	private Set<OWLAxiom> gettoRL() {
		return onto.getAxioms();
	}
	
	@OWLAspectAnd("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity")
	private void assoEL(Set<OWLAxiom> axs) {
		om.addAxioms(onto, axs);
	}
	
	@OWLAspectAnd("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLELComplexity")
	private Set<OWLAxiom> gettoEL() {
		return onto.getAxioms();
	}
	
	@OWLAspectAnd("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLQLComplexity")
	private void assoQL(Set<OWLAxiom> axs) {
		om.addAxioms(onto, axs);
	}
	
	@OWLAspectAnd("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example#OWLQLComplexity")
	private Set<OWLAxiom> gettoQL() {
		return onto.getAxioms();
	}

}
