package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllTeams(Map <Integer, Team> idMap){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				idMap.put(team.getTeamID(), team);
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<Integer, Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		Map <Integer, Match> mappa= new HashMap <Integer, Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				mappa.put(match.getMatchID(), match);
			}
			conn.close();
			return mappa;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Adiacenza> getAdiacenze(Map<Integer, Team> idMap){
		String sql="SELECT m1.TeamHomeID as t1, m2.TeamHomeID as t2, m1.ResultOfTeamHome AS ris1, m2.ResultOfTeamHome AS ris2 "
				+ "FROM matches m1, matches m2 "
				+ "WHERE m1.TeamHomeID> m2.TeamHomeID "
				+ "GROUP BY m1.TeamHomeID, m2.TeamHomeID";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			double punteggio1=0;
			double punteggio2=0;
			double punteggio=0;
			while (res.next()) {
				Team t1= idMap.get(res.getInt("t1"));
				Team t2= idMap.get(res.getInt("t2"));
				double res1=res.getDouble("ris1");
				double res2= res.getDouble("ris2");
				if(res1==1 && res2!=1) {
					punteggio1=punteggio1+3;
					
				}
				if(res2==1 && res1!=1) {
					punteggio2=punteggio2+3;
				}
				if(res2==0 && res1==0) {
					punteggio1=punteggio1+1;
					punteggio2=punteggio2+1;
				}
				
				punteggio=punteggio1-punteggio2;
				if(punteggio>0) {
					Adiacenza a= new Adiacenza(t1,t2,punteggio);
					result.add(a);
				}
				else if(punteggio <0) {
					Adiacenza a = new Adiacenza (t2,t1, (punteggio2-punteggio1));
					result.add(a);
				}
				
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Team> calcolaPunteggio (Map <Integer, Team> idMap) {
		String sql="SELECT t1.TeamID as t1, t2.TeamID as t2, m.ResultOfTeamHome as r "
				+ "FROM matches m, teams t1, teams t2 "
				+ "WHERE t1.TeamID=m.TeamHomeID AND t2.TeamID= m.TeamAwayID "
				+ "";
		
		
		List<Team> result = new ArrayList<Team>(idMap.values());
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Team t1= idMap.get(res.getInt("t1"));
				Team t2= idMap.get(res.getInt("t2"));
				
				if(res.getInt("r")==1 ) {
					t1.setPunteggio(t1.getPunteggio()+3);
					
				}
				
				else if(res.getInt("r")==0 ) {
					t1.setPunteggio(t1.getPunteggio()+1);
					t2.setPunteggio(t2.getPunteggio()+1);
				}
				else if(res.getInt("r")==-1) {
					t2.setPunteggio(t2.getPunteggio()+3);
				}
			}
			Collections.sort(result);
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
