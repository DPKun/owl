package org.owlTryTwo;

import java.util.Set;

public class Animal {
	private String Species;
	private String Size;
	private Set<String> Food;
	
	public Animal(String species,String size, Set<String> food) {
		super();
		Species=species;
		Size = size;
		Food = food;
	}

	public String getSpecies() {
		return Species;
	}

	public void setSpecies(String species) {
		Species = species;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public Set<String> getFood() {
		return Food;
	}

	public void setFood(Set<String> food) {
		Food = food;
	}

	@Override
	public String toString() {
		return "Animal [Species=" + Species + ", Size=" + Size + ", Food=" + Food + "]";
	}
	
	

}
