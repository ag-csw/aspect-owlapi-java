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
import java.util.Collection;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.CollectionFactory;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;

/**
 * This Aspect implements concern "extraction of an ontology module defined by current aspects"
 */
@Aspect
public class AxiomExtractionAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	// Group 1 -------------------------------
	
	/*
	 * Quantified by getAxiomzz() : Examples
	 * 
	set = onto.getABoxAxioms(Imports.EXCLUDED);
	set = onto.getRBoxAxioms(Imports.INCLUDED);
	set = onto.getTBoxAxioms(Imports.INCLUDED);
	set = onto.getAnnotationAssertionAxioms(entity.getIRI());
	set = onto.getAnnotationPropertyDomainAxioms(owlAnnotationProperty);
	set = onto.getAnnotationPropertyRangeAxioms(owlAnnotationProperty);
	set = onto.getAsymmetricObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getAxioms();
	set = onto.getAxioms(AxiomType.SUBCLASS_OF); // or another type
	set = onto.getAxioms(Imports.EXCLUDED);
	set = onto.getAxioms(AxiomType.SUBCLASS_OF, Imports.EXCLUDED); // or another type
	set = onto.getAxioms(owlAnnotationProperty, Imports.EXCLUDED);
	set = onto.getAxioms(owlClass, Imports.EXCLUDED);
	set = onto.getAxioms(owlDataProperty, Imports.EXCLUDED);
	set = onto.getAxioms(owlDatatype, Imports.EXCLUDED);
	set = onto.getAxioms(owlIndividual, Imports.EXCLUDED);
	set = onto.getAxioms(owlObjectPropertyExpression, Imports.EXCLUDED);
	set = onto.getAxioms(type, entity, Imports.EXCLUDED, forSubPosition);
	set = onto.getAxioms(OWLSubClassOfAxiom.class, explicitClass, entity, Imports.EXCLUDED, forSubPosition);
	set = onto.getClassAssertionAxioms(owlClassExpression);
	set = onto.getClassAssertionAxioms(owlIndividual);
	set = onto.getDataPropertyAssertionAxioms(owlIndividual);
	set = onto.getDataPropertyDomainAxioms(owlDataProperty);
	set = onto.getDataPropertyRangeAxioms(owlDataProperty);
	set = onto.getDeclarationAxioms(owlClass);
	set = onto.getDifferentIndividualAxioms(owlIndividual);
	set = onto.getDisjointClassesAxioms(owlClass);
	set = onto.getDisjointDataPropertiesAxioms(owlDataProperty);
	set = onto.getDisjointObjectPropertiesAxioms(owlObjectPropertyExpression);
	set = onto.getDisjointUnionAxioms(owlClass);
	set = onto.getEquivalentClassesAxioms(owlClass);
	set = onto.getEquivalentDataPropertiesAxioms(owlDataProperty);
	set = onto.getEquivalentObjectPropertiesAxioms(owlObjectPropertyExpression);
	set = onto.getFunctionalDataPropertyAxioms(owlDataProperty);
	set = onto.getFunctionalObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getGeneralClassAxioms();
	set = onto.getHasKeyAxioms(owlClass);
	set = onto.getInverseFunctionalObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getInverseObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getIrreflexiveObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getLogicalAxioms();
	set = onto.getLogicalAxioms(Imports.EXCLUDED);
	set = onto.getNegativeDataPropertyAssertionAxioms(owlIndividual);
	set = onto.getNegativeObjectPropertyAssertionAxioms(owlIndividual);
	set = onto.getObjectPropertyAssertionAxioms(owlIndividual);
	set = onto.getObjectPropertyDomainAxioms(owlObjectPropertyExpression);
	set = onto.getObjectPropertyRangeAxioms(owlObjectPropertyExpression);
	set = onto.getReferencingAxioms(owlEntity);
	set = onto.getReferencingAxioms(owlEntity, Imports.EXCLUDED);
	set = onto.getReflexiveObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getSameIndividualAxioms(owlIndividual);
	set = onto.getSubAnnotationPropertyOfAxioms(owlAnnotationProperty);
	set = onto.getSymmetricObjectPropertyAxioms(owlObjectPropertyExpression);
	set = onto.getTransitiveObjectPropertyAxioms(owlObjectPropertyExpression);
	 * 
	set = onto.getDataSubPropertyAxiomsForSubProperty(owlDataProperty);
	set = onto.getDataSubPropertyAxiomsForSuperProperty(owlDataPropertyExpression);
	set = onto.getObjectSubPropertyAxiomsForSubProperty(owlObjectPropertyExpression);
	set = onto.getObjectSubPropertyAxiomsForSuperProperty(owlObjectPropertyExpression);
	set = onto.getSubClassAxiomsForSubClass(owlClass);
	set = onto.getSubClassAxiomsForSuperClass(owlClass);
	 * 
	set = onto.getAxiomsIgnoreAnnotations(axiom);
	set = onto.getAxiomsIgnoreAnnotations(axiom, Imports.EXCLUDED);	
	 */
	/**
	 * quantifies over calls to different types of methods extracting axioms, 
	 * where the method name begins with "get", and contains substring "Axioms" in the rest of the method name 
	 * <p>( e.g. ontology.getSubClassAxiomsForSubClass(owlClass); )
	 */
	@Pointcut("call(public java.util.Set<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.model.*.get*Axioms*(..))")
	void getAxiomzz() {}
	
	/*
	 * Advised by aroundGetDatatypeDefinitions() : Examples
	 * 
	 * set = onto.getDatatypeDefinitions(owlDatatype);
	 */
	/**
	 * quantifies over calls to axiom extraction method of type getDatatypeDefinitions() 
	 * <p>( e.g. ontology.getDatatypeDefinitions(owlDatatype); )
	 */
	@Pointcut("call(public java.util.Set<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.model.*.getDatatypeDefinitions(..))")
	void getDatatypeDef() {}
	
	/*
	 * Advised by aroundFilterAxioms() : Examples
	 * 
	 collectionOfAxioms = onto.filterAxioms(filter, key, Imports.EXCLUDED);
	 */
	/**
	 * quantifies over calls to axiom extraction methods of type filterAxioms() 
	 * <p>( e.g. ontology.filterAxioms(filter, key, Imports.EXCLUDED); )
	 */
	@Pointcut("call(public java.util.Collection<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.model.*.filterAxioms(..))")
	void filterAxiomzz() {}
	
	// combines the above pointcuts
	
	/**
	 * quantifies over calls to different types of axiom extraction methods 
	 * provided by types from org.semanticweb.owlapi.model package.
	 * (usually inherited by OWLOntology interface)
	 */
	@Pointcut("getAxiomzz() || getDatatypeDef() || filterAxiomzz()")
	void getAxiomsDifferentTypes() {}
	
	// Group 2 -------------------------------------
	
	// e.g.Collection<? extends OWLAxiom> tstAxioms = EntitySearcher.getAxiomsIgnoreAnnotations(ax, onto, false);
	/**
	 * quantifies over calls to EntitySearcher methods of type getAxiomsIgnoreAnnotations 
	 * <p>( e.g. EntitySearcher.getAxiomsIgnoreAnnotations(ax, onto, false); )
	 */	
	@Pointcut("call(public java.util.Collection<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.search.EntitySearcher.getAxiomsIgnoreAnnotations(..))")
	void getEntitySearcherAxsIgnoreAnnos() {}
	
//	Z.B. Collection<? extends OWLAxiom> tstAxioms = EntitySearcher.getAnnotationAssertionAxioms(michelle, onto);
	/**
	 * quantifies over calls to EntitySearcher methods of type getAnnotationAssertionAxioms() 
	 * <p>( e.g. EntitySearcher.getAnnotationAssertionAxioms(namedInd, onto); )
	 */	
	@Pointcut("call(public java.util.Collection<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.search.EntitySearcher.getAnnotationAssertionAxioms(..))")
	void getEntitySearcherAnnoAssertAxs() {}
	
	/**
	 * quantifies over calls to EntitySearcher methods of type getReferencingAxioms() 
	 * <p>( e.g. EntitySearcher.getReferencingAxioms(entity, onto); )
	 */	
	@Pointcut("call(public java.util.Collection<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.search.EntitySearcher.getReferencingAxioms(..))")
	void getEntitySearcherReferencingAxs() {}
	
	// combined
	/**
	 * quantifies over calls to different types of axiom extraction methods 
	 * provided by org.semanticweb.owlapi.search.EntitySearcher.
	 */
	@Pointcut("getEntitySearcherAxsIgnoreAnnos() || getEntitySearcherReferencingAxs() || getEntitySearcherAnnoAssertAxs()")
	void getEntitySearcherAxs() {}
	
	
	
	
	// USED POINTCUTS and ADVICES ============================================================
	
	// Group 1 -------------------------------
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * mostly available from OWLOntology interface
	 * if such method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("(getAxiomsDifferentTypes() || filterAxiomzz()) && target(ontology) && @within(annotation)")
	public Object aroundGetAxiomsWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * mostly available from OWLOntology interface
	 * if such method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxiomsDifferentTypes() && target(ontology) && @within(annotation)")
	public Object aroundGetAxiomsWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * mostly available from OWLOntology interface
	 * if such method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxiomsDifferentTypes() && target(ontology) && @withincode(annotation)")
	public Object aroundGetAxiomsWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * mostly available from OWLOntology interface
	 * if such method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxiomsDifferentTypes() && target(ontology) && @withincode(annotation)")
	public Object aroundGetAxiomsWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}

	// Group 2 -------------------------------
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * provided by org.semanticweb.owlapi.search.EntitySearcher, 
	 * if such method was called from a class annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getEntitySearcherAxs() && args(*, ontology, ..) && @within(annotation)")
	public Object aroundEntitySearcherAxsWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * provided by org.semanticweb.owlapi.search.EntitySearcher, 
	 * if such method was called from a class annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getEntitySearcherAxs() && args(*, ontology, ..) && @within(annotation)")
	public Object aroundEntitySearcherAxsWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}

	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * provided by org.semanticweb.owlapi.search.EntitySearcher, 
	 * if such method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getEntitySearcherAxs() && args(*, ontology, ..) && @withincode(annotation)")
	public Object aroundEntitySearcherAxsInsideAMethodMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}
	
	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods 
	 * provided by org.semanticweb.owlapi.search.EntitySearcher, 
	 * if such method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getEntitySearcherAxs() && args(*, ontology, ..) && @withincode(annotation)")
	public Object aroundEntitySearcherAxsInsideAMethod(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);	
	}
	
	// ------------------------ HELPER ------------------------

	/**
	 * Responsible for handling result of the call to a method extracting owl axioms 
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
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleAxioms(ProceedingJoinPoint pjp, OWLOntology ontology, Annotation annotation) throws Throwable {
		@SuppressWarnings("unchecked")
		Collection<OWLAxiom> axColl = (Collection<OWLAxiom>) pjp.proceed();
		Set<OWLAxiom> axSet = CollectionFactory.createSet(axColl);
		return HelperFacade.filterAxioms(ontology, axSet, annotation);
	}

}
