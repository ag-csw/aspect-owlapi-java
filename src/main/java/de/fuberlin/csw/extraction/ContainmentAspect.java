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
package de.fuberlin.csw.extraction;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.AxiomAnnotations;
import org.semanticweb.owlapi.model.parameters.Imports;

import de.fuberlin.csw.OWLAspectAnd;
import de.fuberlin.csw.OWLAspectOr;
import de.fuberlin.csw.helpers.HelperFacade;

/**
 * This Aspect implements concern "containment information about axioms associated with owl-aspects"
 */
@Aspect
public class ContainmentAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	// arguments: (OWLAxiom)
	/**
	 * quantifies over calls to methods of type containsAxiom(axiom) 
	 * provided by types from org.semanticweb.owlapi.model package
	 * and having one argument of type OWLAxiom
	 */
	@Pointcut("call(public boolean org.semanticweb.owlapi.model.*.containsAxiom(org.semanticweb.owlapi.model.OWLAxiom))")
	void containsAx1() {}
	
	// arguments: (OWLAxiom, Imports, AxiomAnnotations)
	/**
	 * quantifies over calls to methods of type containsAxiom(arg1, arg2, arg3) 
	 * provided by types from org.semanticweb.owlapi.model package and 
	 * having three arguments where first argument is of type OWLAxiom
	 */
	@Pointcut("call(public boolean org.semanticweb.owlapi.model.*.containsAxiom(org.semanticweb.owlapi.model.OWLAxiom, *, *))")
	void containsAx3() {}
	
	// arguments: (OWLAxiom, Imports, AxiomAnnotations)
	/**
	 * quantifies over calls to methods of type containsAxiom(arg1, arg2, arg3)
	 * provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * having three arguments where arg1 is of type OWLAxiom and arg2 is of type OWLOntology
	 */
	@Pointcut("call(public boolean org.semanticweb.owlapi.search.EntitySearcher.containsAxiom(org.semanticweb.owlapi.model.OWLAxiom, org.semanticweb.owlapi.model.OWLOntology, *))")
	void containsAx3ES() {}
	
	// EntitySearcher method, arguments: (OWLAxiom, Iterable<OWLOntology>, any type)
	/**
	 * quantifies over calls to methods of type containsAxiom(OWLAxiom, Iterable, any type)
	 * provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * having three arguments specifying the axiom whose containment in which ontologies is to be checked
	 */
	@Pointcut("call(public boolean org.semanticweb.owlapi.search.EntitySearcher.containsAxiom(org.semanticweb.owlapi.model.OWLAxiom, java.lang.Iterable<org.semanticweb.owlapi.model.OWLOntology>, *))")
	void containsAx3ESIterable() {}
	
	// USED POINTCUTS and ADVICES ============================================================
	
	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1() && target(ontology) && args(axiom) && @within(annotation)") 
	public Object aroundContainsAx1WithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1Arg(ontology, annotation, axiom);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1() && target(ontology) && args(axiom) && @within(annotation)") 
	public Object aroundContainsAx1WithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1Arg(ontology, annotation, axiom);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1() && target(ontology) && args(axiom) && @withincode(annotation)") 
	public Object aroundContainsAx1WithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1Arg(ontology, annotation, axiom);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1() && target(ontology) && args(axiom) && @withincode(annotation)") 
	public Object aroundContainsAx1WithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1Arg(ontology, annotation, axiom);
	}

	// 3 args
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations) 
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations), 
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
				OWLAxiom
	 * @param imports
	 * 			Imports (specifies whether to consider imports closure)
	 * @param axiomAnnotations
	 * 			AxiomAnnotations (specifies whether to consider axiom annotations)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3() && args(axiom, imports, axiomAnnotations) && target(ontology) && @within(annotation)") 
	public Object aroundContainsAx3WithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLAxiom axiom, Imports imports, AxiomAnnotations axiomAnnotations, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiom3Args(axiom, imports, axiomAnnotations, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations) 
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations), 
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
				OWLAxiom
	 * @param imports
	 * 			Imports (specifies whether to consider imports closure)
	 * @param axiomAnnotations
	 * 			AxiomAnnotations (specifies whether to consider axiom annotations)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3() && args(axiom, imports, axiomAnnotations) && target(ontology) && @within(annotation)") 
	public Object aroundContainsAx3WithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLAxiom axiom, Imports imports, AxiomAnnotations axiomAnnotations, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiom3Args(axiom, imports, axiomAnnotations, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations) 
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations), 
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
				OWLAxiom
	 * @param imports
	 * 			Imports (specifies whether to consider imports closure)
	 * @param axiomAnnotations
	 * 			AxiomAnnotations (specifies whether to consider axiom annotations)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3() && args(axiom, imports, axiomAnnotations) && target(ontology) && @withincode(annotation)") 
	public Object aroundContainsAx3WithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLAxiom axiom, Imports imports, AxiomAnnotations axiomAnnotations, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiom3Args(axiom, imports, axiomAnnotations, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations) 
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations), 
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
				OWLAxiom
	 * @param imports
	 * 			Imports (specifies whether to consider imports closure)
	 * @param axiomAnnotations
	 * 			AxiomAnnotations (specifies whether to consider axiom annotations)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3() && args(axiom, imports, axiomAnnotations) && target(ontology) && @withincode(annotation)") 
	public Object aroundContainsAx3WithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLAxiom axiom, Imports imports, AxiomAnnotations axiomAnnotations, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiom3Args(axiom, imports, axiomAnnotations, ontology, annotation);
	}

	// method provided by EntitySearcher, having arguments (OWLAxiom, OWLOntology, boolean)
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, OWLOntology, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *		OWLAxiom
	 * @param ontology
	 * 			Ontology			
	 * @param imports
	 * 			specifies whether to consider imports
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ES() && args(axiom, ontology, imports) && @within(annotation)") 
	public Object aroundContainsAx3ESWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLOntology ontology, boolean imports, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiomES3Args(axiom, ontology, imports, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, OWLOntology, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *		OWLAxiom
	 * @param ontology
	 * 			Ontology			
	 * @param imports
	 * 			specifies whether to consider imports
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ES() && args(axiom, ontology, imports) && @within(annotation)") 
	public Object aroundContainsAx3ESWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLOntology ontology, boolean imports, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiomES3Args(axiom, ontology, imports, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, OWLOntology, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *		OWLAxiom
	 * @param ontology
	 * 			Ontology			
	 * @param imports
	 * 			specifies whether to consider imports
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ES() && args(axiom, ontology, imports) && @withincode(annotation)") 
	public Object aroundContainsAx3ESWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLOntology ontology, boolean imports, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiomES3Args(axiom, ontology, imports, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, OWLOntology, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *		OWLAxiom
	 * @param ontology
	 * 			Ontology			
	 * @param imports
	 * 			specifies whether to consider imports
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ES() && args(axiom, ontology, imports) && @withincode(annotation)") 
	public Object aroundContainsAx3ESWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLOntology ontology, boolean imports, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiomES3Args(axiom, ontology, imports, annotation);
	}
	

	// EntitySearcher method, arguments: (OWLAxiom, Iterable<OWLOntology>, boolean)
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, Iterable, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *			OWLAxiom
	 * @param ontologies
	 * 			ontologies to be considered (Iterable)			
	 * @param imports
	 * 			specifies whether to consider imports (boolean)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ESIterable() && args(axiom, ontologies, imports) && @within(annotation)") 
	public Object aroundContainsAx3ESIterableWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, Iterable<OWLOntology> ontologies, boolean imports, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiomESIterable3Args(axiom, ontologies, imports, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, Iterable, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *			OWLAxiom
	 * @param ontologies
	 * 			ontologies to be considered (Iterable)			
	 * @param imports
	 * 			specifies whether to consider imports (boolean)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ESIterable() && args(axiom, ontologies, imports) && @within(annotation)") 
	public Object aroundContainsAx3ESIterableWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, Iterable<OWLOntology> ontologies, boolean imports, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiomESIterable3Args(axiom, ontologies, imports, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, Iterable, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *			OWLAxiom
	 * @param ontologies
	 * 			ontologies to be considered (Iterable)			
	 * @param imports
	 * 			specifies whether to consider imports (boolean)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ESIterable() && args(axiom, ontologies, imports) && @withincode(annotation)") 
	public Object aroundContainsAx3ESIterableWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, Iterable<OWLOntology> ontologies, boolean imports, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiomESIterable3Args(axiom, ontologies, imports, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call 
	 * to a method of type containsAxiom(OWLAxiom, Iterable, boolean), 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *			OWLAxiom
	 * @param ontologies
	 * 			ontologies to be considered (Iterable)			
	 * @param imports
	 * 			specifies whether to consider imports (boolean)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx3ESIterable() && args(axiom, ontologies, imports) && @withincode(annotation)") 
	public Object aroundContainsAx3ESIterableWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, Iterable<OWLOntology> ontologies, boolean imports, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiomESIterable3Args(axiom, ontologies, imports, annotation);
	}
	
	// ------------------------------- HELPER  --------------------------------
	
	/**
	 * Responsible for handling result of the call to a method returning info about containment of this axiom 
	 * if this method has one argument of type OWLAxiom, and 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom
	 * @return
	 * 			true, if this axiom is contained and has current aspects, 
	 * 			otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleContainsAxiom1Arg(OWLOntology ontology, Annotation annotation, OWLAxiom axiom) {	
		return HelperFacade.containsAxiom1(ontology, axiom, annotation);
	}
	
	/**
	 * Responsible for handling result of the call to a method returning info about containment of this axiom 
	 * if this method is provided by types from org.semanticweb.owlapi.model package, and 
	 * if this method has three arguments, and 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param axiom
	 * 			OWLAxiom
	 * @param imports
	 * 			Imports (whether to consider)
	 * @param axiomAnnotations
	 * 			AxiomAnnotations (whether to consider)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, 
	 * 			otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleContainsAxiom3Args(OWLAxiom axiom, Imports imports,
			AxiomAnnotations axiomAnnotations, OWLOntology ontology,
			Annotation annotation) {
		return HelperFacade.containsAxiom3(axiom, imports, axiomAnnotations, ontology, annotation);
	}
	
	/**
	 * Responsible for handling result of the call to a method returning info 
	 * about containment of this axiom in this one ontology
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher, and 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param axiom
	 * 			OWLAxiom
	 * @param ontology
	 * 			Ontology
	 * @param imports
	 * 			boolean (whether to consider imports)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, 
	 * 			otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleContainsAxiomES3Args(OWLAxiom axiom, OWLOntology ontology,
			boolean imports, Annotation annotation) {
		return HelperFacade.containsAxiomES3Args(axiom, ontology, imports, annotation);
	}
	
	/**
	 * Responsible for handling result of the call to a method returning info 
	 * about containment of this axiom in one of these ontologies, 
	 * if this method is provided by org.semanticweb.owlapi.search.EntitySearcher, and 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd} or {@link OWLAspectOr}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param axiom
	 * 			OWLAxiom
	 * @param ontologies
	 * 			ontologies to be checked (Iterable)
	 * @param imports
	 * 			boolean (whether to consider imports)
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, 
	 * 			otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleContainsAxiomESIterable3Args(OWLAxiom axiom, Iterable<OWLOntology> ontologies,
			boolean imports, Annotation annotation) {
		return HelperFacade.containsAxiomESIterable3Args(axiom, ontologies, imports, annotation);
	}

}
