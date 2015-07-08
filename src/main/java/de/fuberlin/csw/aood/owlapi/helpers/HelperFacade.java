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
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.*;


import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;

/**
 * This class delegates requests from AspectJ-Aspects to appropriate helpers
 */
public class HelperFacade {
	
	// ----------------- FILTERING HELPERS ------------------------------
	
	/**
	 * returns the subset of this axiom set consisting only of axioms associated with current aspects
	 * 
	 * @param ontology
	 * 			ontology to be checked
	 * @param axioms
	 * 			set of axioms to be filtered
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return filtered axioms
	 */
	public static Set<OWLAxiom> filterAxioms(OWLOntology ontology, Set<OWLAxiom> axioms, Annotation annotation) {
		return FilteringHelperAxioms.filterAxioms(ontology, axioms, annotation);
	}

	/**
	 * filters this set of entities with respect to current aspects specified in the annotation
	 * 
	 * @param ontology
	 * 			ontology to be checked
	 * @param entities
	 * 			set of entities to be filtered
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			set of entities which have a declaration axiom 
	 * 			associated with current aspects in this ontology
	 */
	public static Set<OWLEntity> filterEntities(
			OWLOntology ontology, Set<OWLEntity> entities, Annotation annotation) {
		return FilteringHelperEntities.filterEntities(ontology, entities, annotation);
	}
	
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
	public static Collection<OWLEntity> filterEntities(
			Iterable<OWLOntology> ontologies, Set<OWLEntity> entities, Annotation annotation) {
		return FilteringHelperEntities.filterEntitiesFromOntologies(ontologies, entities, annotation);
	}
	
    /**
     * Sorts and delegates to filtering the four different EntitySearcher methods which return multimaps: 
     * getObjectPropertyValues(), getNegativeObjectPropertyValues(), 
     * getDataPropertyValues(), getNegativeDataPropertyValues().
     * 
	 * @param methodName
	 * 		  name of the method which returns multimap
	 * @param userParam
	 * 		  individual
	 * @param ontologies
	 * 		  ontologies to search
	 * @param annotation
	 * 		  Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return filtered multimap
	 */
	public static Object filterMap( String methodName,
			OWLIndividual userParam, Iterable<OWLOntology> ontologies, Annotation annotation) {
		return FilteringHelperMultimap.filterMultimapFromOntologies(methodName, userParam, ontologies, annotation);
	}
	
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
		return FilteringHelperAnonymous.filterAnonymousIndividuals(anInds, onto, annotation);
	}
	
	// ----------------- MODIFICATION HELPERS ------------------------------
	
	/**
	 * delegates the change to be handled according to the type of the passed annotation specifying current aspects
	 * 
	 * @param change
	 * 			change to be handled
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return ChangeApplied : status telling if the change was applied successfully 
	 * 			(if aspect annotations were updated successfully)
	 */
	public static List<OWLOntologyChange> handleAxiomChange(OWLOntologyChange change, Annotation annotation) {
		return ModificationHelper.handleAxiomChange(change, annotation);
	}
	
	/**
	 * handles axiom which user asked to remove under current aspects
	 * 
	 * @param userAx
	 * 			axiom to be removed under current aspects
	 * @param onto
	 * 			current ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return List of changes (RemoveAxiom this axiom) if aspect annotations were updated
	 */
	public static List<OWLOntologyChange> handleRemoveAxiomReturnChanges(
			OWLAxiom userAx, OWLOntology onto, Annotation annotation) {
		return ModificationHelperRemove.handleChangeRemoveAxiom(new RemoveAxiom(onto, userAx), annotation);
	}
	
	/**
	 * controls multiple changes to be applied, considering current aspects
	 * 
	 * @param changes
	 * 			changes to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return list of changes applied successfully (considering successful updating of aspect annotations)
	 */
	public static List<OWLOntologyChange> handleMultipleChanges(List<OWLOntologyChange> changes, Annotation annotation) {
		return ModificationHelper.handleMultipleChanges(changes, annotation);
	}
	
	/**
	 * handles addition or removal of these axioms,
	 * depending on the value of parameter isAddAxiom
	 * 
	 * @param ontology
	 * 			ontology 
	 * @param axioms
	 * 			axioms
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @param isAddAxioms
	 * 			boolean telling whether this method is addAxioms()
	 * @return list of changes applied successfully (considering successful updating of aspect annotations)
	 */
	public static List<OWLOntologyChange> handleAddOrRemoveAxs(
			OWLOntology ontology, Set<OWLAxiom> axioms, Annotation annotation, boolean isAddAxioms) {
		return ModificationHelper.handleAddOrRemoveAxs(ontology, axioms, annotation, isAddAxioms);
	}
	
	// ----------------- CONTAINMENT HELPERS ------------------------------
	
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
		return ContainmentHelper.containsAxiom1(onto, axiom, annotation);
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
		return ContainmentHelper.containsAxiom2(axiom, includeImports, considerAnnotations, ontology, annotation);
	}



	
	// ----------------- AXIOM COUNT HELPERS --------------------------
	
	/**
	 * count axioms with current aspects in this ontology
	 * 
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static int handleAxiomCount(OWLOntology ontology, Annotation annotation) {
		return FilteringHelperAxiomCount.handleAxiomCount(ontology, annotation);
	}

	
	/**
	 * count axioms of this type associated with current aspects in this ontology
	 * 
	 * @param <T>
	 * 			type extending OWLAxiom
	 * @param axType
	 * 			Axiom type
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static <T extends OWLAxiom> int handleAxiomCount1(AxiomType<T> axType, OWLOntology ontology, Annotation annotation) {
		return FilteringHelperAxiomCount.handleAxiomCount1(axType, ontology, annotation);
	}
	
	/**
	 * count axioms of this type associated with current aspects in this ontology 
	 * optionally including axioms
	 * 
	 * @param <T>
	 * 			type extending OWLAxiom
	 * @param axType
	 * 			Axiom type
	 * @param includeImports
	 * 			info whether to include imports
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static <T extends OWLAxiom> int handleAxiomCount2(
			AxiomType<T> axType, boolean includeImports,
			OWLOntology ontology, Annotation annotation) {
		return FilteringHelperAxiomCount.handleAxiomCount2(axType, includeImports, ontology, annotation);
	}
	
	/**
	 * count logical axioms associated with current aspects in this ontology
	 * 
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static int handleLogicalAxiomCount(OWLOntology ontology, Annotation annotation) {
		return FilteringHelperAxiomCount.handleLogicalAxiomCount(ontology, annotation);
	}
	


}
