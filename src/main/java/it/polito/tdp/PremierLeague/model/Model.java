package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Graph<Team, DefaultWeightedEdge> garfo;
	private PremierLeagueDAO dao;
	private List<TeamPunti> teamPunti;

	public Model() {
		this.garfo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo() {
		Graphs.addAllVertices(this.garfo, dao.listAllTeams());
		this.teamPunti = new ArrayList<>();
		
		for (Team t : this.garfo.vertexSet() ) {
			int punti =0;
			for (Match m: dao.listAllMatches()) {
				if (m.getTeamHomeID() == t.getTeamID() && m.getResultOfTeamHome() ==1 ) {
					punti+=3;
				}else if(m.getTeamAwayID() == t.getTeamID()&& m.getResultOfTeamHome()==-1) {
					punti+=3;
				}else if((m.getTeamHomeID() == t.getTeamID() || m.getTeamAwayID() == t.getTeamID() ) &&
						m.resultOfTeamHome ==0) {
					punti++;
				}
			}
			teamPunti.add(new TeamPunti(t, punti));
		}
		
		for (TeamPunti t1: teamPunti) {
			for (TeamPunti t2: teamPunti) {
				if (!t1.equals(t2)) {
					int punti = t1.getPunti()-t2.getPunti();
					if (punti>0) {
						Graphs.addEdgeWithVertices(this.garfo, t1.getT(), t2.getT(), punti);
					}else if (punti<0) {
						Graphs.addEdgeWithVertices(this.garfo, t2.getT(), t1.getT(), (punti)*(-1));
					}
				}
			}
			
		}
		
		
	}
	public int getV() {
		return this.garfo.vertexSet().size();
	}

	public int getA() {
		return this.garfo.edgeSet().size();
	}
	public List<Team> getTeams(){
		return dao.listAllTeams();
	}
	
	public List<TeamPunti> getMigliori(Team t) {
		TeamPunti tp = null;
		for (TeamPunti t1: this.teamPunti ) {
			if (t1.getT().equals(t)) {
				tp = t1;
			}
		}
		List<TeamPunti> migliori = new ArrayList<>();
		for (TeamPunti t1: this.teamPunti) {
			if (!t1.getT().equals(t)) {
				if (t1.getPunti()>tp.getPunti()) {
					migliori.add(new TeamPunti(t1.getT(), t1.getPunti()-tp.getPunti()));
				}
			}
		}
		Collections.sort(migliori);
		return migliori;
		
	}
	public List<TeamPunti> getPeggiori(Team t) {
		TeamPunti tp = null;
		for (TeamPunti t1: this.teamPunti ) {
			if (t1.getT().equals(t)) {
				tp = t1;
			}
		}
		List<TeamPunti> peggiori = new ArrayList<>();
		for (TeamPunti t1: this.teamPunti) {
			if (!t1.getT().equals(t)) {
				if (t1.getPunti()<tp.getPunti()) {
					peggiori.add(new TeamPunti(t1.getT(), tp.getPunti()-t1.getPunti()));
				}
			}
		}
		Collections.sort(peggiori);
		return peggiori;
		
	}
	public void simula (int n, int x) {
		Simulatore sim = new Simulatore(garfo, dao.listAllMatches(), getTeams());
		sim.initialize(n, x);
		sim.run();
		
	}
	
	
	
	
	
}
