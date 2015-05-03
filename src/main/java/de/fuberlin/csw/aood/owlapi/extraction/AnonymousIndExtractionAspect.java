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
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;

/**
 * This Aspect implements concern "extraction of anonymous individuals related to owl-aspects"
 */
@Aspect
public class AnonymousIndExtractionAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	/**
	 * Pointcut responsible for quantification over calling methods extracting anonymous individuals
	 */
	@Pointcut("call(public java.util.Set<org.semanticweb.owlapi.model.OWLAnonymousIndividual> org.semanticweb.owlapi.model.*.getAnonymous*(..))")
	void getAnonInd() {}
	
	// USED POINTCUTS and ADVICES ============================================================
	
	// for getAnonymousIndividuals() ----------------------------------------------------
	
	/**
	 * advice responsible for handling result of the call to a method extracting anonymous individuals 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of anonymous individuals whose referencing axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAnonInd() && target(onto) && @within(annotation)")
	public Object aroundGetAnonIndWithinAnd(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
		return handleAnonymousIndividuals(pjp, onto, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method extracting anonymous individuals 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of anonymous individuals whose referencing axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAnonInd() && target(onto) && @within(annotation)")
	public Object aroundGetAnonIndWithinOr(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectOr annotation) throws Throwable  {
		return handleAnonymousIndividuals(pjp, onto, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method extracting anonymous individuals 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of anonymous individuals whose referencing axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAnonInd() && target(onto) && @withincode(annotation)")
	public Object aroundGetAnonIndWithinCodeAnd(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
		return handleAnonymousIndividuals(pjp, onto, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method extracting anonymous individuals 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of anonymous individuals whose referencing axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAnonInd() && target(onto) && @withincode(annotation)")
	public Object aroundGetAnonIndWithinCodeOr(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectOr annotation) throws Throwable  {
		return handleAnonymousIndividuals(pjp, onto, annotation);
	}
	
	// ------------------------------- HELPER  --------------------------------
	
	/**
	 * Responsible for handling result of the call to a method extracting anonymous individuals 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param onto
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of anonymous individuals whose referencing axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleAnonymousIndividuals(ProceedingJoinPoint pjp,
			OWLOntology onto, Annotation annotation) throws Throwable {
		@SuppressWarnings("unchecked")
		Set<OWLAnonymousIndividual> toFilter = (Set<OWLAnonymousIndividual>) pjp.proceed();
		return HelperFacade.filterAnonymousIndividuals(toFilter, onto, annotation);
	}

}
