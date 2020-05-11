package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public void getCountryByYear(int anno, Map<Integer,Country> idMap){
		String sql = "SELECT *, COUNT(*) AS totPerStato " + 
				"FROM country AS cou, contiguity AS con " + 
				"WHERE cou.CCode=con.state1no AND con.conttype=1 AND con.year<=? " + 
				"GROUP BY cou.CCode";
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c=new Country(rs.getInt("CCode"),rs.getString("StateAbb"),rs.getString("StateNme"));
				if(!idMap.containsKey(c.getCodice())) {
					idMap.put(c.getCodice(), c);
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}	
	}

	public List<Border> getCountryPairs(int anno, Map<Integer,Country> idMap) {
		String sql = "SELECT state1no, state2no " + 
				"FROM contiguity " + 
				"WHERE YEAR<=? AND conttype=1";
		
		List<Border> coppie=new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(idMap.containsKey(rs.getInt("state1no")) && idMap.containsKey(rs.getInt("state2no"))) {
					coppie.add(new Border(idMap.get(rs.getInt("state1no")),idMap.get(rs.getInt("state2no"))));
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return coppie;
	}
}
