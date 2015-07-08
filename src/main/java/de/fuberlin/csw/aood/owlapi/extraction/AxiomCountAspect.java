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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;

/**
 * This Aspect implements concern "count axioms associated with owl-aspects"
 */
@Aspect
public class AxiomCountAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	// no args
	/**
	 * quantifies over calls to methods of type getAxiomCount() with zero arguments
	 * provided by types from org.semanticweb.owlapi.model package.
	 */
	@Pointcut("call(public int org.semanticweb.owlapi.model.*.getAxiomCount())")
	void getAxCount() {}
	
	// with 1 arg (AxiomType or Imports)
	// we ignore deprecated method getAxiomCount(boolean imports)
	/**
	 * quantifies over calls to methods of type getAxiomCount(axType) 
	 * provided by types from org.semanticweb.owlapi.model package and 
	 * having one argument of type AxiomType.
	 */
	@Pointcut("call(public int org.semanticweb.owlapi.model.*.getAxiomCount(org.semanticweb.owlapi.model.AxiomType<*>))")
	void getAxCountAxiomType() {}
	
	// with 2 args (AxiomType, Imports)
	/**
	 * quantifies over calls to methods of type getAxiomCount(axType, imports)
	 * provided by types from org.semanticweb.owlapi.model package and  
	 * having two arguments: first one of type AxiomType, second one of type Imports
	 */
	@Pointcut("call(public int org.semanticweb.owlapi.model.*.getAxiomCount(org.semanticweb.owlapi.model.AxiomType<*>, org.semanticweb.owlapi.model.parameters.Imports))")
	void getAxCount2() {}
	
	// logical axs
	/**
	 * quantifies over calls to methods of type getLogicalAxiomCount() with zero arguments
	 * provided by types from org.semanticweb.owlapi.model package and 
	 */
	@Pointcut("call(public int org.semanticweb.owlapi.model.*.getLogicalAxiomCount())")
	void getLogicalAxCount() {}


	// USED POINTCUTS and ADVICES ============================================================
	
	// for getAxiomCount() without args --------------------------------------------------

	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount() && target(ontology) && @within(annotation)") 
	public Object aroundGetAxCountWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleAxiomCount(ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount() && target(ontology) && @within(annotation)") 
	public Object aroundGetAxCountWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleAxiomCount(ontology, annotation);
	}

	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount() && target(ontology) && @withincode(annotation)") 
	public Object aroundGetAxCountWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleAxiomCount(ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount() && target(ontology) && @withincode(annotation)") 
	public Object aroundGetAxCountWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleAxiomCount(ontology, annotation);
	}
	
	// for getAxiomCount() with 1 arg ----------------------------------------------------
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg)
	 * if this method has one argument (of type either AxiomType or Imports) and
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCountAxiomType() && target(ontology) && @within(annotation)")
	public Object aroundGetAxCountWithinClassMarkedWithAnd1(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleAxiomCount1(pjp, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg)
	 * if this method has one argument (of type either AxiomType or Imports) and
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCountAxiomType() && target(ontology) && @within(annotation)")
	public Object aroundGetAxCountWithinClassMarkedWithOr1(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleAxiomCount1(pjp, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg)
	 * if this method has one argument (of type either AxiomType or Imports) and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCountAxiomType() && target(ontology) && @withincode(annotation)")
	public Object aroundGetAxCountWithinCodeMarkedWithAnd1(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleAxiomCount1(pjp, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg)
	 * if this method has one argument (of type either AxiomType or Imports) and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCountAxiomType() && target(ontology) && @withincode(annotation)")
	public Object aroundGetAxCountWithinCodeMarkedWithOr1(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleAxiomCount1(pjp, ontology, annotation);
	}

	// for getAxiomCount() with 2 args ----------------------------------------------------
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg1, arg2)
	 * if this method has two arguments (arg1 of type AxiomType, arg2 of type Imports) and
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param includeImports
	 * 			boolean
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount2() && target(ontology) && args(*, includeImports) && @within(annotation)")
	public Object aroundGetAxCountWithinClassMarkedWithAnd2(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation, boolean includeImports) throws Throwable {
		return handleAxiomCount2(pjp, ontology, annotation, includeImports);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg1, arg2)
	 * if this method has two arguments (arg1 of type AxiomType, arg2 of type Imports) and
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param includeImports
	 * 			boolean
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount2() && target(ontology) && args(*, includeImports) && @within(annotation)")
	public Object aroundGetAxCountWithinClassMarkedWithOr2(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation, boolean includeImports) throws Throwable {
		return handleAxiomCount2(pjp, ontology, annotation, includeImports);
	}

	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg1, arg2)
	 * if this method has two arguments (arg1 of type AxiomType, arg2 of type Imports) and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param includeImports
	 * 			boolean
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount2() && target(ontology) && args(*, includeImports) && @withincode(annotation)")
	public Object aroundGetAxCountWithinCodeMarkedWithAnd2(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation, boolean includeImports) throws Throwable {
		return handleAxiomCount2(pjp, ontology, annotation, includeImports);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getAxiomCount(arg1, arg2)
	 * if this method has two arguments (arg1 of type AxiomType, arg2 of type Imports) and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param includeImports
	 * 			inlcudeImports
	 * @return
	 * 			Number of axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxCount2() && target(ontology) && args(*, includeImports) && @withincode(annotation)")
	public Object aroundGetAxCountWithinCodeMarkedWithOr2(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation, boolean includeImports) throws Throwable {
		return handleAxiomCount2(pjp, ontology, annotation, includeImports);
	}
	
	// for getLogicalAxiomCount() ----------------------------------------------------
	
	/**
	 * advice responsible for handling result of the call to a method of type getLogicalAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Number of logical axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getLogicalAxCount() && target(ontology) && @within(annotation)") 
	public Object aroundLogicalAxCountWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleLogicalAxiomCount(ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getLogicalAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of logical axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getLogicalAxCount() && target(ontology) && @within(annotation)") 
	public Object aroundLogicalAxCountWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleLogicalAxiomCount(ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getLogicalAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Number of logical axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getLogicalAxCount() && target(ontology) && @withincode(annotation)") 
	public Object aroundLogicalAxCountWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleLogicalAxiomCount(ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type getLogicalAxiomCount()
	 * if this method has zero arguments and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of logical axioms related to current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getLogicalAxCount() && target(ontology) && @withincode(annotation)") 
	public Object aroundLogicalAxCountWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleLogicalAxiomCount(ontology, annotation);
	}
	

	
	// ------------------------------- HELPER  --------------------------------
	
	/**
	 * Responsible for handling result of the call to a method of type getAxiomCount()
	 * if this method has zero arguments 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 */
	private Object handleAxiomCount(OWLOntology ontology, Annotation annotation) {
		return HelperFacade.handleAxiomCount(ontology, annotation);
	}
	
	/**
	 * Responsible for handling result of the call to a method of type getAxiomCount(arg)
	 * if this method has one argument and
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of axioms related to current aspects
	 */
	private Object handleAxiomCount1(ProceedingJoinPoint pjp,
			OWLOntology ontology, Annotation annotation) {
		Object arg = (pjp.getArgs())[0];
		@SuppressWarnings("unchecked")
        AxiomType<? extends OWLAxiom> axType = (AxiomType<? extends OWLAxiom>) arg;
		return HelperFacade.handleAxiomCount1(axType, ontology, annotation);

	}
	
	/**
	 * Responsible for handling result of the call to a method of type getAxiomCount(axType, imports)
	 * if this method has two arguments (AxiomType axType, Imports imports) and
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @param includeImports
	 * 			boolean
	 * @return
	 * 			Number of axioms related to current aspects
	 */
	private Object handleAxiomCount2(ProceedingJoinPoint pjp,
			OWLOntology ontology, Annotation annotation, boolean includeImports) {
		@SuppressWarnings("unchecked")
		AxiomType<? extends OWLAxiom> axType = (AxiomType<? extends OWLAxiom>) (pjp.getArgs())[0];	
		return HelperFacade.handleAxiomCount2(axType, includeImports, ontology, annotation);
	}
	
	/**
	 * Responsible for handling result of the call to a method of type getLogicalAxiomCount()
	 * if this method has zero arguments 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Number of logical axioms related to current aspects
	 */
	private Object handleLogicalAxiomCount(OWLOntology ontology, Annotation annotation) {
		return HelperFacade.handleLogicalAxiomCount(ontology, annotation);
	}


}
