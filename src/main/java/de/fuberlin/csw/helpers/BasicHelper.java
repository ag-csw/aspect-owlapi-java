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
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.OWLAspectAnd;
import de.fuberlin.csw.OWLAspectOr;

/**
 * This class is parent of all helper classes 
 * and contains attributes and functions needed by multiple inheriting helpers
 */
public abstract class BasicHelper {
	
	// Basic attributes needed by many subclasses (FilteringHelper, ModificationHelper...)
	
	/** IRI of aspect annotation property isPointcutOf */
	public static final IRI isPointcutOfPropertyIRI = IRI.create(
			"http://www.corporate-semantic-web.de/ontologies/aspect/owl#isPointcutOf");
	/** IRI of main aspect ontology */
	public static final IRI ASPECT_BASE_CLASS_IRI = IRI.create(
			"http://www.corporate-semantic-web.de/ontologies/aspect/owl#Aspect");
	
	// Basic helper methods needed by many subclasses (FilteringHelper, ModificationHelper...)

	/**
	 * takes an ontology and the IRIs of current aspects 
	 * and generates a set of current aspect annotations for this ontology
	 * 
	 * @param onto
	 * 			Ontology
	 * @param aspectIRIs
	 * 			IRIs of current aspects (provided as String array)
	 * @return
	 * 			set of current aspect annotations
	 */
	public static Set<OWLAnnotation> createSetOfRelevantAnnotations(OWLOntology onto, String[] aspectIRIs) {
		Set<OWLAnnotation> result = new HashSet<OWLAnnotation>(aspectIRIs.length);
		OWLDataFactory df = onto.getOWLOntologyManager().getOWLDataFactory();
		for (String aspectIRI : aspectIRIs) {
			result.add(df.getOWLAnnotation(df.getOWLAnnotationProperty(isPointcutOfPropertyIRI), IRI.create(aspectIRI)));
		}
		return result;
	}
	
	/**
	 * transforms the aspects represented as an annotation into aspects represented as array of string arrays
	 * 
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} to be transformed
	 * @return
	 * 			array of string arrays, where 
	 * 			the outer array represents logical disjunction (OR),  
	 * 			and the inner array represents logical conjunction (AND)
	 */
	public static String[][] transformAnnotationToAspects(Annotation annotation) {
		// The passed annotation can be only of type OWLAspectAnd or OWLAspectOr
	    if (annotation instanceof OWLAspectAnd){
	    	String[] currentAspects = ((OWLAspectAnd) annotation).value();
	    	String[][] aspects = new String[][] {currentAspects};
	    	return aspects;
	    	
	    } else {
	    	OWLAspectOr annoOr = (OWLAspectOr) annotation;
	    	OWLAspectAnd[] andLists = annoOr.value();
	    	int size = andLists.length;
	    	String[][] aspects = new String[size][];  	
			for (int i=0; i<size; i++) {
				aspects[i] = (andLists[i]).value();
			}
	    	return aspects;
	    }
	}
	
	/**
	 * checks whether this annotation is an aspect annotation 
	 * (using a data factory associated with a specific ontology)
	 * 
	 * @param anno
	 * 			annotation to be checked
	 * @param df
	 * 			data factory associated with a specific ontology
	 * @return
	 * 			true if this annotation is an aspect annotation, false otherwise
	 */
	public static boolean isAspectAnnotation (OWLAnnotation anno, OWLDataFactory df) {
		OWLAnnotationProperty aspectProp = df.getOWLAnnotationProperty(isPointcutOfPropertyIRI);
		OWLAnnotationProperty annoProp = anno.getProperty();
		return annoProp.equals(aspectProp);
	}
	
	/**
	 * gets the aspect annotation property isPointcutOf 
	 * using the data factory associated with a specific ontology
	 * 
	 * @param df
	 * 			data factory
	 * @return
	 * 			the aspect annotation property isPointcutOf for this ontology
	 */
	public static OWLAnnotationProperty getAspectProperty(OWLDataFactory df) {
		return df.getOWLAnnotationProperty(isPointcutOfPropertyIRI);
	}
	
	/**
	 * finds axioms in this ontology which are similar to the given axiom. 
	 * similar means that they may differ from the given axiom only in aspect annotations:
	 * they should have same regular annotations which are not aspects. 
	 * Usually it has to be only one such axiom there, but checking just in case for every ax.
	 * 
	 * @param passedAxiom
	 * 			axiom for which similar axioms have to be found
	 * @param onto
	 * 			ontology to be searched
	 * @return
	 * 			set of similar axioms
	 */
	public static Set<OWLAxiom> getSimilarAxioms(OWLAxiom passedAxiom, OWLOntology onto) {
		Set<OWLAxiom> result = new HashSet<OWLAxiom>();
		OWLDataFactory df = onto.getOWLOntologyManager().getOWLDataFactory();
		Set<OWLAxiom> similarAxioms = onto.getAxiomsIgnoreAnnotations(passedAxiom);
		for (OWLAxiom similarAxiom : similarAxioms) {
			Set<OWLAnnotation> regularAnnotationsOnSimilarAxiom = findRegularAnnotations(df, similarAxiom.getAnnotations());
			Set<OWLAnnotation> regularAnnotationsOnPassedAxiom = findRegularAnnotations(df, passedAxiom.getAnnotations());
			if (regularAnnotationsOnSimilarAxiom.equals(regularAnnotationsOnPassedAxiom)) {
				result.add(similarAxiom);
			}
		}
		return result;
	}
	
	/**
	 * Gets only regular annotations from this set of annotations. 
	 * Regular annotations are those which are not aspect annotations.
	 * 
	 * @param df
	 * 			data factory associated with a specific ontology
	 * @param axAnnotations
	 * 			set of annotations to be filtered
	 * @return
	 * 			set of regular annotations
	 */
	public static Set<OWLAnnotation> findRegularAnnotations(OWLDataFactory df,
			Set<OWLAnnotation> axAnnotations) {
		Set<OWLAnnotation> regularAnnotations = new HashSet<OWLAnnotation>();
		for (OWLAnnotation anno : axAnnotations) {
			if (!isAspectAnnotation(anno, df)) {
				regularAnnotations.add(anno);
			}	
		}
		return regularAnnotations;
	}
	
	/**
	 * returns the set of elements which are contained only in set2, but not in set1
	 * 
	 * @param <T> 
	 * 			any object type
	 * @param set1
	 * 			first set of objects
	 * @param set2
	 * 			second set of objects
	 * @return
	 * 			set of objects which are contained only in second set, but not in first set
	 */
	public static <T> Set<T> getComplementOfFirstSet(Set<T> set1, Set<T> set2) {
		Set<T> result = new HashSet<T>(set2);	 // copy
		result.removeAll(set1);
		return result;
	}
	
	/**
	 * checks for this axiom in this ontology if it has all current aspects
	 * 
	 * @param ax
	 * 			axiom
	 * @param onto
	 * 			ontology
	 * @param aspects
	 * 			current aspects provided as String array, logically conjunct
	 * @return true, if the axiom has all current aspects;
	 * 			false otherwise
	 */
	protected static boolean hasAllAspects(OWLAxiom ax, OWLOntology onto, String[] aspects) {
		return ax.getAnnotations().containsAll(createSetOfRelevantAnnotations(onto, aspects));
	}

}
