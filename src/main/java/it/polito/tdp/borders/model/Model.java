package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country,DefaultEdge> grafo;
	private Map<Integer,Country> idMap;
	private BordersDAO dao;
	private ConnectivityInspector<Country,DefaultEdge> ci;
	private Map<Country,Country> visita;
	
	public Model() {
		
		grafo=new SimpleGraph<>(DefaultEdge.class);
		idMap=new HashMap<>();
		dao=new BordersDAO();
		visita=new HashMap<>();
	
	}
	
	public void creaGrafo(int anno) {
	
		dao.getCountryByYear(anno, idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Border b:dao.getCountryPairs(anno, idMap)) {
			if(this.grafo.containsVertex(b.getC1()) && this.grafo.containsVertex(b.getC2())) {
				DefaultEdge e= this.grafo.getEdge(b.getC1(), b.getC2());
				if(e==null) {
					Graphs.addEdgeWithVertices(grafo, b.getC1(), b.getC2());
				}
			}
			
		}
		
	}
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}

	public Graph<Country, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	public int nComponentiConnesse() {
		ci=new ConnectivityInspector<>(grafo);
		List<Set<Country>> connesse=ci.connectedSets();
		return connesse.size();
	}
	
	public Set<Country> trovaPercorsoConConnInsp(Country c){
		ConnectivityInspector<Country,DefaultEdge> coin=new ConnectivityInspector<>(grafo);
		Set<Country> paesi=coin.connectedSetOf(c);
		return paesi;	
	}
	
	public List<Country> visitaAmpiezza(Country source){
		List<Country> visita=new ArrayList<>();
		
		BreadthFirstIterator<Country, DefaultEdge> bfv= new BreadthFirstIterator<>(grafo,source);
		while(bfv.hasNext()) {
			visita.add(bfv.next());
		}
		
		return visita;
	}

	public void setGrafo(Graph<Country, DefaultEdge> grafo) {
		this.grafo = grafo;
	}
	
	

}
