package zwendelaar.server;

import java.util.ArrayList;
import zwendelaar.client.domain.Veiling;

public class AlleVeilingen {
	private static AlleVeilingen aV;
	private ArrayList<Veiling> alleVeilingen = new ArrayList<Veiling>();
	
	private AlleVeilingen(){}
	
	public static AlleVeilingen get() {
		if(aV == null) {
			aV = new AlleVeilingen();
		} 
		return aV;
	}
	
	public boolean addVeiling(Veiling v){
		return alleVeilingen.add(v);
	}
	
	public ArrayList<Veiling> getVeilingen(){
		return alleVeilingen;
	}
	
	public ArrayList<Veiling> getVeilingenGebruiker(String email){
		ArrayList<Veiling> tempV = new ArrayList<Veiling>();
		for(Veiling v : alleVeilingen){
			if(v.getBiedingenGebruiker(email).equals(email)) {
				tempV.add(v);
			}
		}
		return tempV;
	}
	
	

}
