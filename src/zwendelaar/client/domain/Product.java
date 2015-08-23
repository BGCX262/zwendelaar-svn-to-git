package zwendelaar.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Product implements IsSerializable{
	
	private int id;
	private String naam;
	private String status;
	private String omschrijving;
	private String merk;
	private String categorie;
	private boolean geblokkeerd = false;

	public Product() { }
	public Product(int id, String nm, String st, String om, String mk, String cat){
		this.id = id;
		naam = nm;
		status = st;
		omschrijving = om;
		merk = mk;
		categorie = cat;
		
	}
	public boolean getBlock()
	{
		return geblokkeerd;
	}
	public void setGeblokkeerd(boolean b)
	{
		geblokkeerd = b;
	}
	
	public int getID(){
		return id;
	}
	
	public String getNaam()
	{
		return naam;
	}
	public String getOmschrijving()
	{
		return omschrijving;
	}
	public String getMerk()
	{
		return merk;
	}
	
	public String getCategorie()
	{
		return categorie;
	}
}
