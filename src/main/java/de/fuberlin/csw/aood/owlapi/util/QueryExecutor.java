package de.fuberlin.csw.aood.owlapi.util;

import org.semanticweb.owlapi.model.OWLOntology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Created by lars on 12.10.15.
 */
public class QueryExecutor {

    public OWLOntology getOntologyModule(String queryString, OWLOntology ontology) throws OWLOntologyStorageException, OWLOntologyCreationException {
        OntModel jenaModel = ModelConverter.owlOntologyToJenaModel(ontology, true);

        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, jenaModel) ;

        Model result = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
        result = qexec.execConstruct(result);

        OWLOntology module = ModelConverter.jenaModelToOWLOntology(result);
        return module;
    }

}
