package de.fuberlin.csw.aood.owlapi.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

public class TestSyntacticLocality {

	public static void main(String[] args) {
		try {
			OWLOntologyManager om = OWLManager.createOWLOntologyManager();
			OWLOntology onto = om.loadOntologyFromOntologyDocument(new File("/Users/ralph/Documents/Arbeit/Publications/2015 KEOD/Evaluation/Ontologies/AKTiveSA/AKTiveSAOntology.owl"));
			SyntacticLocalityModuleExtractor ex = new SyntacticLocalityModuleExtractor(om, onto, ModuleType.BOT);
			
			OWLDataFactory df = om.getOWLDataFactory();
			
			HashSet<OWLEntity> signature = new HashSet<OWLEntity>();
			signature.add(df.getOWLClass(IRI.create("http://sa.aktivespace.org/ontologies/aktivesa#Waterway")));
			signature.add(df.getOWLObjectProperty(IRI.create("http://sa.aktivespace.org/ontologies/aktivesa#hasPrimarySubstanceConstituent")));
			signature.add(df.getOWLClass(IRI.create("http://sa.aktivespace.org/ontologies/aktivesa#Water")));
			
			Set<OWLAxiom> module = ex.extract(signature);
			for (OWLAxiom axiom : module) {
				System.out.println(axiom);
			}
			
			System.out.println("\nSum: " + module.size());
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

}
