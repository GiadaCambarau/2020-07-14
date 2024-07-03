package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class Event implements Comparable<Event>{
	public enum EventType{
		vittoria,
		sconfitta,
		pareggio
	}
	private EventType type;
	private int tempo;
	private Team t1;
	private Team t2;
	public Event(EventType type, int tempo, Team t1, Team t2) {
		super();
		this.type = type;
		this.tempo = tempo;
		this.t1 = t1;
		this.t2 = t2;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public int getTempo() {
		return tempo;
	}
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	public Team getT1() {
		return t1;
	}
	public void setT1(Team t1) {
		this.t1 = t1;
	}
	public Team getT2() {
		return t2;
	}
	public void setT2(Team t2) {
		this.t2 = t2;
	}
	@Override
	public int hashCode() {
		return Objects.hash(t1, t2, tempo, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2) && tempo == other.tempo
				&& type == other.type;
	}
	@Override
	public int compareTo(Event o) {
		return this.tempo-o.tempo;
	}
	
	
	
}
