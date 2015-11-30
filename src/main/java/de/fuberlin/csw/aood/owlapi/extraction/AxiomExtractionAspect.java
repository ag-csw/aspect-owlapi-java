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
    @Pointcut("call(public java.util.Set<org.semanticweb.owlapi.model.OWLAxiom+> org.semanticweb.owlapi.model.OWLEntity.getReferencingAxioms(org.semanticweb.owlapi.model.OWLOntology))")
    void getReferencingAxiomzz() {}



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

    // obsolete since OWLAPI 3 has no EntitySearcher Class
	
	
	
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
	 * if such method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxiomsDifferentTypes() && target(ontology) && @within(annotation)")
	public Object aroundGetAxiomsWithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable  {
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


	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods
	 * mostly available from OWLOntology interface
	 * if such method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("getAxiomsDifferentTypes() && target(ontology) && @withincode(annotation)")
	public Object aroundGetAxiomsWithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);
	}

	// Group 2 -------------------------------


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
    @Around("(getReferencingAxiomzz()) && args(ontology) && @within(annotation)")
    public Object aroundGetAxiomsForEntityWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
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
    @Around("(getReferencingAxiomzz()) && args(ontology) && @within(annotation)")
    public Object aroundGetAxiomsForEntityWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
        return handleAxioms(pjp, ontology, annotation);
    }


	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods
	 * mostly available from OWLOntology interface
	 * if such method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("(getReferencingAxiomzz()) && args(ontology) && @within(annotation)")
	public Object aroundGetAxiomsForEntityWithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable  {
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
    @Around("(getReferencingAxiomzz()) && args(ontology) && @withincode(annotation)")
    public Object aroundGetAxiomsForEntityWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable  {
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
    @Around("(getReferencingAxiomzz()) && args(ontology) && @withincode(annotation)")
    public Object aroundGetAxiomsForEntityWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation) throws Throwable  {
        return handleAxioms(pjp, ontology, annotation);
    }


	/**
	 * Responsible for handling result of the calls to different types of axiom extraction methods
	 * mostly available from OWLOntology interface
	 * if such method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("(getReferencingAxiomzz()) && args(ontology) && @withincode(annotation)")
	public Object aroundGetAxiomsForEntityWithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable  {
		return handleAxioms(pjp, ontology, annotation);
	}








	// ------------------------ HELPER ------------------------

	/**
	 * Responsible for handling result of the call to a method extracting owl axioms 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd}, {@link OWLAspectOr}
	 * or {@link OWLAspectSparql}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql}  specifying current aspects
	 * @return
	 * 			Set of axioms associated with current aspects
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleAxioms(ProceedingJoinPoint pjp, OWLOntology ontology, Annotation annotation) throws Throwable {

		if(annotation instanceof OWLAspectSparql){

			QueryExecutor qex = new QueryExecutor();

			OWLOntology filteredOnto = qex.getOntologyModule(((OWLAspectSparql) annotation).value().toString(), ontology);

			MethodSignature signature = (MethodSignature) pjp.getSignature();

			java.lang.reflect.Method method = signature.getMethod();

			Set<OWLAxiom> result = null;



			if (pjp.getTarget() instanceof OWLEntity){ 			// OWLEntity as target, filtered ontology as the first argument

				switch (pjp.getArgs().length){
					case 1: result = (Set<OWLAxiom>) method.invoke(pjp.getTarget(), filteredOnto);
						break;
					case 2: result = (Set<OWLAxiom>) method.invoke(pjp.getTarget(), filteredOnto, pjp.getArgs()[1]);
						break;
				}


			} else if (pjp.getTarget() instanceof OWLOntology){   // filtered ontology as target, arguments remain the same

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
			}

			return result;


		} else {
			@SuppressWarnings("unchecked")
			Collection<OWLAxiom> axColl = (Collection<OWLAxiom>) pjp.proceed();
			Set<OWLAxiom> axSet = CollectionFactory.createSet(axColl);
			return HelperFacade.filterAxioms(ontology, axSet, annotation);

		}



	}

}
