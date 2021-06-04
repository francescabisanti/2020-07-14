package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulatore {
	//parametri iniziali
	private int N;
	private int x;
	private List <Match> match;
	private List <Team> squadre;
	
	//Stato del mondo
	Map <Team,Integer> mappaReporter;
	//coda degli eventi
	PriorityQueue <Event> queue;
	
	//parametri di Output
	int rAssistenti;
	int nPartiteDeficit;
	
	
	public void init(int N, int x, List <Match> matches, List <Team> squadre) {
		mappaReporter= new HashMap <Team, Integer>();
		this.N=N;
		this.x=x;
		this.match=matches;
		this.squadre=squadre;
		this.rAssistenti=0;
		this.nPartiteDeficit=0;
		for(Team t: this.squadre) {
			this.mappaReporter.put(t, N);
		}
		
	}
	
	public void run(int x) {
		for(Match m: this.match) {
			Team vincente=null;
			Team perdente= null;
			Team casa= new Team(m.getTeamHomeID(), m.getTeamHomeNAME());
			Team ospite= new Team (m.getTeamAwayID(), m.getTeamAwayNAME());
			int res= m.getReaultOfTeamHome();
			int totReporter= mappaReporter.get(casa)+mappaReporter.get(ospite);
			this.rAssistenti=rAssistenti+ mappaReporter.get(casa)+mappaReporter.get(ospite);
			if(totReporter<x)
				this.nPartiteDeficit++;
			if(res==1) {
				vincente=casa;
				perdente=ospite;
				
				
			}else if(res==-1) {
				vincente=ospite;
				perdente=casa;
			}
			if(Math.random()>0.50) {
				if(this.mappaReporter.get(vincente) > 0) {
				mappaReporter.replace(vincente, mappaReporter.get(vincente), mappaReporter.get(vincente)-1);
				int index = (int) (Math.round((Math.random()*1)+1));
				Team t= this.squadre.get(index);
				this.mappaReporter.replace(t, mappaReporter.get(t), mappaReporter.get(t)+1);
				}
			}
			if(Math.random()>0.80) {
				if(mappaReporter.get(perdente)>0) {
					
					int index = (int) (Math.round((Math.random()*1)+1));
					Team t= this.squadre.get(index);
					this.mappaReporter.replace(t, mappaReporter.get(t), mappaReporter.get(t)+1);
					
				}
			}
				
		}
	}
}
