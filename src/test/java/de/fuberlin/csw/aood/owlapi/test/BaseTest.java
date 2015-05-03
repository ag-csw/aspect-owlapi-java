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
package de.fuberlin.csw.aood.owlapi.test;

import java.io.File;

import javax.annotation.Nonnull;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.PriorityCollection;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public abstract class BaseTest {
	
	// ------------------------- TEST SETUP --------------------------------------
    @Nonnull
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    static String testfolder = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "aspect-ontology" + File.separator;
    static String testfolderModules = testfolder + "modules" + File.separator;
    
    // mappings for ontologies to test
    @Nonnull
    public static final IRI ADVISED_ONTO_IRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/testpaintings");   
    @Nonnull 
    public static final File ADVISED_ONTO_NOASPECTS_FILE= new File(testfolder + "testpaintingsAspectless.owl");
    @Nonnull 
    public static final File ADVISED_ONTO_WITHASPECTS_FILE= new File(testfolder + "testpaintings.owl");

//    @Nonnull
//    public static final IRI EXAMPLE_IRI = IRI.create("http://www.semanticweb.org/ontologies/ont.owl");
//    @Nonnull
//    public static final IRI EXAMPLE_SAVE_IRI = IRI.create("file:materializedOntologies/ont1290535967123.owl");

	// mappings for ontologies imported by test ontologies
	
    IRI paintingAnnotatedIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/example/paintingAnnotated");
	File paintingAnnotatedFile = new File(testfolder + "paintingAnnotated.owl");
	
	IRI paintingIRI = IRI.create("http://spraakbanken.gu.se/rdf/owl/painting.owl");
	File paintingFile = new File(testfolder + "imports" + File.separator + "painting.owl");
    
    // mappings for aspect ontologies
	   
	IRI aspectOntologyIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl");
	File aspectOntologyFile = new File(testfolder + "aspectOWL.owl");
	
	IRI severalAspectsExampleIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/example");
	File severalAspectsExampleFile = new File(testfolder + "several-aspects-example.owl");

	// complexity
	IRI complexityAspectIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity");
	File complexityAspectFile = new File(testfolderModules + "complexity" + File.separator + "complexity-aspect.owl");
	
	IRI complexityExampleIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/complexity/example");
	File complexityExampleFile = new File(testfolderModules + "complexity" + File.separator + "complexity-example.owl");
	
	// provenance
	IRI provenanceAspectIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance");
	File provenanceAspectFile = new File(testfolderModules + "provenance" + File.separator + "provenance-aspect.owl");
	
	IRI provenanceExampleIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/provenance/example");
	File provenanceExampleFile = new File(testfolderModules + "provenance" + File.separator + "provenance-example.owl");
	
	IRI provoIRI = IRI.create("http://www.w3.org/ns/prov-o");
	File provoFile = new File(testfolderModules + "provenance" + File.separator + "imports" + File.separator + "prov-o.owl");
	
	
	// temporal
	IRI temporalAspectIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/temporal");
	File temporalAspectFile = new File(testfolderModules + "temporal" + File.separator + "painting.owl");
	
	IRI temporalExampleIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl/temporal/example");
	File temporalExampleFile = new File(testfolderModules + "temporal" + File.separator + "painting.owl");
	
	IRI timeIRI = IRI.create("http://www.w3.org/2006/time");
	File timeFile = new File(testfolderModules + "temporal" + File.separator + "imports" + File.separator + "time.owl");

    
	public static final IRI isPointcutOfPropertyIRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect/owl#isPointcutOf");

    @Nonnull
    OWLDataFactory df = OWLManager.getOWLDataFactory();
    
    @Nonnull
    public OWLOntologyManager create() {
    	// get hold of ontology manager
        OWLOntologyManager om = OWLManager.createOWLOntologyManager();
        PriorityCollection<OWLOntologyIRIMapper> iriMappers = om.getIRIMappers();
        iriMappers.add(new AutoIRIMapper(new File("materializedOntologies"), true));
        iriMappers.add(new SimpleIRIMapper(paintingIRI, IRI.create(paintingFile)));
        iriMappers.add(new SimpleIRIMapper(paintingAnnotatedIRI, IRI.create(paintingAnnotatedFile)));
        iriMappers.add(new SimpleIRIMapper(aspectOntologyIRI, IRI.create(aspectOntologyFile)));
//        iriMappers.add(new SimpleIRIMapper(severalAspectsExampleIRI, IRI.create(severalAspectsExampleFile)));
        iriMappers.add(new SimpleIRIMapper(complexityAspectIRI, IRI.create(complexityAspectFile)));
        iriMappers.add(new SimpleIRIMapper(complexityExampleIRI, IRI.create(complexityExampleFile)));
        iriMappers.add(new SimpleIRIMapper(provenanceAspectIRI, IRI.create(provenanceAspectFile)));
        iriMappers.add(new SimpleIRIMapper(provenanceExampleIRI, IRI.create(provenanceExampleFile)));
        iriMappers.add(new SimpleIRIMapper(temporalAspectIRI, IRI.create(temporalAspectFile)));
        iriMappers.add(new SimpleIRIMapper(temporalExampleIRI, IRI.create(temporalExampleFile)));
        iriMappers.add(new SimpleIRIMapper(timeIRI, IRI.create(timeFile)));
        iriMappers.add(new SimpleIRIMapper(provoIRI, IRI.create(provoFile)));
        return om;
    }
    
    @Nonnull
    public OWLOntology loadAdvisedOntologyWithAspects(@Nonnull OWLOntologyManager om)
            throws OWLOntologyCreationException {
    	// Get hold of advised ontology file
//    	ClassLoader classLoader = getClass().getClassLoader();
//		File advisedOntologyFile = new File(classLoader.getResource(ADVISED_ONTO_WITHASPECTS_LOCAL_PATH).getFile());
    	// Map the ontology IRI to a physical IRI (a file)
    	om.getIRIMappers().add(new SimpleIRIMapper(ADVISED_ONTO_IRI, IRI.create(ADVISED_ONTO_WITHASPECTS_FILE)));
    	// Load ontology - using ontology IRI. IRI Mapper knows file location.
    	return om.loadOntology(ADVISED_ONTO_IRI);
    }
    
    @Nonnull
    public OWLOntology loadAdvisedOntologyNoAspects(@Nonnull OWLOntologyManager om)
            throws OWLOntologyCreationException {
    	// Map the ontology IRI to a physical IRI (a file)
    	om.getIRIMappers().add(new SimpleIRIMapper(ADVISED_ONTO_IRI, IRI.create(ADVISED_ONTO_NOASPECTS_FILE)));
    	// Load ontology - using ontology IRI. IRI Mapper knows file location.
    	return om.loadOntology(ADVISED_ONTO_IRI);
    }
    
}
