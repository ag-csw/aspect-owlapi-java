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
package de.fuberlin.csw.aood.owlapi.modification;

import java.util.List;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import de.fuberlin.csw.aood.owlapi.helpers.HelperFacade;

/**
 * This Aspect implements concern "definition of an ontology module by assigning aspect annotations"
 *
 */
@Aspect
public class OntologyModificationAspect {
	
	// Pointcuts for OWL API Methods =========================================================
	
	// om.applyChange(OWLOntologyChange); // Apply single Change
	/**
	 * quantifies over calls to methods of type applyChange(), 
	 * dealing with single change
	 */
	@Pointcut("call(public java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> org.semanticweb.owlapi.model.*.applyChange(org.semanticweb.owlapi.model.*))")
	void singleChange() {}
	
	// om.addAxiom(onto, ax); // Add single Axiom
	/**
	 * quantifies over calls to methods of type addAxiom(), 
	 * dealing with single Axiom
	 */
	@Pointcut("call(public java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> org.semanticweb.owlapi.model.*.addAxiom(..))")
	void callAddAxiom() {}
	
	// om.removeAxiom(onto, ax); // Remove single Axiom
	/**
	 * quantifies over calls to methods of type removeAxiom(), 
	 * dealing with single Axiom
	 */
	@Pointcut("call(* org.semanticweb.owlapi.model.*.removeAxiom(..))")
	void callRemoveAxiom() {}
	
	// om.applyChanges(List<OWLOntologyChange>); // Apply multiple Changes
	/**
	 * quantifies over calls to methods of type applyChanges(), 
	 * dealing with multiple changes
	 */
	@Pointcut("call(public java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> org.semanticweb.owlapi.model.*.applyChanges(..))")
	void multipleChanges() {}
	
	// om.addAxioms(ont, Set<OWLAxiom>); 		// Add multiple Axioms
	// m.removeAxioms(ont, Set<OWLAxiom>); 		// Remove multiple Axioms
	/**
	 * quantifies over calls to methods of type addAxioms() or removeAxioms(), 
	 * dealing with multiple Axioms
	 */
	@Pointcut("call(public java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> org.semanticweb.owlapi.model.*.*Axioms(..))")
	void addOrRemoveMultipleAxioms() {}
	
	// not needed anymore since handled by addOrRemoveMultipleAxioms()
//	// om.addAxioms(ont, Set<OWLAxiom>); 		// Add multiple Axioms
//	// m.removeAxioms(ont, Set<OWLAxiom>); 		// Remove multiple Axioms
//	@Pointcut("call(public java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> org.semanticweb.owlapi.model.*.*(..))")
//	void removeMultipleAxioms() {} 
	
	// USED POINTCUTS and ADVICES ========================================================
	
	// ------------------------ APPLY CHANGE ------------------------
	
	/**
	 * advice responsible for handling result of the call to a method of type applyChange(), 
	 * dealing with single change, 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.applyChange(OWLOntologyChange);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param change
	 * 			Change to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the change has been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("singleChange() && args(change) && @within(annotation)")
	public Object aroundCallApplyChangeWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntologyChange change, OWLAspectAnd annotation) throws Throwable  {
		if(change.isAxiomChange()) {
			return HelperFacade.handleAxiomChange(change, annotation);
		} else {
			return pjp.proceed();
		}
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type applyChange(), 
	 * dealing with single change, 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.applyChange(OWLOntologyChange);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param change
	 * 			Change to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			ChangeApplied (status whether the change has been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("singleChange() && args(change) && @within(annotation)")
	public Object aroundCallApplyChangeWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntologyChange change, OWLAspectOr annotation) throws Throwable  {
		if(change.isAxiomChange()) {
			return HelperFacade.handleAxiomChange(change, annotation);
		} else {
			return pjp.proceed();
		}
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type applyChange(), 
	 * dealing with single change, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.applyChange(OWLOntologyChange);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param change
	 * 			Change to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the change has been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("singleChange() && args(change) && @withincode(annotation)")
	public Object aroundCallApplyChangeWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, OWLOntologyChange change, OWLAspectAnd annotation) throws Throwable  {
		if(change.isAxiomChange()) { 
			return HelperFacade.handleAxiomChange(change, annotation);
		} else {
			return pjp.proceed();
		}
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type applyChange(), 
	 * dealing with single change, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.applyChange(OWLOntologyChange);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param change
	 * 			Change to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the change has been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("singleChange() && args(change) && @withincode(annotation)")
	public Object aroundCallApplyChangeWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntologyChange change, OWLAspectOr annotation) throws Throwable  {
		if(change.isAxiomChange()) { 
			return HelperFacade.handleAxiomChange(change, annotation);
		} else {
			return pjp.proceed();
		}
	}
	
	// ------------------------ ADD AXIOM ------------------------

	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(), 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.addAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, into which this axiom has to be added
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("callAddAxiom() && args(ontology, axiom) && @within(annotation)")
	public Object aroundCallAddAxiomWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectAnd annotation) throws Throwable {			
		return HelperFacade.handleAxiomChange(new AddAxiom(ontology, axiom), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(), 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.addAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, into which this axiom has to be added
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("callAddAxiom() && args(ontology, axiom) && @within(annotation)")
	public Object aroundCallAddAxiomWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectOr annotation) throws Throwable {			
		return HelperFacade.handleAxiomChange(new AddAxiom(ontology, axiom), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(), 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.addAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, into which this axiom has to be added
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("callAddAxiom() && args(ontology, axiom) && @withincode(annotation)")
	public Object aroundCallAddAxiomWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectAnd annotation) throws Throwable {			
		return HelperFacade.handleAxiomChange(new AddAxiom(ontology, axiom), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(), 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.addAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, into which this axiom has to be added
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			java.util.List<OWLOntologyChange> (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("callAddAxiom() && args(ontology, axiom) && @withincode(annotation)")
	public Object aroundCallAddAxiomWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectOr annotation) throws Throwable {			
		return HelperFacade.handleAxiomChange(new AddAxiom(ontology, axiom), annotation);
	}
		
	// ------------------------ REMOVE AXIOM ------------------------

	/**
	 * advice responsible for handling result of the call to removeAxiom(), 
	 * if it was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.removeAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, from which this axiom has to be removed
	 * @param axiom
	 * 			Axiom to be removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(removing axiom means removing the current aspects from such an axiom in ontology)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callRemoveAxiom() && args(ontology, axiom) && @within(annotation)")
	public Object aroundCallRemoveAxiomWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectAnd annotation) throws Throwable {
		return HelperFacade.handleRemoveAxiomReturnChanges(axiom, ontology, annotation);	
	}
	
	/**
	 * advice responsible for handling result of the call to removeAxiom(), 
	 * if it was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.removeAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, from which this axiom has to be removed
	 * @param axiom
	 * 			Axiom to be removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(removing axiom means removing the current aspects from such an axiom in ontology)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callRemoveAxiom() && args(ontology, axiom) && @within(annotation)")
	public Object aroundCallRemoveAxiomWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectOr annotation) throws Throwable {
		return HelperFacade.handleRemoveAxiomReturnChanges(axiom, ontology, annotation);	
	}
	
	/**
	 * advice responsible for handling result of the call to removeAxiom(), 
	 * if it was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.removeAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, from which this axiom has to be removed
	 * @param axiom
	 * 			Axiom to be removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(removing axiom means removing the current aspects from such an axiom in ontology)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callRemoveAxiom() && args(ontology, axiom) && @withincode(annotation)")
	public Object aroundCallRemoveAxiomWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectAnd annotation) throws Throwable {
		return HelperFacade.handleRemoveAxiomReturnChanges(axiom, ontology, annotation);	
	}
	
	/**
	 * advice responsible for handling result of the call to removeAxiom(), 
	 * if it was called from a method or constructor annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.removeAxiom(ontology, axiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology, from which this axiom has to be removed
	 * @param axiom
	 * 			Axiom to be removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(removing axiom means removing the current aspects from such an axiom in ontology)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callRemoveAxiom() && args(ontology, axiom) && @withincode(annotation)")
	public Object aroundCallRemoveAxiomWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, OWLAxiom axiom, OWLAspectOr annotation) throws Throwable {
		return HelperFacade.handleRemoveAxiomReturnChanges(axiom, ontology, annotation);	
	}
	
	// ------------------------ APPLY CHANGES ------------------------

	/**
	 * advice responsible for handling result of the call to applyChanges(), 
	 * if it was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.applyChanges(List OWLOntologyChange changes);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param changes 
	 * 			Changes to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("multipleChanges() && args(changes) && @within(annotation)") 
	public Object aroundCallApplyChangesWithinClassMarkedWithAnd(
			ProceedingJoinPoint pjp, List<OWLOntologyChange> changes, OWLAspectAnd annotation) throws Throwable {
		return HelperFacade.handleMultipleChanges(changes, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to applyChanges(), 
	 * if it was called from a class annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.applyChanges(List OWLOntologyChange changes);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param changes 
	 * 			Changes to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("multipleChanges() && args(changes) && @within(annotation)") 
	public Object aroundCallApplyChangesWithinClassMarkedWithOr(
			ProceedingJoinPoint pjp, List<OWLOntologyChange> changes, OWLAspectOr annotation) throws Throwable {
		return HelperFacade.handleMultipleChanges(changes, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to applyChanges(), 
	 * if it was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.applyChanges(List OWLOntologyChange changes);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param changes 
	 * 			Changes to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("multipleChanges() && args(changes) && @withincode(annotation)") 
	public Object aroundCallApplyChangesWithinCodeMarkedWithAnd(
			ProceedingJoinPoint pjp, List<OWLOntologyChange> changes, OWLAspectAnd annotation) throws Throwable {
		return HelperFacade.handleMultipleChanges(changes, annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to applyChanges(), 
	 * if it was called from a method or constructor annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.applyChanges(List OWLOntologyChange changes);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param changes 
	 * 			Changes to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("multipleChanges() && args(changes) && @withincode(annotation)") 
	public Object aroundCallApplyChangesWithinCodeMarkedWithOr(
			ProceedingJoinPoint pjp, List<OWLOntologyChange> changes, OWLAspectOr annotation) throws Throwable {
		return HelperFacade.handleMultipleChanges(changes, annotation);
	}
	
	
	// ------------------------ ADD OR REMOVE AXIOMS ------------------------
	
	/**
	 * advice responsible for handling result of the call to addAxioms() or removeAxioms(), 
	 * if it was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.removeAxioms(ont, Set OWLAxiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology to be updated
	 * @param axioms
	 * 			Axioms to be added or removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("addOrRemoveMultipleAxioms() && args(ontology, axioms) && @within(annotation)")
	public Object aroundAddOrRemoveAxiomsWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, Set<OWLAxiom> axioms, OWLAspectAnd annotation) throws Throwable {
		boolean isAddAxioms = pjp.getSignature().getName().equals("addAxioms");
		return HelperFacade.handleAddOrRemoveAxs(ontology, axioms, annotation, isAddAxioms);
	}
	
	/**
	 * advice responsible for handling result of the call to addAxioms() or removeAxioms(), 
	 * if it was called from a class annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.removeAxioms(ont, Set OWLAxiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology to be updated
	 * @param axioms
	 * 			Axioms to be added or removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("addOrRemoveMultipleAxioms() && args(ontology, axioms) && @within(annotation)")
	public Object aroundAddOrRemoveAxiomsWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, Set<OWLAxiom> axioms, OWLAspectOr annotation) throws Throwable {
		boolean isAddAxioms = pjp.getSignature().getName().equals("addAxioms");
		return HelperFacade.handleAddOrRemoveAxs(ontology, axioms, annotation, isAddAxioms);
	}
	
	/**
	 * advice responsible for handling result of the call to addAxioms() or removeAxioms(), 
	 * if it was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. manager.removeAxioms(ont, Set OWLAxiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology to be updated
	 * @param axioms
	 * 			Axioms to be added or removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("addOrRemoveMultipleAxioms() && args(ontology, axioms) && @withincode(annotation)")
	public Object aroundAddOrRemoveAxiomsWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLOntology ontology, Set<OWLAxiom> axioms, OWLAspectAnd annotation) throws Throwable {
		boolean isAddAxioms = pjp.getSignature().getName().equals("addAxioms");
		return HelperFacade.handleAddOrRemoveAxs(ontology, axioms, annotation, isAddAxioms);
	}
	
	/**
	 * advice responsible for handling result of the call to addAxioms() or removeAxioms(), 
	 * if it was called from a method or constructor annotated with {@link OWLAspectOr}
	 * <p>E.g. manager.removeAxioms(ont, Set OWLAxiom);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param ontology
	 * 			Ontology to be updated
	 * @param axioms
	 * 			Axioms to be added or removed
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange : changes which have been applied successfully
	 * 			(given that the current aspect annotations have been changed successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around("addOrRemoveMultipleAxioms() && args(ontology, axioms) && @withincode(annotation)")
	public Object aroundAddOrRemoveAxiomsWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLOntology ontology, Set<OWLAxiom> axioms, OWLAspectOr annotation) throws Throwable {
		boolean isAddAxioms = pjp.getSignature().getName().equals("addAxioms");
		return HelperFacade.handleAddOrRemoveAxs(ontology, axioms, annotation, isAddAxioms);
	}
	
	// ------------------------ ADDITIONAL POINTCUTS ------------------------
	
	// ----------------------- for HasDirectAddAxiom ------------------------
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(axiom), 
	 * (provided by interface HasDirectAddAxiom), which has only single argument of type OWLAxiom, 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. ((HasDirectAddAxiom) onto).addAxiom(ax); 
	 * <p>or ((OWLOntologyImpl) onto).addAxiom(ax);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			ChangeApplied (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callAddAxiom() && args(axiom) && @within(annotation)")
	public Object aroundTypeHasDirectAddAxiomWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLAspectAnd annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAxiomChange(new AddAxiom(onto, axiom), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(axiom), 
	 * (provided by interface HasDirectAddAxiom), which has only single argument of type OWLAxiom, 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * <p>E.g. ((HasDirectAddAxiom) onto).addAxiom(ax); 
	 * <p>or ((OWLOntologyImpl) onto).addAxiom(ax);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			ChangeApplied (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callAddAxiom() && args(axiom) && @within(annotation)")
	public Object aroundTypeHasDirectAddAxiomWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLAspectOr annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAxiomChange(new AddAxiom(onto, axiom), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(axiom), 
	 * (provided by interface HasDirectAddAxiom), which has only single argument of type OWLAxiom, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. ((HasDirectAddAxiom) onto).addAxiom(ax); 
	 * <p>or ((OWLOntologyImpl) onto).addAxiom(ax);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			ChangeApplied (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callAddAxiom() && args(axiom) && @withincode(annotation)")
	public Object aroundTypeHasDirectAddAxiomWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLAspectAnd annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAxiomChange(new AddAxiom(onto, axiom), annotation);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxiom(axiom), 
	 * (provided by interface HasDirectAddAxiom), which has only single argument of type OWLAxiom, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. ((HasDirectAddAxiom) onto).addAxiom(ax); 
	 * <p>or ((OWLOntologyImpl) onto).addAxiom(ax);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axiom
	 * 			Axiom to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			ChangeApplied (status whether the axiom has been added successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="callAddAxiom() && args(axiom) && @withincode(annotation)")
	public Object aroundTypeHasDirectAddAxiomWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			OWLAxiom axiom, OWLAspectOr annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAxiomChange(new AddAxiom(onto, axiom), annotation);
	}
	
	// ----------------------- for HasDirectAddAxioms ------------------------
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxioms(axioms), 
	 * (provided by interface HasDirectAddAxioms), which has only single argument of type Set of OWLAxiom, 
	 * if this method was called from a class annotated with {@link OWLAspectAnd}
	 * <p>E.g. ((OWLMutableOntology) onto).addAxioms(axioms);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axioms
	 * 			Axioms to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange (changes which have been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="addOrRemoveMultipleAxioms() && args(axioms) && @within(annotation)")
	public Object aroundTypeHasDirectAddAxiomsWithinClassMarkedWithAnd(ProceedingJoinPoint pjp, 
			Set<OWLAxiom> axioms, OWLAspectAnd annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAddOrRemoveAxs(onto, axioms, annotation, true);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxioms(axioms), 
	 * (provided by interface HasDirectAddAxioms), which has only single argument of type Set of OWLAxiom, 
	 * if this method was called from a class annotated with {@link OWLAspectOr}
	 * <p>E.g. ((OWLMutableOntology) onto).addAxioms(axioms);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axioms
	 * 			Axioms to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange (changes which have been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="addOrRemoveMultipleAxioms() && args(axioms) && @within(annotation)")
	public Object aroundTypeHasDirectAddAxiomsWithinClassMarkedWithOr(ProceedingJoinPoint pjp, 
			Set<OWLAxiom> axioms, OWLAspectOr annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAddOrRemoveAxs(onto, axioms, annotation, true);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxioms(axioms), 
	 * (provided by interface HasDirectAddAxioms), which has only single argument of type Set of OWLAxiom, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectAnd}
	 * <p>E.g. ((OWLMutableOntology) onto).addAxioms(axioms);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axioms
	 * 			Axioms to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange (changes which have been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="addOrRemoveMultipleAxioms() && args(axioms) && @withincode(annotation)")
	public Object aroundTypeHasDirectAddAxiomsWithinCodeMarkedWithAnd(ProceedingJoinPoint pjp, 
			Set<OWLAxiom> axioms, OWLAspectAnd annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAddOrRemoveAxs(onto, axioms, annotation, true);
	}
	
	/**
	 * advice responsible for handling result of the call to a method of type addAxioms(axioms), 
	 * (provided by interface HasDirectAddAxioms), which has only single argument of type Set of OWLAxiom, 
	 * if this method was called from a method or constructor annotated with {@link OWLAspectOr}
	 * <p>E.g. ((OWLMutableOntology) onto).addAxioms(axioms);
	 * 
	 * @param pjp
	 * 			Proceeding Join Point
	 * @param axioms
	 * 			Axioms to be added
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectOr} specifying current aspects
	 * @return
	 * 			List of OWLOntologyChange (changes which have been applied successfully)
	 * @throws Throwable
	 * 			in case something goes wrong
	 */
	@Around(value="addOrRemoveMultipleAxioms() && args(axioms) && @withincode(annotation)")
	public Object aroundTypeHasDirectAddAxiomsWithinCodeMarkedWithOr(ProceedingJoinPoint pjp, 
			Set<OWLAxiom> axioms, OWLAspectOr annotation) throws Throwable {	
		OWLOntology onto = (OWLOntology) (pjp.getTarget());
		return HelperFacade.handleAddOrRemoveAxs(onto, axioms, annotation, true);
	}

}
