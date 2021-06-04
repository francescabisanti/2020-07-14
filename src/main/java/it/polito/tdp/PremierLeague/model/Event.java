package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable <Event>{
	public enum EventType{
		SQUADRA_VINCENTE,
		SQUADRA_PERDENTE,
		PAREGGIO,
	}
	
	private EventType tipo;
	private int tempo;
	public Event(EventType tipo, int tempo) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	public int getTempo() {
		return tempo;
	}
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	@Override
	public int compareTo(Event o) {
		
		return this.tempo-o.tempo;
	}
	
	
}
