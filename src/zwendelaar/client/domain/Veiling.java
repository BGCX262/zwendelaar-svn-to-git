package zwendelaar.client.domain;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Veiling implements IsSerializable {
	
	public int ID;
	public int minCredits;
	public String sluitDatum;
	public String beginDatum;
	public ArrayList<Bod> biedingen;
	private Product product;
	private String email;
	
	public Veiling(){ }
	
	public Veiling(String email, int ID, int credits, String sDatum, String bDatum, Product p){
		this.email = email;
		this.ID = ID;
		product = p;
		minCredits = credits;
		sluitDatum = sDatum;
		beginDatum = bDatum;
		biedingen = new ArrayList<Bod>();
	}
	public int getID()
	{
		return ID;
	}
	
	public String getEmail(){
		return email;
	}
	
	public Product getProduct(){
		return product;
	}
	
	public int getLastBieding(){
		Bod bod = null;
		int credits = 0;
		if(biedingen.size() == 0){
			return 0;
		}else{
			bod = biedingen.get(0);
			credits = bod.getCredits();
		}
		return credits;
	}
	
	public ArrayList<Bod> getBiedingenGebruiker(String eM){
		ArrayList<Bod> bods = new ArrayList<Bod>();
		for(Bod b : biedingen){
			if(b.getEmail().equals(eM)){
				bods.add(b);
			}
		}
		return bods;
	}
	public Bod getHighBiedingUser(String eM){
		Bod bod = null;
		for(Bod b : biedingen){
			if(b.getEmail().equals(eM)){
				bod = b;
				break;
			}
		}
		return bod;
	}
	public ArrayList<Bod> getAlleBiedingen()
	{
		return biedingen;
	}
	
	public void addBod(Bod b)
	{
		if(!biedingen.contains(b))
		{
			biedingen.add(b);
		}
	}
	
	public HashMap<Boolean, Integer> getMinBod() {
		HashMap<Boolean, Integer> result = new HashMap<Boolean, Integer>();
		if (biedingen.size() == 0)
			result.put(true, this.minCredits);
		else
			result.put(false, biedingen.get(0).getCredits());
		return result;
	}
	
	public int getMinCredits(){
		return minCredits;
	}
	
	public String getSluitDatum(){
		return sluitDatum;
	}
	
	public String getBeginDatum(){
		return beginDatum;
	}
}
