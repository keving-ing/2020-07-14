package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Team, DefaultWeightedEdge> grafo;
	List<Team> vertici;
	List<Team> migliori = new LinkedList<Team>();
	
	public Model()
	{
		dao = new PremierLeagueDAO();
	}
	
	public String creaGrafo()
	{
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		vertici = dao.listAllTeams();
		Graphs.addAllVertices(grafo, vertici);
		
		List<Match> partite = dao.listAllMatches();
		
		for(Match m:partite)
		{
			if(m.getReaultOfTeamHome()== 0) {
				
				for(Team t:vertici)
				{
					if(t.getTeamID() == m.getTeamHomeID())
					{
						t.pareggia();
					}
					
					if(t.getTeamID() == m.getTeamAwayID())
					{
						t.pareggia();
					}
				}
				
			}
			
			if(m.getReaultOfTeamHome()== 1) {
				
				for(Team t:vertici)
				{
					if(t.getTeamID() == m.getTeamHomeID())
					{
						t.vince();
					}
				}
				
			}

			if(m.getReaultOfTeamHome()== -1) {
				
				for(Team t:vertici)
				{
					if(t.getTeamID() == m.getTeamAwayID())
					{
						t.vince();
					}
				}
	
			}
		}
		
		Collections.sort(vertici);
		
		for(Team t1:vertici)
		{
			for(Team t2:vertici)
			{
				if(t1.getPunteggio()!=t2.getPunteggio() && (t1.getPunteggio() - t2.getPunteggio())>0)
				{
					Graphs.addEdgeWithVertices(grafo, t1, t2, (t1.getPunteggio() - t2.getPunteggio()));
				}
			}
		}
		return "Grafo creato\n# VERTICI: " + grafo.vertexSet().size() + "\n# ARCHI: " + grafo.edgeSet().size();
		
	}

	public List<Team> getVertici() {
		return vertici;
	}
	
	public List<Team> getPeggiori(Team t)
	{
		List<Team> peggiori = Graphs.successorListOf(grafo, t);
		migliori = Graphs.predecessorListOf(grafo, t);
		
		return peggiori;
	}

	public List<Team> getMigliori() {
		
		Collections.sort(migliori, new Comparator<Team>() {
            @Override
            public int compare(Team m1, Team m2) {
                return (m1.punteggio - m2.punteggio);
            }
        });
		return migliori;
	}
	
	
	
	
}
