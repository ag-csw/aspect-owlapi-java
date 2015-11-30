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

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;

/**
 * This helper provides methods to filter owl anonymous individuals considering current aspects 
 */
public class FilteringHelperAnonymous extends FilteringHelper {
	
	/**
	 * filters this set of anonymous individuals with respect to current aspects
	 * 
	 * @param anInds
	 * 			set of anonymous individuals
	 * @param onto
	 * 			ontology to be searched
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of anonymous individuals from this set, 
	 * for which there exist a referencing axiom in this ontology, which has current aspects
	 */
	public static Set<OWLAnonymousIndividual> filterAnonymousIndividuals(
			Set<OWLAnonymousIndividual> anInds, OWLOntology onto, Annotation annotation) {
		Set<OWLAnonymousIndividual> result = new HashSet<OWLAnonymousIndividual>();



		String[][] aspects = transformAnnotationToAspects(annotation);

		for (OWLAnonymousIndividual anInd : anInds) {
			// check if any of the axiom in this ontology referring to this anonymous individual 
			// has current aspects
			for (OWLAxiom ax : onto.getAxioms()) {
				if(ax.getAnonymousIndividuals().contains(anInd) &&
						passAspectsTest(ax, onto, aspects)) {
					result.add(anInd);
					break;
				}
			}
		}
		return result;
	}

}
