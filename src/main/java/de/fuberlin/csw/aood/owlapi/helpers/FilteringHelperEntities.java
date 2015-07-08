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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;

/**
 * This helper provides methods to filter owl entities considering current aspects 
 */
public class FilteringHelperEntities extends FilteringHelper {	
	
	// ----------------- Helpers for Sets ------------------------------
	
	/**
	 * filters this set of entities with respect to current aspects specified in the annotation
	 * 
	 * @param ontologies
	 * 			ontologies to be checked
	 * @param entities
	 * 			set of entities to be filtered
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			set of entities 
	 * 				which have at least one declaration axiom 
	 *					which is associated with current aspects in any of these ontologies
	 */
	public static Set<OWLEntity> filterEntitiesFromOntologies(
			Iterable<OWLOntology> ontologies, Set<OWLEntity> entities, Annotation annotation) {
		Collection<OWLEntity> filteredEntities = new HashSet<OWLEntity>();
		for (OWLOntology onto : ontologies) {
			filteredEntities.addAll(filterEntities(onto, entities, annotation));
		}
		return (Set<OWLEntity>) filteredEntities;
	}
	
	/**
	 * filters this set of entities with respect to current aspects specified in the annotation
	 * 
	 * @param onto
	 * 			ontology to be checked
	 * @param entities
	 * 			set of entities to be filtered
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			set of entities which have a declaration axiom 
	 * 			associated with current aspects in this ontology
	 */
	public static Set<OWLEntity> filterEntities(OWLOntology onto, Set<OWLEntity> entities, Annotation annotation) {
		// The passed annotation can be only instance of OWLAspectAnd or OWLAspectOr
		String[][] aspects = transformAnnotationToAspects(annotation);
		Set<OWLEntity> filteredEntities = new HashSet<OWLEntity>();
		for (OWLEntity entity : entities) {	
			// we used to check for annotations on entity, the following way: 
			// if (EntitySearcher.getAnnotations(entity, onto).containsAll(createSetOfRelevantAnnotations(onto, currentAspects))) { }
			// but changed it to checking annotations on referencing declaration axioms. 
			// maybe we should also check for other referencing axioms or declaration axs in imports...
			Collection<OWLAxiom> referencingAxioms = entity.getReferencingAxioms(onto);
			for (OWLAxiom refAx : referencingAxioms) {
				if ((refAx instanceof OWLDeclarationAxiom) 
						&& passAspectsTest(refAx, onto, aspects)) {
					filteredEntities.add(entity);
				}
			}
		}
		return filteredEntities;
	}	

}
