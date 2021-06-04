package it.polito.tdp.PremierLeague.model;

public class SquadraDifferenza implements Comparable <SquadraDifferenza> {
	private Team team;
	private double diff;
	public SquadraDifferenza(Team team, double diff) {
		super();
		this.team = team;
		this.diff = diff;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public double getDiff() {
		return diff;
	}
	public void setDiff(double diff) {
		this.diff = diff;
	}
	@Override
	public int compareTo(SquadraDifferenza o) {
		
		return (int) (this.getDiff()-o.getDiff());
	}
	@Override
	public String toString() {
		return team + " ( " + diff + ")";
	}
	
	
	
	
}
