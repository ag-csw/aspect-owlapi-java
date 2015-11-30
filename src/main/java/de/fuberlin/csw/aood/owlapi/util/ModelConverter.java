package de.fuberlin.csw.aood.owlapi.util;

/**
 * Created by lars on 12.10.15.
 */


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLOntologyImportsClosureSetProvider;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;


public class ModelConverter {

    /**
     * Converts a Jena OntModel to an OWL API OWLOntology.
     * @param jenaModel A Jena OntModel.
     * @return The corresponding OWL API OWLOntology.
     */
    public static OWLOntology jenaModelToOWLOntology(Model jenaModel) throws OWLOntologyCreationException {
        RDFWriter writer = jenaModel.getWriter("RDF/XML");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer.write(jenaModel, baos, null);
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        manager.setSilentMissingImportsHandling(true);
        OWLOntology owlOntology = manager.loadOntologyFromOntologyDocument(bais);
        return owlOntology;
    }


    public static OntModel owlOntologyToJenaModel(OWLOntology owlOntology, boolean withImports) throws OWLOntologyStorageException, OWLOntologyCreationException {

        OWLOntologyManager om = owlOntology.getOWLOntologyManager();

        if (withImports) {
            owlOntology = new OWLOntologyMerger(new OWLOntologyImportsClosureSetProvider(om, owlOntology)).createMergedOntology(om, null);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        owlOntology.getOWLOntologyManager().saveOntology(owlOntology, new RDFXMLOntologyFormat(), baos);
        byte[] bytes = baos.toByteArray();



        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);


        OntModel jenaModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
        jenaModel.read(bais, null, "RDF/XML");

        return jenaModel;
    }


}
