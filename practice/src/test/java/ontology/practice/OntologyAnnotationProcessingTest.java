package ontology.practice;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OntologyAnnotationProcessingTest {

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

	@Ignore
	@Test
	public void testListAnnotations() {
		
		System.out.println(String.format("# of Annotations: %d", ontology.getAnnotations().size()));
		for(OWLAxiom annotation : ontology.getAxioms()){
			
			if(!annotation.isAnnotationAxiom()){
				continue;
			}
			System.out.println(((OWLAnnotation)annotation).getValue());
		}
	}
	
	@Test
	public void test(){
		//final String knownIRI = "http://webprotege.stanford.edu/ReEABjzdvcE0Nzqguw0Qvd";
		final String knownIRI = "http://webprotege.stanford.edu/RC26OLkjxs60DJJ2PUZ3AtN";
		
		
		
		Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(IRI.create(knownIRI));
		for(OWLAnnotationAssertionAxiom annotationAssertion : annotationAssertionAxioms){
			System.out.println(annotationAssertion.getValue());
		}
	}

}
