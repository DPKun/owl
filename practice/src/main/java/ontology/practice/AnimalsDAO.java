package main.java.ontology.practice;

import java.util.Collection;

import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public interface AnimalsDAO {

	public Collection<Animal> readAnimals();

	public void createAnimals(Collection<Animal> animals) throws OWLOntologyStorageException;

	public void updateAnimals(Collection<Animal> animals) throws OWLOntologyStorageException;

	public void deleteAnimal(Animal animal) throws OWLOntologyStorageException;

}
