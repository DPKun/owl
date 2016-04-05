package ontology.practice;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OWLDataFactoryTest {

	private static final String ONTOLOGY_DOCUMENT_PATH = "sources/ILONA-ontology.owx";

	private OWLOntology ontology;

	@Before
	public void setUp() throws OWLOntologyCreationException {
		File ontologyDocument = new File(ONTOLOGY_DOCUMENT_PATH);
		IRI ontIRI = IRI.create(ontologyDocument);

		// the OWLOntologyManager implementation with the OWLManager
		OWLOntologyManager onman = OWLManager.createOWLOntologyManager();

		this.ontology = onman.loadOntologyFromOntologyDocument(ontIRI);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
