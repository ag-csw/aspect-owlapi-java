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
package de.fuberlin.csw.aood.owlapi.extraction;

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;

/**
 * This Aspect implements concern "extraction of Multimaps related to owl-aspects
 * using methods provided by org.semanticweb.owlapi.search.EntitySearcher"
 */
@Aspect
public class MultimapExtractionAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	// from org.semanticweb.owlapi.search.EntitySearcher ---------------------------------
	
	/**
	 * quantifies over EntitySearcher methods 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in this one ontology
	 * <p>e.g. EntitySearcher.getObjectPropertyValues(individual, ontology);
	 */
	@Pointcut("call(public com.google.common.collect.Multimap<*,*> org.semanticweb.owlapi.search.EntitySearcher.get*(*, org.semanticweb.owlapi.model.OWLOntology))")
	void getMultimap() {}
	
	/**
	 * quantifies over EntitySearcher methods 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in these multiple ontologies
	 * <p>e.g. EntitySearcher.getObjectPropertyValues(individual, ontologies);
	 */
	@Pointcut("call(public com.google.common.collect.Multimap<*,*> org.semanticweb.owlapi.search.EntitySearcher.get*(*, java.lang.Iterable<org.semanticweb.owlapi.model.OWLOntology>))")
	void getMultimapFromOntologies() {}
	
	// USED POINTCUTS and ADVICES ============================================================
	
	// for Multimaps from single ontology ------------------------------------------------
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in this one ontology, 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values
	 * 			such that the axiom relating this individual to this property value via this property
	 * 			in this ontology has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimap() && args(ind, onto) && @within(annotation)")
	public Object aroundMultimapWithinAnd(ProceedingJoinPoint pjp, 
			OWLIndividual ind, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
		return handleMultimap(pjp, ind, Collections.singleton(onto), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in this one ontology, 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values
	 * 			such that the axiom relating this individual to this property value via this property
	 * 			in this ontology has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimap() && args(ind, onto) && @within(annotation)")
	public Object aroundMultimapWithinOr(ProceedingJoinPoint pjp, 
			OWLIndividual ind, OWLOntology onto, OWLAspectOr annotation) throws Throwable  {
		return handleMultimap(pjp, ind, Collections.singleton(onto), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in this one ontology, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values
	 * 			such that the axiom relating this individual to this property value via this property
	 * 			in this ontology has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimap() && args(ind, onto) && @withincode(annotation)")
	public Object aroundMultimapWithinCodeAnd(ProceedingJoinPoint pjp, 
			OWLIndividual ind, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
		return handleMultimap(pjp, ind, Collections.singleton(onto), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in this one ontology, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values
	 * 			such that the axiom relating this individual to this property value via this property
	 * 			in this ontology has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimap() && args(ind, onto) && @withincode(annotation)")
	public Object aroundMultimapWithinCodeOr(ProceedingJoinPoint pjp, 
			OWLIndividual ind, OWLOntology onto, OWLAspectOr annotation) throws Throwable  {
		return handleMultimap(pjp, ind, Collections.singleton(onto), annotation);
	}
	
	// for Multimaps from ontologies ----------------------------------------------------------
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in these multiple ontologies, 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param ontologies
	 * 			ontologies (Iterable)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values such that 
	 * 			the axiom relating this individual to this property value via this property 
	 * 			in these ontologies has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimapFromOntologies() && args(ind, ontologies) && @within(annotation)")
	public Object aroundMultimapFromOntologiesWithinAnd(ProceedingJoinPoint pjp, 
			OWLIndividual ind, Iterable<OWLOntology> ontologies, OWLAspectAnd annotation) throws Throwable  {
		return handleMultimap(pjp, ind, ontologies, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in these multiple ontologies, 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param ontologies
	 * 			ontologies (Iterable)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values such that 
	 * 			the axiom relating this individual to this property value via this property 
	 * 			in these ontologies has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimapFromOntologies() && args(ind, ontologies) && @within(annotation)")
	public Object aroundMultimapFromOntologiesWithinOr(ProceedingJoinPoint pjp, 
			OWLIndividual ind, Iterable<OWLOntology> ontologies, OWLAspectOr annotation) throws Throwable  {
		return handleMultimap(pjp, ind, ontologies, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in these multiple ontologies, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param ontologies
	 * 			ontologies (Iterable)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values such that 
	 * 			the axiom relating this individual to this property value via this property 
	 * 			in these ontologies has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimapFromOntologies() && args(ind, ontologies) && @withincode(annotation)")
	public Object aroundMultimapFromOntologiesWithinCodeAnd(ProceedingJoinPoint pjp, 
			OWLIndividual ind, Iterable<OWLOntology> ontologies, OWLAspectAnd annotation) throws Throwable  {
		return handleMultimap(pjp, ind, ontologies, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in these multiple ontologies, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual
	 * @param ontologies
	 * 			ontologies (Iterable)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values such that 
	 * 			the axiom relating this individual to this property value via this property 
	 * 			in these ontologies has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getMultimapFromOntologies() && args(ind, ontologies) && @withincode(annotation)")
	public Object aroundMultimapFromOntologiesWithinCodeOr(ProceedingJoinPoint pjp, 
			OWLIndividual ind, Iterable<OWLOntology> ontologies, OWLAspectOr annotation) throws Throwable  {
		return handleMultimap(pjp, ind, ontologies, annotation);
	}
	
	// ------------------------ HELPER ------------------------
	
	/**
	 * Responsible for handling result of the call to an EntitySearcher method 
	 * returning multimap of properties and corresponding property values,  
	 * which are referencing a given individual in one or multiple ontologies, 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ind
	 * 			OWLIndividual (whose properties with corresponding values are to be found)
	 * @param ontologies 
	 * 			Ontologies to be searched (Iterable). 
	 * 			(Single ontology can be passed e.g. as a Collection of singleton)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Multimap of properties and corresponding property values such that 
	 * 			the axiom relating this individual to this property value via this property 
	 * 			in at least one of these ontologies has current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleMultimap(ProceedingJoinPoint pjp, 
			OWLIndividual ind, Iterable<OWLOntology> ontologies, Annotation annotation) throws Throwable {
		String methodName = pjp.getSignature().getName();
		return HelperFacade.filterMultimap(methodName, ind, ontologies, annotation);
	}
	

}
