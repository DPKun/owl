package ontology.practice;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologySingletonSetProvider;
import org.semanticweb.owlapi.util.PropertyAssertionValueShortFormProvider;

import com.google.common.collect.Multimap;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * Tries with the OWL api
 */
public class App {
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		File ontology = new File("sources/ILONA-ontology.owx");
		
		// the OWLOntologyManager implementation with the OWLManager
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		//datafactory
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		
		// loading the ontology itself
		OWLOntology first = manager.loadOntology(IRI.create(ontology));
		
		//OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(first);
		
		//set for zone objects
		Set<Zone> zones= new HashSet<Zone>(); 

	    //get the class IRIs
		Set<OWLClass> classes = first.getClassesInSignature();
	    IRI zone = IRI.create("Ezt nem kéne visszakapni");
	    for (OWLClass cs : classes){
	    
	    	//Get the Zone IRI
	    	if(EntitySearcher.getAnnotations(cs, first, df.getRDFSLabel()).toString().contains("\"Zone\"")){
	    		System.out.println(cs.getIRI());
	    		zone = cs.getIRI();
	    	}
	    }
	    
	    Set<OWLNamedIndividual> individuals = first
				.getIndividualsInSignature();
		//get Individual IRI
	    for (OWLNamedIndividual individum : individuals) {
			IRI iri = individum.getIRI();
			Zone zozo = new Zone("0","0");
			//check if it has the Zone IRI
			if(EntitySearcher.getTypes(individum, first).toString().contains(zone)){
			
			
				
			Set<OWLAnnotationAssertionAxiom> annotations = first
					.getAnnotationAssertionAxioms(individum.getIRI());
			
			Set<OWLDataPropertyAssertionAxiom> dpaas = first
					.getDataPropertyAssertionAxioms(individum);
			//create a Zone object
			for (OWLDataPropertyAssertionAxiom dpaa : dpaas) {

				OWLDataPropertyExpression dpe = dpaa.getProperty();
				
				//System.out.println("\t\t" + dpe.asOWLDataProperty().getIRI()+"\n"
				//		+ " -> "
						if(annotationName(dpe.asOWLDataProperty().getIRI(), first).contains("Id")){
							zozo.setID(dpaa.getObject().toString());
						}else if(annotationName(dpe.asOWLDataProperty().getIRI(), first).contains("Name")){
							zozo.setName(dpaa.getObject().toString());
							
						}
						zones.add(zozo);
			}
	    
			
			
	    /*for (OWLClass cls : first.getClassesInSignature()) {
			  
			 // for(OWLAnnotation ann : EntitySearcher.getAnnotations(cls, first, df.getRDFSLabel())){
		System.out.println(cls.toString());
			
		  Set<OWLNamedIndividual> individuals = cls.getIndividualsInSignature();
		  
		  System.out.println(individuals);
				  for(OWLNamedIndividual in : individuals){
					
			*/		
				  
				  /*  if(val.toString().equals("\"Zone\"")){
					  for(OWLIndividual in : EntitySearcher.getIndividuals(cls, first)){
						Multimap<OWLDataPropertyExpression,OWLLiteral> props = EntitySearcher.getDataPropertyValues(in, first);
						//System.out.println(props.toString());
						
						Set<String> value = new HashSet<String>();
						for(OWLDataPropertyExpression exp : props.keySet()){
							System.out.println();
							//System.out.println(EntitySearcher.getDataPropertyValues(in, exp, first));
								
							//System.out.println(exp.asOWLDataProperty().toStringID() + "<------------");
						//value.add(EntitySearcher.getDataPropertyValues(in, exp, first).toString());	
							
						}
						System.out.println(value);
							/* for(OWLAnnotation an : anns){
								 OWLAnnotationValue va = an.getValue();
								 System.out.println(va.toString());*/
							// }*/
				  }
			
			
		  }
		System.out.println(zones.toString());
	}
	private static String annotationName(IRI iri, OWLOntology ontology) {
		Set<OWLAnnotationAssertionAxiom> annotations = ontology
				.getAnnotationAssertionAxioms(iri);
		return annotations.iterator().next().getValue().toString();
	}
	
}