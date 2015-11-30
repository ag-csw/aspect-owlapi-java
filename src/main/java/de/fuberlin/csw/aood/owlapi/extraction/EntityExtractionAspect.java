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

import de.fuberlin.csw.aood.owlapi.OWLAspectSparql;
import de.fuberlin.csw.aood.owlapi.util.QueryExecutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;

/**
 * This Aspect implements concern "extraction of entities related to owl-aspects"
 */
@Aspect
public class EntityExtractionAspect {
	
	// Pointcuts for OWL API Methods =========================================================


	// from org.semanticweb.owlapi.model.OWLOntology -------------------------------------


    /**
	 * quantifies over calls to methods related to extracting signature
	 * available via org.semanticweb.owlapi.model.OWLOntology
	 */
	@Pointcut("call(public java.util.Set<org.semanticweb.owlapi.model.OWLEntity+> org.semanticweb.owlapi.model.*.get*Signature(..))")
	void getSignatureRelated() {}


		
	
	// USED POINTCUTS and ADVICES ============================================================
	
	// for getSignatureRelated() -------------------------------------
	
	/**
	 * advice responsible for handling result of the call to a method extracting signature, 
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getSignatureRelated() && target(ontology) && @within(annotation)")
	public Object aroundGetSignatureWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
		return handleEntities(pjp, ontology, annotation);	
	}
	
	/**
	 * advice responsible for handling result of the call to a method extracting signature, 
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getSignatureRelated() && target(ontology) && @within(annotation)")
	public Object aroundGetSignatureWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
		return handleEntities(pjp, ontology, annotation);	
	}

	/**
	 * advice responsible for handling result of the call to a method extracting signature,
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and
	 * if this method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getSignatureRelated() && target(ontology) && @within(annotation)")
	public Object aroundGetSignatureWithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable  {
		return handleEntities(pjp, ontology, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method extracting signature, 
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getSignatureRelated() && target(ontology) && @withincode(annotation)")
	public Object aroundGetSignatureWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
		return handleEntities(pjp, ontology, annotation);	
	}
	
	/**
	 * advice responsible for handling result of the call to a method extracting signature, 
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getSignatureRelated() && target(ontology) && @withincode(annotation)")
	public Object aroundGetSignatureWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
		return handleEntities(pjp, ontology, annotation);
	}

	/**
	 * advice responsible for handling result of the call to a method extracting signature,
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getSignatureRelated() && target(ontology) && @withincode(annotation)")
	public Object aroundGetSignatureWithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable  {
		return handleEntities(pjp, ontology, annotation);
	}
	
	// ------------------------ HELPER ------------------------
	
	
	/**
	 * Responsible for handling result of the call to a method extracting signature (set of entities), 
	 * if this method is available via org.semanticweb.owlapi.model.OWLOntology, and 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql}  specifying current aspects
	 * @return
	 * 			Set of entities whose declaration axioms have current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleEntities(ProceedingJoinPoint pjp, OWLOntology ontology, Annotation annotation) throws Throwable {

		if (annotation instanceof OWLAspectSparql) {

			QueryExecutor qex = new QueryExecutor();
			OWLOntology filteredOnto = qex.getOntologyModule(((OWLAspectSparql) annotation).value().toString(), ontology);

			MethodSignature signature = (MethodSignature) pjp.getSignature();

			java.lang.reflect.Method method = signature.getMethod();

			Set<OWLAxiom> result = null;

			switch (pjp.getArgs().length){
				case 0: result = (Set<OWLAxiom>) method.invoke(filteredOnto);
					break;
				case 1: result = (Set<OWLAxiom>) method.invoke(filteredOnto, pjp.getArgs()[0]);
					break;
				case 2: result = (Set<OWLAxiom>) method.invoke(filteredOnto, pjp.getArgs()[0], pjp.getArgs()[1]);
					break;
				case 3: result = (Set<OWLAxiom>) method.invoke(filteredOnto, pjp.getArgs()[0], pjp.getArgs()[1], pjp.getArgs()[2]);
					break;
				case 4: result = (Set<OWLAxiom>) method.invoke(filteredOnto, pjp.getArgs()[0], pjp.getArgs()[1], pjp.getArgs()[2], pjp.getArgs()[4]);
					break;

			}

			return result;


		} else {
			@SuppressWarnings("unchecked")
			Set<OWLEntity> entities = (Set<OWLEntity>) pjp.proceed();
			return HelperFacade.filterEntities(ontology, entities, annotation);
		}


	}
	
	
//	// Test
//	// This does not work yet. 
//	@Pointcut("call(public java.util.Set<org.semanticweb.owlapi.model.HasSignature+> org.semanticweb.owlapi.model.*.getNested*(..))")
//	void getTst() {}
//	
//	@Around("getTst() && target(onto) && @within(annotation)")
//	public Object aroundGetTst(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
//		System.out.println(pjp.getSignature().toString());
//		return handleCE(pjp, onto, annotation);	
//	}
//	private Object handleCE(ProceedingJoinPoint pjp, OWLOntology ontology, Annotation annotation) throws Throwable {
//		@SuppressWarnings("unchecked")
//		Set<OWLClassExpression> ces = (Set<OWLClassExpression>) pjp.proceed();
//		System.out.println("Got yourself " + ces.size() + " class expressions, eh? Well, let me steal one ;)");
//		Set<OWLClassExpression> result = new HashSet<OWLClassExpression>();
//		for (OWLClassExpression ce : ces) {
//			System.out.println(ce.getSignature().toString());
//			System.out.println(ce.getAnnotationPropertiesInSignature().size());			
//		}
//		return result;
//	}
	
	// -------------------------------------------------------
	
	// we have commented our first code out since we have noticed 
	// that our initial idea does not work and needs refactoring 
	// for which we did not have time, unfortunately.
	// The initial idea was to filter the resulting collection of entities.
	// But actually we have to make different filtering for every concrete method
	// one step earlier: during the examining corresponding axioms. 
	//	(similarly like we did with multimaps)
	// But still the initial code might be useful for documentation purposes, 
	//	or for reuse, so we left it here.

//	// for OWLObject Collections -------------------------------------
//	
//	@Around("(getObjectColl() && !getAxiomColl()) && args(*, onto, ..) && @within(annotation)")
//	public Object aroundObjectCollWithinAnd(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
//		Object result = handleObjectCollection(pjp, Collections.singleton(onto), annotation);
//		return result;
//	}
//	
//	@Around("(getObjectColl() && !getAxiomColl()) && args(*, onto, ..) && @within(annotation)")
//	public Object aroundObjectCollWithinOr(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectOr annotation) throws Throwable  {
//		Object result = handleObjectCollection(pjp, Collections.singleton(onto), annotation);
//		return result;
//	}
//	
//	@Around("(getObjectColl() && !getAxiomColl()) && args(*, onto, ..) && @withincode(annotation)")
//	public Object aroundObjectCollWithinCodeAnd(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectAnd annotation) throws Throwable  {
//		Object result = handleObjectCollection(pjp, Collections.singleton(onto), annotation);
//		return result;
//	}
//	
//	@Around("(getObjectColl() && !getAxiomColl()) && args(*, onto, ..) && @withincode(annotation)")
//	public Object aroundObjectCollWithinCodeOr(ProceedingJoinPoint pjp, OWLOntology onto, OWLAspectOr annotation) throws Throwable  {
//		Object result = handleObjectCollection(pjp, Collections.singleton(onto), annotation);
//		return result;
//	}
//	
//	// for OWLObject Collections from ontologies -------------------------------------
//	
//	@Around("(getObjectCollFromOntologies() && !getAxiomColl()) && args(*, ontologies, ..) && @within(annotation)")
//	public Object aroundObjectCollFromOntologiesWithinAnd(ProceedingJoinPoint pjp, 
//			Iterable<OWLOntology> ontologies, OWLAspectAnd annotation) throws Throwable  {
//		System.out.println(pjp.getSignature().toString());
//		return handleObjectCollection(pjp, ontologies, annotation);
//	}
//	
//	@Around("(getObjectCollFromOntologies() && !getAxiomColl()) && args(*, ontologies, ..) && @within(annotation)")
//	public Object aroundObjectCollFromOntologiesWithinOr(ProceedingJoinPoint pjp, 
//			Iterable<OWLOntology> ontologies, OWLAspectOr annotation) throws Throwable  {
//		System.out.println(pjp.getSignature().toString());
//		return handleObjectCollection(pjp, ontologies, annotation);
//	}
//	
//	@Around("(getObjectCollFromOntologies() && !getAxiomColl()) && args(*, ontologies, ..) && @withincode(annotation)")
//	public Object aroundObjectCollFromOntologiesWithinCodeAnd(ProceedingJoinPoint pjp, 
//			Iterable<OWLOntology> ontologies, OWLAspectAnd annotation) throws Throwable  {
//		System.out.println(pjp.getSignature().toString());
//		return handleObjectCollection(pjp, ontologies, annotation);
//	}
//	
//	@Around("(getObjectCollFromOntologies() && !getAxiomColl()) && args(*, ontologies, ..) && @withincode(annotation)")
//	public Object aroundObjectCollFromOntologiesWithinCodeOr(ProceedingJoinPoint pjp, 
//			Iterable<OWLOntology> ontologies, OWLAspectOr annotation) throws Throwable  {
//		System.out.println(pjp.getSignature().toString());
//		return handleObjectCollection(pjp, ontologies, annotation);
//	}
//
// 	------------------------ HELPER ------------------------
//	private Object handleObjectCollection(ProceedingJoinPoint pjp, 
//			Iterable<OWLOntology> ontologies, Annotation annotation) throws Throwable {
//		@SuppressWarnings("unchecked")
//		Collection<OWLEntity> objColl = (Collection<OWLEntity>) pjp.proceed();
//		Set<OWLEntity> entSet = new HashSet<OWLEntity>(objColl); // copy of objColl as set
//		return HelperFacade.filterEntities(ontologies, entSet, annotation);
//	}
	
}
