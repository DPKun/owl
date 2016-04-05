package ontology.practice;

import static org.junit.Assert.*;

import java.io.File;
import java.security.acl.Owner;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class AxiomProcessingTest {

	private static final String ONTOLOGY_DOCUMENT_PATH = "sources/ILONA-ontology.owx";
	// private static final String ONTOLOGY_DOCUMENT_PATH =
	// "sources/root-ontology.owx";

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
	public void testListAxioms() {
		for (OWLAxiom axiom : ontology.getAxioms()) {
			System.out.println(axiom);
		}
	}

	@Ignore
	@Test
	public void testListClassAxioms() {
		for (OWLClass owlClass : ontology.getClassesInSignature()) {
			System.out.println(owlClass);
			Set<OWLAnnotationAssertionAxiom> annotationAxioms = ontology
					.getAnnotationAssertionAxioms(owlClass.getIRI());
			for (OWLAnnotationAssertionAxiom annotationAxiom : annotationAxioms) {
				System.out.println("\t" + annotationAxiom.getValue());
			}
		}
	}

	@Test
	public void testListAllIndividuals() {
		Set<OWLNamedIndividual> individuals = ontology
				.getIndividualsInSignature();
		for (OWLNamedIndividual individum : individuals) {
			System.out.println(individum.getIRI());
			Set<OWLAnnotationAssertionAxiom> annotations = ontology
					.getAnnotationAssertionAxioms(individum.getIRI());
			
			Set<OWLDataPropertyAssertionAxiom> dpaas = ontology
					.getDataPropertyAssertionAxioms(individum);
			for (OWLDataPropertyAssertionAxiom dpaa : dpaas) {

				OWLDataPropertyExpression dpe = dpaa.getProperty();
				System.out.println("\t\t" + dpaa);
				System.out.println("\t\t" + dpe.asOWLDataProperty().getIRI()
						+ " -> "
						+ annotationName(dpe.asOWLDataProperty().getIRI())
						+ " --> " + dpaa.getObject());

				// System.out.println("\t\t"+dpaa);
			}
			/*
			for (OWLAnnotationAssertionAxiom annotation : annotations) {

				// System.out.println(annotation.getAnnotation().getProperty().getIRI());
				// System.out.println(annotation.getAnnotation().getValue());
				System.out.println(annotation.toString() + "\t"
						+ annotation.getValue());
			}
			*/
		}
	}

	private String annotationName(IRI iri) {
		Set<OWLAnnotationAssertionAxiom> annotations = ontology
				.getAnnotationAssertionAxioms(iri);
		return annotations.iterator().next().getValue().toString();
	}

	@Ignore
	@Test
	public void testListIndividualsOfClassAxioms() {

		Set<OWLClass> classes = ontology.getClassesInSignature();
		for (OWLClass cls : classes) {
			System.out.println(cls.getIRI());
			Set<OWLAnnotationAssertionAxiom> clsAnnotations = ontology
					.getAnnotationAssertionAxioms(cls.getIRI());
			for (OWLAnnotationAssertionAxiom annotation : clsAnnotations) {
				System.out.println(annotation.getValue());
			}
			System.out.println(cls.getIndividualsInSignature().size());

		}
	}

	@Ignore
	@Test
	public void testListIndividuals() {

		System.out.println(String.format("# of individuals: %d", ontology
				.getIndividualsInSignature().size()));
		for (OWLNamedIndividual individual : ontology
				.getIndividualsInSignature()) {
			System.out.println(individual.getIRI());
			System.out.println(" # of properties: "
					+ individual.getDataPropertiesInSignature().size());
			System.out.println(" # of datatypes: "
					+ individual.getDatatypesInSignature());

			for (OWLDataProperty property : individual
					.getDataPropertiesInSignature()) {
				System.out.println("\t" + property);
			}
		}

	}
}
