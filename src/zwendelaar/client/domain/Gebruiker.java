package zwendelaar.client.domain;

import zwendelaar.client.Zwendelaar;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Gebruiker implements IsSerializable {
	private boolean admin = false;

	private String voorNaam;
	private String achterNaam;

	private String woonPlaats;
	private String postCode;
	private String straatNaam;
	private String huisNummer;

	private int credits;
	private String emailAdres;
	
	private boolean geblokkeerd = false;

	public Gebruiker () {}
	public Gebruiker(String vN, String aN, 
			String wP, String pC, String sN, String hN,
			int credits, String eA)
	{
		voorNaam = vN;
		achterNaam = aN;
		setWoonPlaats(wP);
		setPostCode(pC);
		setStraatNaam(sN);
		setHuisNummer(hN);
		this.credits = credits;
		emailAdres = eA;
	}

	public boolean getAdmin() {
		return admin;
	}
	public String getNaam(){
		return voorNaam+" "+achterNaam;
	}
	
	public String getVoornaam()
	{
		return voorNaam;
	}

	public String getAchternaam()
	{
		return achterNaam;
	}
	
	public boolean getBlock()
	{
		return geblokkeerd;
	}
	
	public void setAdmin(boolean b) {
		admin = b;
	}
	public void setWoonPlaats(String woonPlaats) {
		this.woonPlaats = woonPlaats;
	}
	public String getWoonPlaats() {
		return woonPlaats;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setStraatNaam(String straatNaam) {
		this.straatNaam = straatNaam;
	}
	public String getStraatNaam() {
		return straatNaam;
	}
	public void setHuisNummer(String huisNummer) {
		this.huisNummer = huisNummer;
	}
	public String getHuisNummer() {
		return huisNummer;
	}
	
	public void setGeblokkeerd(boolean b)
	{
		geblokkeerd = b;
	}
	
	public void addCredits(int credits) {
		this.credits += credits;
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Boolean result) {

			}
			
		};
		Zwendelaar.get().updateGebruiker(this, callback);
	}
	public void substractCredits(int credits) {
		this.credits = this.credits - credits;
	}
	public int getCredits() {
		return this.credits;
	}
	
	public String getEmailAdres() {
		return emailAdres;
	}
}
