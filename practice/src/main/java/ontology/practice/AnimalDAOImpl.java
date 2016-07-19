package main.java.ontology.practice;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyStorageIOException;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyChangeVetoException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyDocumentAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyFactoryNotFoundException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLStorerNotFoundException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class AnimalDAOImpl implements AnimalsDAO {

	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OWLDataFactory factory;
	private OWLReasoner reasoner;
	private ShortFormProvider shortFormProvider;

	public AnimalDAOImpl(String ontologyFilePath) throws OWLOntologyCreationException {
		super();

		// create the classes needed for the handling of the OWL
		this.manager = OWLManager.createOWLOntologyManager();
		this.ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyFilePath));
		this.factory = OWLManager.getOWLDataFactory();
		this.shortFormProvider = new SimpleShortFormProvider();
		this.reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
	}

	@Override
	public Collection<Animal> readAnimals() {
		// create a collection to read the animals into it
		Collection<Animal> result = new ArrayList<Animal>();
		// Get the subclasses of the animal class with the reasoner
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(factory.getOWLClass(AnimalsIRIs.ANIMAL));
		for (Node<OWLClass> subClass : subClasses) {
			OWLEntity entity = subClass.getRepresentativeElement();
			// Get the instances of the subclasses with the reasoner
			NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(factory.getOWLClass(entity.getIRI()));
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
						if (dataProperty.getIRI().toString().equals(AnimalsIRIs.SIZE)) {
							size = dataPropertyAxiom.getObject().getLiteral();
						}
					}
				}

				// get what the animal eats
				Set<OWLObjectPropertyAssertionAxiom> objectPropertyAxioms = ontology
						.getObjectPropertyAssertionAxioms(instanceEntity.asOWLNamedIndividual());
				for (OWLPropertyAssertionAxiom objectPropertyAxiom : objectPropertyAxioms) {
					Set<OWLObjectProperty> objectProperties = objectPropertyAxiom.getObjectPropertiesInSignature();
					for (OWLObjectProperty objectProperty : objectProperties) {
						System.out.println(objectProperty.getIRI().toString());
						if (objectProperty.getIRI().toString().equals(AnimalsIRIs.EATS_PLANT)
								|| objectProperty.getIRI().toString().equals(AnimalsIRIs.EATS_ANIMAL)) {
							OWLIndividual range = (OWLIndividual) objectPropertyAxiom.getObject();
							food.add(range.asOWLNamedIndividual().getIRI().getShortForm());
						}
					}
				}
				Animal animal = new Animal(name, size, food);
				result.add(animal);

			}
		}
		return result;
	}

	@Override
	public void createAnimals(Collection<Animal> animals) throws OWLOntologyStorageException {
		for (Animal animal : animals) {
			createAnimal(animal);
		}
	}

	@Override
	public void updateAnimals(Collection<Animal> animals) throws OWLOntologyStorageException {
		for (Animal animal : animals) {
			deleteAnimal(animal);
		}
		createAnimals(animals);

	}

	@Override
	public void deleteAnimal(Animal animal) throws OWLOntologyStorageException {
		// Create OWLEntityRemover
		OWLEntityRemover eraser = new OWLEntityRemover(ontology);
		// visit the needed individual with the remover
		for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()) {
			if (ind.getIRI().getShortForm().toString().equals(animal.getSpecies())) {
				ind.accept(eraser);
			}
		}

		// apply the changes in the ontology
		manager.applyChanges(eraser.getChanges());
		// save the ontology
		ontology.saveOntology();

	}

	public void createAnimal(Animal animal) throws OWLOntologyStorageException {
		// create the animal itself
		OWLNamedIndividual animalIndividual = factory.getOWLNamedIndividual(
				IRI.create("http://webprotege.stanford.edu/project/BQ9brKWW6pcSvWvKu3ajh4#" + animal.getSpecies()));
		// add the individual to the animal class
		// should add it later, maybe we can decide if it is a carnivore or
		// herbivore
		OWLClassAssertionAxiom animalClass = factory
				.getOWLClassAssertionAxiom(factory.getOWLClass(IRI.create(AnimalsIRIs.ANIMAL)), animalIndividual);

		// get the size of the animal
		OWLDataPropertyAssertionAxiom sizeProperty = factory.getOWLDataPropertyAssertionAxiom(
				factory.getOWLDataProperty(AnimalsIRIs.SIZE), animalIndividual, animal.getSize());

		List<AddAxiom> foods = new ArrayList<AddAxiom>();
		// get the instances of animals, check if it eats any of them
		NodeSet<OWLNamedIndividual> meats = reasoner.getInstances(factory.getOWLClass(IRI.create(AnimalsIRIs.ANIMAL)));
		for (Node<OWLNamedIndividual> meat : meats) {
			OWLNamedIndividual meal = meat.getRepresentativeElement();
			if (animal.getFood().contains(meal.getIRI().getShortForm().toString())) {
				foods.add(new AddAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(
						factory.getOWLObjectProperty(AnimalsIRIs.EATS_ANIMAL), animalIndividual, meal)));
			}

		}
		// if it eats animals, then it is a carnivore, if not, then it is a
		// herbivore
		if (foods.isEmpty() == false) {
			animalClass = factory.getOWLClassAssertionAxiom(factory.getOWLClass(AnimalsIRIs.CARNIVORE),
					animalIndividual);
		} else {
			animalClass = factory.getOWLClassAssertionAxiom(factory.getOWLClass(AnimalsIRIs.HERBIVORE),
					animalIndividual);
		}
		// check if it eats any plants
		NodeSet<OWLNamedIndividual> plants = reasoner.getInstances(factory.getOWLClass(IRI.create(AnimalsIRIs.PLANT)));
		for (Node<OWLNamedIndividual> plant : plants) {
			OWLNamedIndividual plantIndividual = plant.getRepresentativeElement();
			if (animal.getFood().contains(plantIndividual.getIRI().getShortForm().toString())) {
				foods.add(new AddAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(
						factory.getOWLObjectProperty(AnimalsIRIs.EATS_PLANT), animalIndividual, plantIndividual)));
			}

		}

		// add the class and DataProperty axioms
		AddAxiom classAxiom = new AddAxiom(ontology, animalClass);
		AddAxiom sizeAxiom = new AddAxiom(ontology, sizeProperty);

		// Apply the changes
		manager.applyChange(classAxiom);
		manager.applyChange(sizeAxiom);
		manager.applyChanges(foods);

		// save the ontology
		ontology.saveOntology();

	}

}
