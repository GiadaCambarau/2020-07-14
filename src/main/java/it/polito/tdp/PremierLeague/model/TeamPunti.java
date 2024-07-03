package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class TeamPunti implements Comparable<TeamPunti> {
	private Team t;
	private int  punti;
	public TeamPunti(Team t, int punti) {
		super();
		this.t = t;
		this.punti = punti;
	}
	public Team getT() {
		return t;
	}
	public void setT(Team t) {
		this.t = t;
	}
	public int getPunti() {
		return punti;
	}
	public void setPunti(int punti) {
		this.punti = punti;
	}
	@Override
	public int hashCode() {
		return Objects.hash(punti, t);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamPunti other = (TeamPunti) obj;
		return punti == other.punti && Objects.equals(t, other.t);
	}
	@Override
	public int compareTo(TeamPunti o) {
		return o.punti-this.punti;
	}
	@Override
	public String toString() {
		return  t + "   " + punti ;
	}
	
	
	
	

}
