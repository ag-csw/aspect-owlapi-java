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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import de.fuberlin.csw.aood.owlapi.Config;
import de.fuberlin.csw.aood.owlapi.OWLAspectAnd;
import de.fuberlin.csw.aood.owlapi.OWLAspectOr;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

/**
 * This helper provides methods to filter owl axioms considering current aspects 
 */
public class FilteringHelperAxioms extends FilteringHelper {
	
	private static final Logger log = Logger.getLogger(FilteringHelperAxioms.class.getName());
	
	private static final boolean expandModules;
	
	// TODO move to more sensible place
	static {
		expandModules = (boolean)Config.instance().get("expandModules");
	}
	
	/**
	 * returns the subset of this axiom set consisting only of axioms associated with current aspects
	 * 
	 * @param onto
	 * 			ontology to be checked
	 * @param axioms
	 * 			set of axioms to be filtered
	 * @param annotation
	 * 			Annotation of type {@link OWLAspectAnd} or {@link OWLAspectOr} specifying current aspects
	 * @return filtered axioms
	 */
	public static Set<OWLAxiom> filterAxioms(OWLOntology onto, Set<OWLAxiom> axioms, Annotation annotation) {
		// The passed annotation can be only of type OWLAspectAnd or OWLAspectOr
		String[][] aspects = transformAnnotationToAspects(annotation);
		Set<OWLAxiom> filteredAxioms = new HashSet<OWLAxiom>();
		for (OWLAxiom ax : axioms) {
			if (passAspectsTest(ax, onto, aspects)) {
				filteredAxioms.add(ax.getAxiomWithoutAnnotations());
			}	
		}
		
		// TODO move to specialized Helper class
		if (expandModules) {
			HashSet<OWLEntity> signature = new HashSet<OWLEntity>();
			for (OWLAxiom axiom : filteredAxioms) {
				signature.addAll(axiom.getSignature());
			}
			SyntacticLocalityModuleExtractor extractor = new SyntacticLocalityModuleExtractor(onto.getOWLOntologyManager(), onto, ModuleType.STAR);
			Set<OWLAxiom> expandedModule = extractor.extract(signature);
			
			filteredAxioms.addAll(expandedModule);
			
			log.log(Level.FINE, "\n\n *** axioms filtered from aspect: ***");
			log(filteredAxioms);
			log.log(Level.FINE, " *** signature: ");
			log(signature);
			log.log(Level.FINE, "-----\n *** expanded module:***");
			log(expandedModule);
			log.log(Level.FINE, "\n\n");
		}
		
		return filteredAxioms;
	}
	
	private static void log(Collection c) {
		for (Object o : c) {
			if (o instanceof OWLAxiom) {
				o = ((OWLAxiom)o).getAxiomWithoutAnnotations();
			}
			log.log(Level.FINE, "* " + o);
		}
	}
}
