package org.owlTryTwo;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class OntologyReader {

	public static final File ONTOLOGY_ROOT = new File(
			"/home/dpkun/practice/OWLAPI/owlTryTwo/resources/annotatedAnimals.owl");
	public static final String IRI_PREFIX = new String("http://webprotege.stanford.edu/");
	public static final String ANIMAL_IRI = new String("http://webprotege.stanford.edu/#Animal");
	public static final String SIZE_IRI = new String(
			"http://webprotege.stanford.edu/project/BQ9brKWW6pcSvWvKu3ajh4#Size");
	public static final String FOOD_PLANT_IRI = new String(
			"http://webprotege.stanford.edu/project/BQ9brKWW6pcSvWvKu3ajh4#EatsPlant");
	public static final String FOOD_MEAT_IRI = new String(
			"http://webprotege.stanford.edu/project/BQ9brKWW6pcSvWvKu3ajh4#EatsAnimal");

	public Set<Animal> readAnimals() throws OWLOntologyCreationException {
		Set<Animal> animals = new HashSet<Animal>();
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ONTOLOGY_ROOT);
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

		NodeSet<OWLClass> subClasses = treasoner.getSubClasses(factory.getOWLClass(ANIMAL_IRI));
		for (Node<OWLClass> subClass : subClasses) {
			OWLEntity entity = subClass.getRepresentativeElement();
			NodeSet<OWLNamedIndividual> instances = treasoner.getInstances(factory.getOWLClass(entity.getIRI()));
			for (Node<OWLNamedIndividual> instance : instances) {
				OWLEntity instanceEntity = instance.getRepresentativeElement();

				// get species
				String name = shortFormProvider.getShortForm(instanceEntity);
				String size = new String();
				Set<String> food = new HashSet<String>();

				// get size
				Set<OWLDataPropertyAssertionAxiom> dataPropertyAxioms = ontology
						.getDataPropertyAssertionAxioms(instanceEntity.asOWLNamedIndividual());
				for (OWLDataPropertyAssertionAxiom dataPropertyAxiom : dataPropertyAxioms) {
					Set<OWLDataProperty> dataProperties = dataPropertyAxiom.getDataPropertiesInSignature();
					for (OWLDataProperty dataProperty : dataProperties) {
						if (dataProperty.getIRI().toString().equals(SIZE_IRI)) {
							size = dataPropertyAxiom.getObject().getLiteral();
						}
					}
				}

				// get foods
				Set<OWLObjectPropertyAssertionAxiom> objectPropertyAxioms = ontology
						.getObjectPropertyAssertionAxioms(instanceEntity.asOWLNamedIndividual());
				for (OWLPropertyAssertionAxiom objectPropertyAxiom : objectPropertyAxioms) {
					Set<OWLObjectProperty> objectProperties = objectPropertyAxiom.getObjectPropertiesInSignature();
					for (OWLObjectProperty objectProperty : objectProperties) {
						if (objectProperty.getIRI().toString().equals(FOOD_PLANT_IRI)
								|| objectProperty.getIRI().toString().equals(FOOD_MEAT_IRI)) {
							OWLIndividual range = (OWLIndividual) objectPropertyAxiom.getObject();
							food.add(range.asOWLNamedIndividual().getIRI().getShortForm());
						}
					}
				}
				Animal animal = new Animal(name, size, food);
				animals.add(animal);

			}
		}
		return animals;

	}

}
