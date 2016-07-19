package test.java.ontology.practice;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import main.java.ontology.practice.AnimalQueries;

public class OWLAPITest {

	@Test
	public void testReadOntology() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
	}

	@Test
	public void testReasonerInitialization() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
	}

	@Test
	public void testGetCarnivoreClassInstances() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));
	}

	@Test
	public void testGetNamesOfIndividuals() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		for (Node<OWLNamedIndividual> nod : ret) {
			OWLEntity ent = nod.getRepresentativeElement();
			// System.out.println(shortFormProvider.getShortForm(ent));
		}
	}

	@Test
	public void testGetDataProperties() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		for (Node<OWLNamedIndividual> nod : ret) {
			OWLEntity ent = nod.getRepresentativeElement();

			Set<OWLDataPropertyAssertionAxiom> DPs = ontology
					.getDataPropertyAssertionAxioms(ent.asOWLNamedIndividual());
			for (OWLDataPropertyAssertionAxiom DP : DPs) {
				// System.out.println(DP.getProperty());
			}
		}
	}

	@Test
	public void testGetValuesOfDataProperties() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		for (Node<OWLNamedIndividual> nod : ret) {
			OWLEntity ent = nod.getRepresentativeElement();

			Set<OWLDataPropertyAssertionAxiom> DPs = ontology
					.getDataPropertyAssertionAxioms(ent.asOWLNamedIndividual());
			for (OWLDataPropertyAssertionAxiom DP : DPs) {

				// System.out.println(DP.getObject().getLiteral());
			}
		}
	}

	@Test
	public void testGetObjectProperties() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		for (Node<OWLNamedIndividual> nod : ret) {
			OWLEntity ent = nod.getRepresentativeElement();

			Set<OWLObjectPropertyAssertionAxiom> OPs = ontology
					.getObjectPropertyAssertionAxioms(ent.asOWLNamedIndividual());
			for (OWLObjectPropertyAssertionAxiom OP : OPs) {
				// System.out.println(OP.getProperty());
			}
		}
	}

	@Test
	public void testGetRangeOfObjectProperties() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		for (Node<OWLNamedIndividual> nod : ret) {
			OWLEntity ent = nod.getRepresentativeElement();

			Set<OWLObjectPropertyAssertionAxiom> OPs = ontology
					.getObjectPropertyAssertionAxioms(ent.asOWLNamedIndividual());
			for (OWLObjectPropertyAssertionAxiom OP : OPs) {
				// System.out.println(OP.getObject().toString());
			}
		}
	}

	@Test
	public void testGetNameOfReferencedObject() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

		NodeSet<OWLNamedIndividual> ret = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		for (Node<OWLNamedIndividual> nod : ret) {
			OWLEntity ent = nod.getRepresentativeElement();

			Set<OWLObjectPropertyAssertionAxiom> OPs = ontology
					.getObjectPropertyAssertionAxioms(ent.asOWLNamedIndividual());
			for (OWLObjectPropertyAssertionAxiom OP : OPs) {

				OWLIndividual range = OP.getObject();
				// System.out.println(range.asOWLNamedIndividual().getIRI().getShortForm());

			}
		}
	}

	@Test
	public void testGetDisjoint() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

		NodeSet<OWLClass> disjointClasses = treasoner
				.getDisjointClasses(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));
		for (Node<OWLClass> cls : disjointClasses) {
			OWLEntity ent = cls.getRepresentativeElement();
			// System.out.println(shortFormProvider.getShortForm(ent));
		}
	}

	@Test
	public void testNotEatingAntelope() throws OWLOntologyCreationException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

		NodeSet<OWLNamedIndividual> instances = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.CARNIVORE)));

		Set<OWLEntity> antelopeEaters = new HashSet<OWLEntity>();
		for (Node<OWLNamedIndividual> instance : instances) {
			OWLEntity ent = instance.getRepresentativeElement();

			Set<OWLObjectPropertyAssertionAxiom> OPs = ontology
					.getObjectPropertyAssertionAxioms(ent.asOWLNamedIndividual());
			for (OWLObjectPropertyAssertionAxiom OP : OPs) {

				OWLIndividual range = OP.getObject();
				if (range.asOWLNamedIndividual().getIRI().getShortForm().equals("Antelope")) {
					antelopeEaters.add(ent);
				}
			}
			if (antelopeEaters.contains(ent) == false) {
				// System.out.println(shortFormProvider.getShortForm(ent));
			}
		}
	}

	@Test
	public void testAddPlant() throws OWLOntologyCreationException, OWLOntologyStorageException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		// real addition starts here

		// get what we want to add
		OWLNamedIndividual Rose = factory.getOWLNamedIndividual(
				IRI.create("http://webprotege.stanford.edu/project/BQ9brKWW6pcSvWvKu3ajh4#Rose"));
		OWLClassAssertionAxiom roseToPlants = factory.getOWLClassAssertionAxiom(
				factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.FLOWER)), Rose);
		NodeSet<OWLNamedIndividual> waters = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.WATER)));
		OWLNamedIndividual freshWater = null;
		for (Node<OWLNamedIndividual> water : waters) {
			OWLNamedIndividual watah = water.getRepresentativeElement();
			if (watah.getIRI().getShortForm().toString().equals("FreshWater")) {
				freshWater = watah;
			}
		}
		OWLObjectProperty roseDrinks = factory.getOWLObjectProperty(
				IRI.create("http://webprotege.stanford.edu/project/BQ9brKWW6pcSvWvKu3ajh4#Drinks"));
		OWLObjectPropertyAssertionAxiom roseDrinksFreshWater = factory.getOWLObjectPropertyAssertionAxiom(roseDrinks,
				Rose, freshWater);

		// create AddAxioms from the additions
		AddAxiom classAssertionAxiom = new AddAxiom(ontology, roseToPlants);
		AddAxiom drinkAssertionAxiom = new AddAxiom(ontology, roseDrinksFreshWater);

		// add the changes to the ontology
		manager.applyChange(classAssertionAxiom);
		manager.applyChange(drinkAssertionAxiom);

		// save the ontology
		manager.saveOntology(ontology);
	}

	@Test
	public void testDeletePlant() throws OWLOntologyCreationException, OWLOntologyStorageException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner treasoner = rf.createReasoner(ontology);
		treasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		// Erasing starts here
		// Get the individual to be removed
		OWLNamedIndividual rose = factory.getOWLNamedIndividual(IRI.create("needToFindTheIndividual"));
		NodeSet<OWLNamedIndividual> flowers = treasoner
				.getInstances(factory.getOWLClass(IRI.create(main.java.ontology.practice.AnimalsIRIs.FLOWER)));
		for (Node<OWLNamedIndividual> water : flowers) {
			OWLNamedIndividual flower = water.getRepresentativeElement();
			if (flower.getIRI().getShortForm().toString().equals("Rose")) {
				rose = flower;
			}
		}

		// create a remover
		OWLEntityRemover eraser = new OWLEntityRemover(ontology);

		// visit the individual with the remover
		rose.accept(eraser);

		// Erase the individual from the ontology
		manager.applyChanges(eraser.getChanges());

		// save the new version of the ontology
		manager.saveOntology(ontology);
	}

	@Test
	public void testFirstQuery() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(IRI.create(main.java.ontology.practice.AnimalsIRIs.ONTOLOGY_ROOT));
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
		AnimalQueries trialOne = new AnimalQueries(manager, ontology, reasoner);
		trialOne.getNotCarnivoreInstances();
	}
}
