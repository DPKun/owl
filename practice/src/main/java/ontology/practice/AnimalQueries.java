package org.owlTryTwo;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class AnimalQueries {

	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private ShortFormProvider provider;
	private DLQueryEngine engine;
	
	
	
	public AnimalQueries(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner) {
		super();
		this.manager = manager;
		this.ontology = ontology;
		this.reasoner = reasoner;
		this.provider = new SimpleShortFormProvider();
		this.engine = new DLQueryEngine(reasoner, provider);
	}
	
	public Set<OWLNamedIndividual> getNotCarnivoreInstances(){
		Set<OWLNamedIndividual> ret= new HashSet<OWLNamedIndividual> ();
		engine.getInstances("Animal and not Carnivore", true).forEach(t -> System.out.println(t.getIRI().getShortForm().toString()));
		return ret;
	}

	class DLQueryEngine {
	    private final OWLReasoner reasoner;
	    private final DLQueryParser parser;

	    public DLQueryEngine(OWLReasoner reasoner, ShortFormProvider shortFormProvider) {
	        this.reasoner = reasoner;
	        parser = new DLQueryParser(reasoner.getRootOntology(), shortFormProvider);
	    }

	    public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        NodeSet<OWLClass> superClasses = reasoner
	                .getSuperClasses(classExpression, direct);
	        return superClasses.getFlattened();
	    }

	    public Set<OWLClass> getEquivalentClasses(String classExpressionString) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
	        Set<OWLClass> result = null;
	        if (classExpression.isAnonymous()) {
	            result = equivalentClasses.getEntities();
	        } else {
	            result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
	        }
	        return result;
	        }

	    public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
	        return subClasses.getFlattened();
	        }

	    public Set<OWLNamedIndividual> getInstances(String classExpressionString,
	            boolean direct) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression,
	                direct);
	        return individuals.getFlattened();
	        }
	    }
	
	class DLQueryParser {
	    private final OWLOntology rootOntology;
	    private final BidirectionalShortFormProvider bidiShortFormProvider;

	    public DLQueryParser(OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
	        this.rootOntology = rootOntology;
	        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
	        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
	        // Create a bidirectional short form provider to do the actual mapping.
	        // It will generate names using the input
	        // short form provider.
	        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager,
	                importsClosure, shortFormProvider);
	    }

	
	public OWLClassExpression parseClassExpression(String classExpressionString) {
        OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
                .getOWLDataFactory();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(
                dataFactory, classExpressionString);
        parser.setDefaultOntology(rootOntology);
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        return parser.parseClassExpression();
        }
    }

}
