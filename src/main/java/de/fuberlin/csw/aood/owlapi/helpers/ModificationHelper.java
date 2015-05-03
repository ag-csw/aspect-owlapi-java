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
package de.fuberlin.csw.aood.owlapi.helpers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;

import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;

/**
 * This helper provides methods related to 
 * updating current aspect annotations in case of ontology modification
 * and needed by multiple other helpers
 */
public class ModificationHelper extends BasicHelper {
	
	/**
	 * delegates the change to be handled according to the type of the passed annotation specifying current aspects
	 * 
	 * @param change
	 * 			change to be handled
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return ChangeApplied : status telling if the change was applied successfully 
	 * 			(if aspect annotations were updated successfully)
	 */
	public static ChangeApplied handleAxiomChange(OWLOntologyChange change, Annotation annotation) {	
		if (change.isAddAxiom()) {
			return ModificationHelperAdd.handleChangeAddAxiom(change, annotation);
		} else {
			return ModificationHelperRemove.handleChangeRemoveAxiom(change, annotation);
		}
	}
	
	/**
	 * controls multiple changes to be applied, considering current aspects
	 * 
	 * @param changes
	 * 			changes to be applied
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return list of changes applied successfully (considering successful updating of aspect annotations)
	 */
	public static List<OWLOntologyChange> handleMultipleChanges(List<OWLOntologyChange> changes, Annotation annotation) {
		List<OWLOntologyChange> changesApplied = new ArrayList<OWLOntologyChange>();
		for (OWLOntologyChange change : changes) {
			if (change.isAxiomChange()) { 
				if (change.isAddAxiom()) { // change is AddAxiom
					changesApplied.addAll(ModificationHelperAdd.handleChangeAddAxiomReturnListOfChanges(change, annotation));
				} else { // change is RemoveAxiom
					changesApplied.addAll(ModificationHelperRemove.handleChangeRemoveAxiomReturnListOfChanges(change, annotation));
				}
			} else { // change is something else, e.g. Import change
				ChangeApplied chgApplied = change.getOntology().getOWLOntologyManager().applyChange(change);
				if (chgApplied.equals(ChangeApplied.SUCCESSFULLY)) {
					changesApplied.add(change);
				}
			}
		}
		return changesApplied;
	}


	/**
	 * handles addition or removal of these axioms,
	 * depending on the value of parameter isAddAxiom
	 * 
	 * @param ontology
	 * 			ontology 
	 * @param axioms
	 * 			axioms
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @param isAddAxioms
	 * 			boolean telling whether this method is addAxioms()
	 * @return list of changes applied successfully (considering successful updating of aspect annotations)
	 */
	public static List<OWLOntologyChange> handleAddOrRemoveAxs(OWLOntology ontology, Set<OWLAxiom> axioms, Annotation annotation, boolean isAddAxioms) {
		List<OWLOntologyChange> changesApplied = new ArrayList<OWLOntologyChange>();
		if (isAddAxioms) {		
			for (OWLAxiom axiom : axioms) {
				changesApplied.addAll(ModificationHelperAdd.handleChangeAddAxiomReturnListOfChanges(new AddAxiom(ontology, axiom), annotation));
			}	
		} else {		
			for (OWLAxiom axiom : axioms) {
//				changesApplied.addAll(ModificationHelperRemove.handleChangeRemoveAxiomReturnListOfChanges(new RemoveAxiom(ontology, axiom), annotation));
				changesApplied.addAll(ModificationHelperRemove.handleRemoveAxiomReturnChanges(axiom, ontology, annotation));
			}
		}
		return changesApplied;
	}

	
	// ----------------- HELPERS ------------------------------
		
	/**
	 * associate axiom with aspects
	 * 
	 * @param onto
	 * 			ontology
	 * @param axiom
	 * 			axiom to be annotated with aspects
	 * @param currentAspects
	 * 			current aspects as string array
	 * @return axiom annotated with current aspects
	 */
	public static OWLAxiom associateAxiomWithAspects(OWLOntology onto, OWLAxiom axiom, String[] currentAspects) {
		return axiom.getAnnotatedAxiom(createSetOfRelevantAnnotations(onto, currentAspects));
	}
	
	/**
	 * associate these axioms with aspects 
	 * 
	 * @param ontology
	 * 			current ontology
	 * @param axioms
	 * 			axioms to be associated with aspects
	 * @param currentAspects
	 * 			current aspects as string array
	 * @return axioms annotated with aspects
	 */
	public static Set<OWLAxiom> associateAxiomsWithAspects(OWLOntology ontology, Set<OWLAxiom> axioms, String[] currentAspects) {
		Set<OWLAxiom> newAxioms = new HashSet<OWLAxiom>();	
		for (OWLAxiom axiom : axioms) {
			newAxioms.add(associateAxiomWithAspects(ontology, axiom, currentAspects));
		}
		return newAxioms;
	}
	
	/**
	 * replace axiom inside axiom change with its copy annotated with aspects
	 * 
	 * @param change
	 * 			axiom change
	 * @param currentAspects
	 * 			current aspects as string array
	 * @return change with updated axiom
	 */
	public static OWLOntologyChange associateChangeWithAspects(OWLOntologyChange change, String[] currentAspects) {
		OWLOntology onto = change.getOntology();
		OWLAxiom newAxiom = associateAxiomWithAspects(onto, change.getAxiom(), currentAspects);
		if (change.isAxiomChange()) { // before proceeding with the change: add current aspects to AxiomChange
			return (change.isAddAxiom()) ? new AddAxiom(onto, newAxiom) : new RemoveAxiom(onto, newAxiom);
		} else {
			return change;
		}
	}
	
	/**
	 * replace all axioms inside axiom changes with their copies annotated with aspects 
	 * and return result as a new list of changes
	 * 
	 * @param changes
	 * 			axiom changes
	 * @param currentAspects
	 * 			IRIs of current aspects as String array
	 * @return list of changes with updated axioms
	 */
	public static List<OWLOntologyChange> associateChangesWithAspects(List<OWLOntologyChange> changes, String[] currentAspects) {
		List<OWLOntologyChange> changesUpdated = new ArrayList<OWLOntologyChange>();	
		for (OWLOntologyChange change : changes) {
			changesUpdated.add(associateChangeWithAspects(change, currentAspects));
		}
		return changesUpdated;
	}
	
	/**
	 * concatenates two string arrays
	 * 
	 * @param arr1
	 * 			first string array
	 * @param arr2
	 * 			second string array
	 * @return concatenated array
	 */
	public static String[] concatenateTwoStringArrays(String[] arr1, String[] arr2) {
		List<String> tmpList = new ArrayList<String>(Arrays.asList(arr1));
		tmpList.addAll(Arrays.asList(arr2));
		String[] result = new String[tmpList.size()];
		for (int i=0; i<tmpList.size(); i++) {
			result[i] = tmpList.get(i);
		}	
		return result;	
	}

	

}
