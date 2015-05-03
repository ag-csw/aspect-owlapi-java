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
package de.fuberlin.csw.deprecated;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import de.fuberlin.csw.helpers.ModificationHelper;

@Deprecated
public class ModificationHelperPostprocess extends ModificationHelper {
	
	// ----------------- POSTPROCESSING HELPERS ------------------------------
	
	// Postprocesses the combination of current aspects and ontology change:
	// adds or removes aspects to or from entities if it was declaration axiom
	public static void postprocessChange(String[] currentAspects, OWLOntologyChange ontoChange) {
		if (ontoChange.isAddAxiom()) {
			ModificationHelperPostprocessAdd.postprocessAddAxiom(ontoChange.getOntology(), ontoChange.getAxiom(), currentAspects);
		} else if (ontoChange.isRemoveAxiom()) {
			ModificationHelperPostprocessRemove.postprocessRemoveAxiom(ontoChange.getOntology(), ontoChange.getAxiom(), currentAspects);
		} 
	}
	
	public static void postprocessChanges(String[] currentAspects, List<OWLOntologyChange> changesUpdated) {
		for (OWLOntologyChange change : changesUpdated) {
			postprocessChange(currentAspects, change);
		}
	}
	
	// if true then add axiom, if false, remove
	public static void postprocessAddOrRemoveAxiom(OWLOntology ontology, OWLAxiom newAxiom, String[] currentAspects, boolean isAddAxiom) {
		if (isAddAxiom) {
			ModificationHelperPostprocessAdd.postprocessAddAxiom(ontology, newAxiom, currentAspects);
		} else {
			ModificationHelperPostprocessRemove.postprocessRemoveAxiom(ontology, newAxiom, currentAspects);
		}
	}

	@Deprecated
	public static void postprocessAddOrRemoveAxioms(OWLOntology ontology, String[] currentAspects, Set<OWLAxiom> newAxioms, boolean isAddAxioms) {
		for (OWLAxiom axiom : newAxioms) {
			if (isAddAxioms) {
				ModificationHelperPostprocessAdd.postprocessAddAxiom(ontology, axiom, currentAspects);
			} else {
				ModificationHelperPostprocessRemove.postprocessRemoveAxiom(ontology, axiom, currentAspects);
			}
		}
	}

}
