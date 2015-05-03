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
import org.semanticweb.owlapi.model.parameters.AxiomAnnotations;
import org.semanticweb.owlapi.model.parameters.Imports;

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
	 * @param imports
	 * 			Imports (included or exluded)
	 * @param axiomAnnotations
	 * 			AxiomAnnotations (consider or ignore)
	 * @param ontology
	 * 			Ontology to be searched
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if ontology contains the axiom like this which has current aspects; 
	 * 			false otherwise
	 */
	public static boolean containsAxiom3(OWLAxiom axiom, Imports imports,
			AxiomAnnotations axiomAnnotations, OWLOntology ontology,
			Annotation annotation) {	
		Set<OWLAxiom> axsToTest = getSimilarAxioms(
				axiom, imports, axiomAnnotations, ontology); 
		// check if one of the axioms in question has current aspects	
		for (OWLAxiom ax : axsToTest) {
			if (passAspectsTest(ax, ontology, annotation)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks whether a given ontology contains this axiom considering current aspects
	 * (aspects are provided in an Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr})
	 * 
	 * @param axiom
	 * 			axiom to be checked
	 * @param ontology
	 * 			Ontology to be searched
	 * @param imports
	 * 			information, whether to include imports (boolean) 
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if ontology contains the axiom like this which has current aspects; 
	 * 			false otherwise
	 */
	public static boolean containsAxiomES3Args(OWLAxiom axiom,
			OWLOntology ontology, boolean imports, Annotation annotation) {
		// transform imports statement from boolean to Imports
		Imports importsClosure = (imports) ? Imports.INCLUDED : Imports.EXCLUDED;
		return containsAxiom3(axiom, importsClosure, 
				AxiomAnnotations.CONSIDER_AXIOM_ANNOTATIONS, ontology, annotation);
	}


	/**
	 * checks whether any of the given ontologies contains this axiom considering current aspects
	 * (aspects are provided in an Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr})
	 * 
	 * @param axiom
	 * 			axiom to be checked
	 * @param ontologies
	 * 			Ontologies to be searched (Iterable)
	 * @param imports
	 * 			information, whether to include imports (boolean) 
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if at least one of the ontologies contains the axiom like this which has current aspects; 
	 * 			false otherwise
	 */
	public static boolean containsAxiomESIterable3Args(OWLAxiom axiom,
			Iterable<OWLOntology> ontologies, boolean imports,
			Annotation annotation) {
		for (OWLOntology onto : ontologies) {
			if (containsAxiomES3Args(axiom, onto, imports, annotation)) {
				return true;
			}
		} // we have checked all the ontologies, and none of them contains such axiom
		return false;
	}
	
	// ------------------------ HELPER ------------------------
	
	/**
	 * returns a set of axioms from this ontology which are similar to this axiom 
	 * if aspect annotations are ignored
	 * 
	 * @param axiom
	 * 			axiom for which the similar axioms have to be found
	 * @param imports
	 * 			info whether to include imports
	 * @param axiomAnnotations
	 * 			info whether to consider annotations
	 * @param ontology
	 * 			ontology to be checked
	 * @return set of similar axioms
	 */
	private static Set<OWLAxiom> getSimilarAxioms(OWLAxiom axiom,
			Imports imports, AxiomAnnotations axiomAnnotations,
			OWLOntology ontology) {
		OWLDataFactory df = ontology.getOWLOntologyManager().getOWLDataFactory();
		Set<OWLAxiom> similarAxioms = ontology.getAxiomsIgnoreAnnotations(axiom, imports);
		Set<OWLAxiom> axsToTest = new HashSet<OWLAxiom>();	
		
		if (axiomAnnotations.equals(AxiomAnnotations.IGNORE_AXIOM_ANNOTATIONS)) {
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
