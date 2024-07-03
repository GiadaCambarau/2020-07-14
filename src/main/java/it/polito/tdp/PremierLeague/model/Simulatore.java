package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulatore {
	private Graph<Team, DefaultWeightedEdge> garfo;
	private int nReporter;
	private int criticita;
	 
	private PriorityQueue<Event> queue;
	private List<Team> allTeams;
	private List<Match> allMatches;
	private Map<Integer, Team> mappaTeam;
	
	private Map<Team, Integer> reporterAssegnati;
	private double mediaReporter;
	private Model model;
	private int nGCritici;
	
	public Simulatore(Graph<Team, DefaultWeightedEdge> garfo, List<Match> allMatches, List<Team> allTeams) {
		super();
		this.garfo = garfo;
		this.allMatches = allMatches;
		this.allTeams = allTeams;
	}
	
	public void initialize(int n, int x) {
		this.criticita =x;
		this.nReporter =n;
		this.nGCritici =0;
		this.queue = new PriorityQueue<>();
		this.reporterAssegnati = new HashMap<>();
		this.mediaReporter =0;
		//riempi la mappa con i team
		for (Team t: allTeams) {
			reporterAssegnati.put(t, nReporter);
			mappaTeam.put(t.teamID, t);
		}
		this.model = new Model();
		
		
		//riempi la coda 
		Collections.sort(allMatches);
		int i =0;
		for (Match m: allMatches) {
			if (m.getResultOfTeamHome()==1){
				queue.add(new Event(EventType.vittoria, i, mappaTeam.get(m.teamHomeID), mappaTeam.get(m.teamAwayID)));
			}else if (m.getResultOfTeamHome()== -1) {
				queue.add(new Event(EventType.sconfitta, i, mappaTeam.get(m.teamHomeID), mappaTeam.get(m.teamAwayID)));
			}else if (m.getResultOfTeamHome()==0) {
				queue.add(new Event(EventType.pareggio, i, mappaTeam.get(m.teamHomeID), mappaTeam.get(m.teamAwayID)));
			}
			i++;
		}
	
	}
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processa(e);
		}
	}

	private void processa(Event e) {
		EventType type = e.getType();
		Team t1 = e.getT1();
		Team t2 = e.getT2();
		int tempo = e.getTempo();
		switch (type) {
		case pareggio:
			if (reporterAssegnati.get(t1)<criticita ) {
				nGCritici++;
			}
			
			break;
		case sconfitta:
			double random3 = Math.random();
			if (random3<0.2) {
				if (reporterAssegnati.get(t1)>0) {
					double r = Math.random();
					int nR = (int) (r*reporterAssegnati.get(t1));
					List<TeamPunti> peggiori = model.getPeggiori(t1);
					if (peggiori.size()>0) {
						double random4 = Math.random();
						int indice = (int) (random4*peggiori.size());
						Team nuovo = peggiori.get(indice).getT();
						reporterAssegnati.put(nuovo, reporterAssegnati.get(nuovo)+nR);
						reporterAssegnati.put(t1, reporterAssegnati.get(t1)-nR);
						if (reporterAssegnati.get(t1)<criticita || reporterAssegnati.get(nuovo)<criticita ) {
							nGCritici++;
						}
					}
				}
			}
				
			
			break;
		case vittoria:
			double random = Math.random();
			if (random<0.5) {
				List<TeamPunti> migliori = model.getMigliori(t1);
				if (migliori.size()>0) {
					double random2 = Math.random();
					int indice = (int) (random2*migliori.size());
					Team nuovo = migliori.get(indice).getT();
					if (reporterAssegnati.get(t1)>0) {
						//aggiungi al nuovo, togli al vecchio
						reporterAssegnati.put(nuovo, reporterAssegnati.get(nuovo)+1);
						reporterAssegnati.put(t1, reporterAssegnati.get(t1)-1);
						if (reporterAssegnati.get(t1)<criticita || reporterAssegnati.get(nuovo)<criticita ) {
							nGCritici++;
						}
					}
				}
			}
			break;

		
		
	}
	
	}
	public int getG(){
		return nGCritici;
	}
	
	
}
