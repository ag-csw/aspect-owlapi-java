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
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * This Aspect implements concern "containment information about axioms associated with owl-aspects"
 */
@Aspect
public class ContainmentAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	// arguments: (OWLAxiom)
	/**
	 * quantifies over calls to methods of type containsAxiom(axiom) and containsAxiomIgnoreAnnotations(axiom)
	 * provided by types from org.semanticweb.owlapi.model package
	 * and having one argument of type OWLAxiom
	 */
	@Pointcut("call(public boolean org.semanticweb.owlapi.model.*.containsAxiom(org.semanticweb.owlapi.model.OWLAxiom))")
	void containsAx1() {}
	
	// arguments: (OWLAxiom, includeImports)
	/**
	 * quantifies over calls to methods of type containsAxiom(arg1, arg2) and containsAxiomIgnoreAnnotations(arg1, arg2)
	 * provided by types from org.semanticweb.owlapi.model package and 
	 * having two arguments where first argument is of type OWLAxiom and the second argument is of type boolean
	 */
	@Pointcut("call(public boolean org.semanticweb.owlapi.model.*.containsAxiom(org.semanticweb.owlapi.model.OWLAxiom, boolean))")
	void containsAxWithImportFlag() {}



    // arguments: (OWLAxiom)
    /**
     * quantifies over calls to methods of type containsAxiom(axiom) and containsAxiomIgnoreAnnotations(axiom)
     * provided by types from org.semanticweb.owlapi.model package
     * and having one argument of type OWLAxiom
     */
    @Pointcut("call(public boolean org.semanticweb.owlapi.model.*.containsAxiomIgnoreAnnotations(org.semanticweb.owlapi.model.OWLAxiom))")
    void containsAx1IgnoreAnnotations() {}

    // arguments: (OWLAxiom, OWLAxiom, includeImports)
    /**
     * quantifies over calls to methods of type containsAxiom(arg1, arg2) and containsAxiomIgnoreAnnotations(arg1, arg2)
     * provided by types from org.semanticweb.owlapi.model package and
     * having two arguments where first argument is of type OWLAxiom and the second argument is of type boolean
     */
    @Pointcut("call(public boolean org.semanticweb.owlapi.model.*.containsAxiomIgnoreAnnotations(org.semanticweb.owlapi.model.OWLAxiom, *))")
    void containsAxWithImportFlagIgnoreAnnotations() {}





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
	 * if this method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1() && target(ontology) && args(axiom) && @within(annotation)")
	public Object aroundContainsAx1WithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation, OWLAxiom axiom) throws Throwable {
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

	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1() && target(ontology) && args(axiom) && @withincode(annotation)")
	public Object aroundContainsAx1WithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1Arg(ontology, annotation, axiom);
	}

	// 2 args
	
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
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
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
	@Around("containsAxWithImportFlag() && args(axiom, includeImports) && target(ontology) && @within(annotation)")
	public Object aroundContainsAxWithImportFlagWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiom2Args(axiom, includeImports, ontology, annotation);     // Todo :: handler method is responsable to call method with or without Annotations (pjp)
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
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
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
	@Around("containsAxWithImportFlag() && args(axiom, includeImports) && target(ontology) && @within(annotation)")
	public Object aroundContainsAxWithImportFlagWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiom2Args(axiom, includeImports, ontology, annotation);
	}

	/**
	 * advice responsible for handling result of the call
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations)
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations),
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	OWLAxiom
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAxWithImportFlag() && args(axiom, includeImports) && target(ontology) && @within(annotation)")
	public Object aroundContainsAxWithImportFlagWithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable {
		return handleContainsAxiom2Args(axiom, includeImports, ontology, annotation);
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
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
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
	@Around("containsAxWithImportFlag() && args(axiom, includeImports) && target(ontology) && @withincode(annotation)")
	public Object aroundContainsAxWithImportFlagWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
		return handleContainsAxiom2Args(axiom, includeImports, ontology, annotation);
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
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
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
	@Around("containsAxWithImportFlag() && args(axiom, includeImports) && target(ontology) && @withincode(annotation)")
	public Object aroundContainsAxWithImportFlagWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
		return handleContainsAxiom2Args(axiom, includeImports, ontology, annotation);
	}


	/**
	 * advice responsible for handling result of the call
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations)
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations),
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	OWLAxiom
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAxWithImportFlag() && args(axiom, includeImports) && target(ontology) && @withincode(annotation)")
	public Object aroundContainsAxWithImportFlagWithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable {
		return handleContainsAxiom2Args(axiom, includeImports, ontology, annotation);
	}


    // 1 arg and ignore Annotations

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
    @Around("containsAx1IgnoreAnnotations() && target(ontology) && args(axiom) && @within(annotation)")
    public Object aroundContainsAx1IgnoreAnnotationsWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation, OWLAxiom axiom) throws Throwable {
        return handleContainsAxiom1ArgIgnoreAnnotations(ontology, annotation, axiom);
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
    @Around("containsAx1IgnoreAnnotations() && target(ontology) && args(axiom) && @within(annotation)")
    public Object aroundContainsAx1IgnoreAnnotationsWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation, OWLAxiom axiom) throws Throwable {
        return handleContainsAxiom1ArgIgnoreAnnotations(ontology, annotation, axiom);
    }


	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1IgnoreAnnotations() && target(ontology) && args(axiom) && @within(annotation)")
	public Object aroundContainsAx1IgnoreAnnotationsWithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1ArgIgnoreAnnotations(ontology, annotation, axiom);
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
    @Around("containsAx1IgnoreAnnotations() && target(ontology) && args(axiom) && @withincode(annotation)")
    public Object aroundContainsAx1IgnoreAnnotationsWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectAnd annotation, OWLAxiom axiom) throws Throwable {
        return handleContainsAxiom1ArgIgnoreAnnotations(ontology, annotation, axiom);
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
    @Around("containsAx1IgnoreAnnotations() && target(ontology) && args(axiom) && @withincode(annotation)")
    public Object aroundContainsAx1IgnoreAnnotationsWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectOr annotation, OWLAxiom axiom) throws Throwable {
        return handleContainsAxiom1ArgIgnoreAnnotations(ontology, annotation, axiom);
    }

	/**
	 * advice responsible for handling result of the call to a method of type containsAxiom(axiom)
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAx1IgnoreAnnotations() && target(ontology) && args(axiom) && @withincode(annotation)")
	public Object aroundContainsAx1IgnoreAnnotationsWithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLOntology ontology, OWLAspectSparql annotation, OWLAxiom axiom) throws Throwable {
		return handleContainsAxiom1ArgIgnoreAnnotations(ontology, annotation, axiom);
	}




    // 2 args  +  ignore Annotations

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
     * @param includeImports
     * 			boolean (specifies whether to consider imports closure)
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
    @Around("containsAxWithImportFlagIgnoreAnnotations() && args(axiom, includeImports) && target(ontology) && @within(annotation)")
    public Object aroundContainsAxWithImportFlagIgnoreAnnotationsWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
        return handleContainsAxiom2ArgsIgnoreAnnotations(axiom, includeImports, ontology, annotation);     // Todo :: handler method is responsable to call method with or without Annotations (pjp)
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
     * @param includeImports
     * 			boolean (specifies whether to consider imports closure)
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
    @Around("containsAxWithImportFlagIgnoreAnnotations() && args(axiom, includeImports) && target(ontology) && @within(annotation)")
    public Object aroundContainsAxWithImportFlagIgnoreAnnotationsWithinClassMarkedWithOr(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
        return handleContainsAxiom2ArgsIgnoreAnnotations(axiom, includeImports, ontology, annotation); // Todo :: handler method is responsable to call method with or without Annotations (pjp)
    }

	/**
	 * advice responsible for handling result of the call
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations)
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations),
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a class annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	OWLAxiom
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAxWithImportFlagIgnoreAnnotations() && args(axiom, includeImports) && target(ontology) && @within(annotation)")
	public Object aroundContainsAxWithImportFlagIgnoreAnnotationsWithinClassMarkedWithSparql(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable {
		return handleContainsAxiom2ArgsIgnoreAnnotations(axiom, includeImports, ontology, annotation); // Todo :: handler method is responsable to call method with or without Annotations (pjp)
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
     * @param includeImports
     * 			boolean (specifies whether to consider imports closure)
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
    @Around("containsAxWithImportFlagIgnoreAnnotations() && args(axiom, includeImports) && target(ontology) && @withincode(annotation)")
    public Object aroundContainsAxWithImportFlagsIgnoreAnnotationsWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectAnd annotation) throws Throwable {
        return handleContainsAxiom2ArgsIgnoreAnnotations(axiom, includeImports, ontology, annotation);
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
     *          OWLAxiom
     * @param includeImports
     * 			boolean (specifies whether to consider imports closure)
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
    @Around("containsAxWithImportFlagIgnoreAnnotations() && args(axiom, includeImports) && target(ontology) && @withincode(annotation)")
    public Object aroundContainsAxWithImportFlagIgnoreAnnotationsWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectOr annotation) throws Throwable {
        return handleContainsAxiom2ArgsIgnoreAnnotations(axiom, includeImports, ontology, annotation);
    }


	/**
	 * advice responsible for handling result of the call
	 * to a method of type containsAxiom(axiom, imports, axiomAnnotations)
	 * with three arguments having types (OWLAxiom, Imports, AxiomAnnotations),
	 * if this method is provided by a type from org.semanticweb.owlapi.model package and
	 * if this method was called from a method or constructor annotated with {@link OWLAspectSparql}
	 *
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 *          OWLAxiom
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom whose containment is to be checked
	 * @return
	 * 			true, if this axiom is contained and has current aspects, otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("containsAxWithImportFlagIgnoreAnnotations() && args(axiom, includeImports) && target(ontology) && @withincode(annotation)")
	public Object aroundContainsAxWithImportFlagIgnoreAnnotationsWithinCodeMarkedWithSparql(ProceedingJoinPoint pjp, OWLAxiom axiom, boolean includeImports, OWLOntology ontology, OWLAspectSparql annotation) throws Throwable {
		return handleContainsAxiom2ArgsIgnoreAnnotations(axiom, includeImports, ontology, annotation);
	}



	
	// ------------------------------- HELPER  --------------------------------
	
	/**
	 * Responsible for handling result of the call to a method returning info about containment of this axiom 
	 * if this method has one argument of type OWLAxiom, and 
	 * if this method was called from a context annotated with either {@link OWLAspectAnd}, {@link OWLAspectOr}
	 * or {@link OWLAspectSparql}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql} specifying current aspects
	 * @param axiom
	 * 			OWLAxiom
	 * @return
	 * 			true, if this axiom is contained and has current aspects, 
	 * 			otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleContainsAxiom1Arg(OWLOntology ontology, Annotation annotation, OWLAxiom axiom) throws OWLOntologyCreationException,
			OWLOntologyStorageException {

		// TODO try catch

		if (annotation instanceof OWLAspectSparql){

			QueryExecutor qex = new QueryExecutor();
			OWLOntology filteredOnto = qex.getOntologyModule(((OWLAspectSparql) annotation).value().toString(), ontology);

			return  filteredOnto.containsAxiom(axiom);

		} else {

			return HelperFacade.containsAxiom1(ontology, axiom, annotation);

		}
	}
	
	/**
	 * Responsible for handling result of the call to a method returning info about containment of this axiom 
	 * if this method is provided by types from org.semanticweb.owlapi.model package, and 
	 * if this method has two arguments, and
	 * if this method was called from a context annotated with either {@link OWLAspectAnd}, {@link OWLAspectOr}
	 * or {@link OWLAspectSparql}
	 * The context may be a class, a method or a constructor.
	 * 
	 * @param axiom
	 * 			OWLAxiom
	 * @param includeImports
	 * 			boolean (specifies whether to consider imports closure)
	 * @param ontology
	 * 			Ontology
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql}  specifying current aspects
	 * @return
	 * 			true, if this axiom is contained and has current aspects, 
	 * 			otherwise false.
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	private Object handleContainsAxiom2Args(OWLAxiom axiom, boolean includeImports, OWLOntology ontology,
			Annotation annotation) throws Throwable {

		if (annotation instanceof OWLAspectSparql){

			QueryExecutor qex = new QueryExecutor();
			OWLOntology filteredOnto = qex.getOntologyModule(((OWLAspectSparql) annotation).value().toString(), ontology);

			return  filteredOnto.containsAxiom(axiom, includeImports);

		} else {

			return HelperFacade.containsAxiom2(axiom, includeImports, true, ontology, annotation);
		}
	}





    /**
     * Responsible for handling result of the call to a method returning info about containment of this axiom
     * if this method has one argument of type OWLAxiom, Annoations should be ignored and
     * if this method was called from a context annotated with either {@link OWLAspectAnd}, {@link OWLAspectOr}
	 * or {@link OWLAspectSparql}
	 * The context may be a class, a method or a constructor.
     *
     * @param ontology
     * 			Ontology
     * @param annotation
     * 			Annotation of type {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql}  specifying current aspects
     * @param axiom
     * 			OWLAxiom
     * @return
     * 			true, if this axiom is contained and has current aspects,
     * 			otherwise false.
     * @throws Throwable
     * 			in case something goes wrong
     */
    private Object handleContainsAxiom1ArgIgnoreAnnotations(OWLOntology ontology, Annotation annotation, OWLAxiom axiom)
			throws OWLOntologyCreationException,
			OWLOntologyStorageException{


		if (annotation instanceof OWLAspectSparql){

			QueryExecutor qex = new QueryExecutor();
			OWLOntology filteredOnto = qex.getOntologyModule(((OWLAspectSparql) annotation).value().toString(), ontology);

			return  filteredOnto.containsAxiomIgnoreAnnotations(axiom);

		} else {

			return HelperFacade.containsAxiom2(axiom, true, false, ontology, annotation);
		}
    }

    /**
     * Responsible for handling result of the call to a method returning info about containment of this axiom
     * if this method is provided by types from org.semanticweb.owlapi.model package,Annoations should be ignored and
     * if this method has two arguments, and
     * if this method was called from a context annotated with either {@link OWLAspectAnd}, {@link OWLAspectOr}
	 * or {@link OWLAspectSparql}
	 * The context may be a class, a method or a constructor.
     *
     * @param axiom
     * 			OWLAxiom
     * @param includeImports
     * 			boolean (specifies whether to consider imports closure)
     * @param ontology
     * 			Ontology
     * @param annotation
     * 			Annotation of type {@link OWLAspectAnd}, {@link OWLAspectOr} or {@link OWLAspectSparql}  specifying current aspects
     * @return
     * 			true, if this axiom is contained and has current aspects,
     * 			otherwise false.
     * @throws Throwable
     * 			in case something goes wrong
     */
    private Object handleContainsAxiom2ArgsIgnoreAnnotations(OWLAxiom axiom, boolean includeImports, OWLOntology ontology,
                                            Annotation annotation) throws Throwable {


		if (annotation instanceof OWLAspectSparql){

			QueryExecutor qex = new QueryExecutor();
			OWLOntology filteredOnto = qex.getOntologyModule(((OWLAspectSparql) annotation).value().toString(), ontology);

			return  filteredOnto.containsAxiomIgnoreAnnotations(axiom, includeImports);

		} else {
			return HelperFacade.containsAxiom2(axiom, includeImports, false, ontology, annotation);
		}
    }



}



