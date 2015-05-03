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

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

import de.fuberlin.csw.OWLAspectAnd;
import de.fuberlin.csw.OWLAspectOr;

/**
 * This helper provides methods related to 
 * filtering groups of owl objects with respect to current aspects 
 * and needed by multiple other helpers
 */
public abstract class FilteringHelper extends BasicHelper {
	
	
	/**
	 * checks if this axiom in this ontology has these aspects
	 * 
	 * @param ax
	 * 			axiom to be checked
	 * @param onto
	 * 			ontology to be searched
	 * @param aspects
	 * 			aspect IRIs organized as an array of String arrays
	 * @return
	 * 			true, if axiom has these aspects, 
	 * 			false otherwise
	 */
	public static boolean passAspectsTest(OWLAxiom ax, OWLOntology onto, String[][] aspects) {
		for (String[] asps : aspects) {
			if (hasAllAspects(ax, onto, asps)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if this axiom in this ontology has these aspects
	 * 
	 * @param ax
	 * 			axiom to be checked
	 * @param onto
	 * 			ontology to be searched
	 * @param annotationWithCurrentAspects
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if axiom has these aspects, 
	 * 			false otherwise
	 */
	public static boolean passAspectsTest(OWLAxiom ax, OWLOntology onto, Annotation annotationWithCurrentAspects) {
		String[][] aspects = transformAnnotationToAspects(annotationWithCurrentAspects);
		for (String[] asps : aspects) {
			if (hasAllAspects(ax, onto, asps)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Checks if this annotation in this ontology has specified aspectIRI as value 
	 * and a subproperty of isPointcutOf as property
	 * 
	 * @param onto
	 * 			ontology containing this annotation
	 * @param anno
	 * 			annotation to be checked
	 * @param aspectIRI
	 * 			aspect IRI (as String)
	 * @return true if this annotation is relevant aspect with the specified IRI, 
	 * 			false otherwise.
	 */
	protected static boolean isRelevantAspect(OWLOntology onto, OWLAnnotation anno, String aspectIRI) {
		Set<OWLAnnotationProperty> aspectProperties = getAllAspectAnnotationProperties(onto);
		boolean isRelevant = anno.getValue().equals(IRI.create(aspectIRI)) && aspectProperties.contains(anno.getProperty());
		return isRelevant;
	}
	

	/**
	 * Returns all annotation properties that are transitively sub-property of
	 * the top annotation property.
	 * 
	 * @param onto
	 * 			Ontology
	 * @return All annotation properties in the given ontology that are transitively sub-property of
	 * the top annotation property.
	 */
	public static Set<OWLAnnotationProperty> getAllAspectAnnotationProperties (OWLOntology onto) {
		return fillSubProperties(new HashSet<OWLAnnotationProperty>(), onto.getOWLOntologyManager().getOWLDataFactory().getOWLAnnotationProperty(isPointcutOfPropertyIRI), onto);
	}
	
	/**
	 * adds this annotation property to this set 
	 * and fills it also with subproperties of this property in this ontology including imports closure
	 * 
	 * @param set
	 * 			set of annotation properties
	 * @param property
	 * 			annotation property to be added together with its subproperties
	 * @param onto
	 * 			ontology
	 * @return
	 * 			set filled with this property and its subproperties
	 */
	private static Set<OWLAnnotationProperty> fillSubProperties(Set<OWLAnnotationProperty> set, OWLAnnotationProperty property, OWLOntology onto) {
		set.add(property);
		for(OWLAnnotationProperty subProperty : EntitySearcher.getSubProperties(property, onto, true)) {
			fillSubProperties(set, subProperty, onto);
		}
		return set;
	}
	
	
}
