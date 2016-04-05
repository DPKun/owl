package ontology.practice;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OntologyBuidTest {

	private static final String ONTOLOGY_DOCUMENT_PATH = "sources/ILONA-ontology.owx";

	@Test
	public void testLoadOntologyFromIRI() throws OWLOntologyCreationException {

		File ontologyDocument = new File(ONTOLOGY_DOCUMENT_PATH);
		IRI ontIRI = IRI.create(ontologyDocument);

		// the OWLOntologyManager implementation with the OWLManager
		OWLOntologyManager onman = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = onman.loadOntologyFromOntologyDocument(ontIRI);
	}

	@Test
	public void testLoadOntologyFromFile() throws OWLOntologyCreationException {

		File ontologyDocument = new File("sources/ILONA-ontology.owx");

		// the OWLOntologyManager implementation with the OWLManager
		OWLOntologyManager onman = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = onman
				.loadOntologyFromOntologyDocument(ontologyDocument);
	}

}
