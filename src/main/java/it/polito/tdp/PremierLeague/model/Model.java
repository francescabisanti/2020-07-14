package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	PremierLeagueDAO dao;
	SimpleDirectedWeightedGraph <Team, DefaultWeightedEdge>grafo;
	Map <Integer, Team> idMap;
	
	public Model() {
		dao= new PremierLeagueDAO();
		idMap= new HashMap <Integer, Team>();
		
	}
	
	public void creaGrafo() {
		grafo= new SimpleDirectedWeightedGraph <Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		dao.listAllTeams(idMap);
		//Aggiunta vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		dao.calcolaPunteggio(idMap);
		for(Team t1: dao.calcolaPunteggio(idMap)) {
			for(Team t2:dao.calcolaPunteggio(idMap)) {
				DefaultWeightedEdge e = grafo.getEdge(t2, t1);
				if(!t1.equals(t2) && e==null) {
					int punteggio= t1.getPunteggio()-t2.getPunteggio();
					if(punteggio>0)
						Graphs.addEdgeWithVertices(this.grafo, t1, t2, punteggio);
					else if(punteggio <0) {
						Graphs.addEdgeWithVertices(this.grafo, t2, t1, t2.getPunteggio()-t1.getPunteggio());
					}
				}
			}
		}
	}
	public List <SquadraDifferenza> classificaPeggiori(Team t){
		List <SquadraDifferenza> peggiori= new ArrayList <SquadraDifferenza>();
		
		for(Team tt: Graphs.successorListOf(grafo, t)) {
				DefaultWeightedEdge e= grafo.getEdge(t, tt);
				SquadraDifferenza s= new SquadraDifferenza (tt, grafo.getEdgeWeight(e));
				peggiori.add(s);
		}
		Collections.sort(peggiori);
		
		
		return peggiori;
	}
	
	public List <SquadraDifferenza> classificaMigliori(Team t){
		List <SquadraDifferenza> migliori= new ArrayList <SquadraDifferenza>();
		
		for(Team tt: Graphs.predecessorListOf(grafo, t)) {
				DefaultWeightedEdge e= grafo.getEdge(tt, t);
				SquadraDifferenza s= new SquadraDifferenza (tt, grafo.getEdgeWeight(e));
				migliori.add(s);
		}
		Collections.sort(migliori);
		
		
		return migliori;
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public PremierLeagueDAO getDao() {
		return dao;
	}

	public void setDao(PremierLeagueDAO dao) {
		this.dao = dao;
	}

	public SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public Map<Integer, Team> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<Integer, Team> idMap) {
		this.idMap = idMap;
	}
	
	
}
