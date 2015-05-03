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
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import de.fuberlin.csw.OWLAspectAnd;
import de.fuberlin.csw.OWLAspectOr;

/**
 * This helper handles methods related to counting owl axioms considering current aspects 
 */
public class FilteringHelperAxiomCount extends FilteringHelperAxioms {
	
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
		Set<OWLAxiom> axs = ontology.getAxioms();
		return countFilteredAxioms(ontology, annotation, axs);
	}

	/**
	 * count axioms with current aspects in this ontology optionally including imports
	 * 
	 * @param imports
	 * 			info whether to include imports
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static int handleAxiomCount1(Imports imports,
			OWLOntology ontology, Annotation annotation) {
		Set<OWLAxiom> axs = ontology.getAxioms(imports);
		return countFilteredAxioms(ontology, annotation, axs);
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
	public static <T extends OWLAxiom> int handleAxiomCount1(AxiomType<T> axType,
			OWLOntology ontology, Annotation annotation) {
		Set<T> axs = ontology.getAxioms(axType);
		return countFilteredAxioms(ontology, annotation, axs);
	}
	
	/**
	 * count axioms of this type associated with current aspects in this ontology 
	 * optionally including axioms
	 * 
	 * @param <T>
	 * 			type extending OWLAxiom
	 * @param axType
	 * 			Axiom type
	 * @param imports 
	 * 			info whether to include imports
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static <T extends OWLAxiom> int handleAxiomCount2(
			AxiomType<T> axType, Imports imports,
			OWLOntology ontology, Annotation annotation) {
		Set<T> axs = ontology.getAxioms(axType, imports);
		return countFilteredAxioms(ontology, annotation, axs);
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
	public static int handleLogicalAxiomCount(
			OWLOntology ontology, Annotation annotation) {
		Set<OWLLogicalAxiom> axs = ontology.getLogicalAxioms();
		return countFilteredAxioms(ontology, annotation, axs);
	}
	
	/**
	 * count logical axioms associated with current aspects in this ontology, optionally including imports
	 * 
	 * @param imports
	 * 			info whether to include imports
	 * @param ontology
	 * 			ontology to check
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return number of axioms with current aspects
	 */
	public static int handleLogicalAxiomCountImports(
			Imports imports, OWLOntology ontology, Annotation annotation) {
		Set<OWLLogicalAxiom> axs = ontology.getLogicalAxioms(imports);
		return countFilteredAxioms(ontology, annotation, axs);
	}
	
	// ----------------- HELPER ------------------------------
	
	/**
	 * counts the number of the axioms in this set which are associated with current aspects
	 * 
	 * @param ontology
	 * 			ontology to be checked
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @param axs
	 * 			set of axioms to be filtered
	 * @return
	 * 			subset of this axiom set
	 * 				containing only axioms associated with current aspects in this ontology
	 */
	private static int countFilteredAxioms(OWLOntology ontology,
			Annotation annotation, Set<? extends OWLAxiom> axs) {
		@SuppressWarnings("unchecked")
		Set<OWLAxiom> filteredAxs = filterAxioms(ontology, (Set<OWLAxiom>) axs, annotation);	
		return filteredAxs.size();
	}
	

}
