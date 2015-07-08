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
package de.fuberlin.csw.aood.owlapi.helpers;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;


import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;

/**
 * This helper contains methods providing consistent information 
 * about containment of axioms having current aspects in given ontologies
 */
public class ContainmentHelper extends FilteringHelper {
	
	/**
	 * checks whether a given ontology contains this axiom considering current aspects 
	 * (aspects are specified in an Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr})
	 * 
	 * @param onto
	 * 			ontology to be searched
	 * @param axiom
	 * 			axiom to be checked
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true if ontology contains such axiom and axiom has current aspects,
	 * 			false otherwise
	 */
	public static boolean containsAxiom1(OWLOntology onto, OWLAxiom axiom, Annotation annotation) {
		boolean result = false;
		Set<OWLAxiom> similarAxioms = getSimilarAxioms(axiom, onto);
		for (OWLAxiom similarAxiom : similarAxioms) {
			if (passAspectsTest(similarAxiom, onto, annotation)) {
				result = true;
			}
		}	
		return result;
	}
	
	/**
	 * checks whether a given ontology contains this axiom considering current aspects
	 * (aspects are provided in an Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr})
	 * 
	 * @param axiom
	 * 			axiom to be checked
	 * @param includeImports
	 * 			boolean (included or exluded)
	 * @param considerAnnotations
	 * 			boolean (consider or ignore)
	 * @param ontology
	 * 			Ontology to be searched
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if ontology contains the axiom like this which has current aspects; 
	 * 			false otherwise
	 */
	public static boolean containsAxiom2(OWLAxiom axiom, boolean includeImports,
			boolean considerAnnotations, OWLOntology ontology,
			Annotation annotation) {	
		Set<OWLAxiom> axsToTest = getSimilarAxioms(
				axiom, includeImports, considerAnnotations, ontology);
		// check if one of the axioms in question has current aspects	
		for (OWLAxiom ax : axsToTest) {
			if (passAspectsTest(ax, ontology, annotation)) {
				return true;
			}
		}
		return false;
	}
	



	
	// ------------------------ HELPER ------------------------
	
	/**
	 * returns a set of axioms from this ontology which are similar to this axiom 
	 * if aspect annotations are ignored
	 * 
	 * @param axiom
	 * 			axiom for which the similar axioms have to be found
	 * @param includeImports
	 * 			info whether to include imports
	 * @param considerAnnotations
	 * 			info whether to consider annotations
	 * @param ontology
	 * 			ontology to be checked
	 * @return set of similar axioms
	 */
	private static Set<OWLAxiom> getSimilarAxioms(OWLAxiom axiom,
			boolean includeImports, boolean considerAnnotations,
			OWLOntology ontology) {
		OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();
		Set<OWLAxiom> similarAxioms = ontology.getAxiomsIgnoreAnnotations(axiom, includeImports);
		Set<OWLAxiom> axsToTest = new HashSet<OWLAxiom>();	
		
		if (!considerAnnotations) {
			axsToTest = similarAxioms;
		} else { // do not ignore axiom annotations		
			for (OWLAxiom similarAxiom : similarAxioms) {
				Set<OWLAnnotation> regularAnnotations = findRegularAnnotations(df, similarAxiom.getAnnotations());
				if (regularAnnotations.equals(axiom.getAnnotations())) {
					axsToTest.add(similarAxiom);
				}
			}		
		}
		return axsToTest;
	}

}
