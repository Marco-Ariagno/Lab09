package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country,DefaultEdge> grafo;
	private Map<Integer,Country> idMap;
	private BordersDAO dao;
	private ConnectivityInspector<Country,DefaultEdge> ci;
	
	public Model() {
		
		grafo=new SimpleGraph<>(DefaultEdge.class);
		idMap=new HashMap<>();
		dao=new BordersDAO();
	
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

	public void setGrafo(Graph<Country, DefaultEdge> grafo) {
		this.grafo = grafo;
	}
	
	

}
