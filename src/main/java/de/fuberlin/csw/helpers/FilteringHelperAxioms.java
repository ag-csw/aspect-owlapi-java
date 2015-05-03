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
package de.fuberlin.csw.helpers;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.OWLAspectAnd;
import de.fuberlin.csw.OWLAspectOr;

/**
 * This helper provides methods to filter owl axioms considering current aspects 
 */
public class FilteringHelperAxioms extends FilteringHelper {
	
	/**
	 * returns the subset of this axiom set consisting only of axioms associated with current aspects
	 * 
	 * @param onto
	 * 			ontology to be checked
	 * @param axioms
	 * 			set of axioms to be filtered
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return filtered axioms
	 */
	public static Set<OWLAxiom> filterAxioms(OWLOntology onto, Set<OWLAxiom> axioms, Annotation annotation) {
		// The passed annotation can be only of type OWLAspectAnd or OWLAspectOr
		String[][] aspects = transformAnnotationToAspects(annotation);
		Set<OWLAxiom> filteredAxioms = new HashSet<OWLAxiom>();
		for (OWLAxiom ax : axioms) {
			if (passAspectsTest(ax, onto, aspects)) {
				filteredAxioms.add(ax);
			}	
		}
		return filteredAxioms;
	}
}
