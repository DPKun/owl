package Ontology.practice;

import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.IRI;

/**
 * Tries with the OWL api
 */
public class App {
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		File ontology = new File("sources/ILONA-ontology.owx");
		IRI ontIRI = IRI.create(ontology);

		// Instancing the OWLmanager for further use
		OWLManager man = new OWLManager();

		OWLDataFactory fac = man.getOWLDataFactory();

		// the OWLOntologyManager implementation with the OWLManager
		OWLOntologyManager onman = man.createOWLOntologyManager();

		// loading the ontology itself
		OWLOntology first = onman.loadOntologyFromOntologyDocument(ontology);
		
		System.out.println(first.toString());
	}
}
