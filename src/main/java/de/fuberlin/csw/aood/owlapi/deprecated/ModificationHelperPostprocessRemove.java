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
package de.fuberlin.csw.aood.owlapi.deprecated;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

@Deprecated
public class ModificationHelperPostprocessRemove extends ModificationHelperPostprocess {
	
	// checks if the axiom is of type declaration, and removes aspects from entity if yes
	public static void postprocessRemoveAxiom(OWLOntology onto, OWLAxiom userAx, String[] currentAspects) {
		if (userAx.isOfType(AxiomType.DECLARATION)) { // additionally remove aspects from declared entity
			removeAspectsFromEntity(onto, ((OWLDeclarationAxiom) userAx).getEntity(), currentAspects);
		}	
	}
	
	// remove aspects from entity
	protected static void removeAspectsFromEntity(OWLOntology onto, OWLEntity entity, String[] currentAspects) {
		OWLOntologyManager om = onto.getOWLOntologyManager();
		OWLDataFactory df = om.getOWLDataFactory();
		OWLAnnotationProperty annoProp = df.getOWLAnnotationProperty(isPointcutOfPropertyIRI);
		for (String aspectIRI : currentAspects) {
			om.removeAxiom(onto, df.getOWLAnnotationAssertionAxiom(
				entity.getIRI(), df.getOWLAnnotation(annoProp, IRI.create(aspectIRI))));
		}
	}

}
