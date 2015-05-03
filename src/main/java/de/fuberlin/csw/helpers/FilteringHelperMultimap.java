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

import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import de.fuberlin.csw.OWLAspectAnd;
import de.fuberlin.csw.OWLAspectOr;

/**
 * This helper provides methods to filter multimaps of owl objects considering current aspects 
 */
public class FilteringHelperMultimap extends FilteringHelper {
	
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
	public static Multimap<? extends OWLObject, ? extends OWLObject> filterMultimapFromOntologies(String methodName,
			OWLIndividual userParam, Iterable<OWLOntology> ontologies, Annotation annotation) {
		// Transform annotation
		String[][] aspects = transformAnnotationToAspects(annotation);
		Multimap<? extends OWLObject, ? extends OWLObject> result = LinkedListMultimap.create();	
		switch (methodName) {
			case "getObjectPropertyValues":
				result = filterObjPropValues(userParam, ontologies, aspects);
				break;
			case "getNegativeObjectPropertyValues":
				result = filterNegObjPropValues(userParam, ontologies, aspects);
				break;
			case "getDataPropertyValues":
				result = filterDataPropValues(userParam, ontologies, aspects);
				break;
			case "getNegativeDataPropertyValues":
				result = filterNegDataPropValues(userParam, ontologies, aspects);		
		}
		return result;
	}
	
    /**
     * Obtains the object properties for this individual 
     * mapped to related object property values
     * where the corresponding asserting axiom has current aspects
     * 
     * @param userParam
     *        individual
     * @param ontologies
     *        ontologies to search
     * @param aspects
     *        aspects serving as filter criteria
     * @return properties mapped to property values
     */
	private static Multimap<OWLObjectPropertyExpression, OWLIndividual> filterObjPropValues(
			OWLIndividual userParam, Iterable<OWLOntology> ontologies,
			String[][] aspects) {
        Multimap<OWLObjectPropertyExpression, OWLIndividual> resultMap = LinkedListMultimap.create();
        for (OWLOntology onto : ontologies) {
            for (OWLObjectPropertyAssertionAxiom ax : onto.getObjectPropertyAssertionAxioms(userParam)) {
            	if (passAspectsTest(ax, onto, aspects)) {
            		 resultMap.put(ax.getProperty(), ax.getObject());
            	}
            }
        }
        return resultMap;
	}

    /**
     * Obtains the negative object properties connected to this individual 
     * mapped to related negative object property values
     * where the corresponding asserting axiom has current aspects
     * 
     * @param userParam
     *        individual
     * @param ontologies
     *        ontologies to search
     * @param aspects
     *        aspects serving as filter criteria
     * @return properties mapped to property values
     */
	private static Multimap<OWLObjectPropertyExpression, OWLIndividual> filterNegObjPropValues(
			OWLIndividual userParam, Iterable<OWLOntology> ontologies, String[][] aspects) {
        Multimap<OWLObjectPropertyExpression, OWLIndividual> resultMap = LinkedListMultimap.create();
        for (OWLOntology onto : ontologies) {
            for (OWLNegativeObjectPropertyAssertionAxiom ax : onto.getNegativeObjectPropertyAssertionAxioms(userParam)) {
            	if (passAspectsTest(ax, onto, aspects)) {
            		 resultMap.put(ax.getProperty(), ax.getObject());
            	}
            }
        }
        return resultMap;
	}

    /**
     * Obtains the data properties connected to this individual 
     * mapped to related data property values
     * where the corresponding asserting axiom has current aspects
     * 
     * @param userParam
     *        individual
     * @param ontologies
     *        ontologies to search
     * @param aspects
     *        aspects serving as filter criteria
     * @return properties mapped to property values
     */
	private static Multimap<OWLDataPropertyExpression, OWLLiteral> filterDataPropValues(
			OWLIndividual userParam, Iterable<OWLOntology> ontologies, String[][] aspects) {
        Multimap<OWLDataPropertyExpression, OWLLiteral> resultMap = LinkedListMultimap.create();
        for (OWLOntology onto : ontologies) {
            for (OWLDataPropertyAssertionAxiom ax : onto.getDataPropertyAssertionAxioms(userParam)) {
            	if (passAspectsTest(ax, onto, aspects)) {
            		 resultMap.put(ax.getProperty(), ax.getObject());
            	}
            }
        }
        return resultMap;
	}

    /**
     * Obtains the negative data properties connected to this individual 
     * mapped to related negative data property values
     * where the corresponding asserting axiom has current aspects
     * 
     * @param userParam
     *        individual
     * @param ontologies
     *        ontologies to search
     * @param aspects
     *        aspects serving as filter criteria
     * @return properties mapped to property values
     */
	private static Multimap<OWLDataPropertyExpression, OWLLiteral> filterNegDataPropValues(
			OWLIndividual userParam, Iterable<OWLOntology> ontologies,
			String[][] aspects) {
        Multimap<OWLDataPropertyExpression, OWLLiteral> resultMap = LinkedListMultimap.create();
        for (OWLOntology onto : ontologies) {
            for (OWLNegativeDataPropertyAssertionAxiom ax : onto.getNegativeDataPropertyAssertionAxioms(userParam)) {
            	if (passAspectsTest(ax, onto, aspects)) {
            		 resultMap.put(ax.getProperty(), ax.getObject());
            	}
            }
        }
        return resultMap;
	}

}
