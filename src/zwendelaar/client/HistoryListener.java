package zwendelaar.client;

import zwendelaar.client.ui.Bieden;
import zwendelaar.client.ui.Blokkeren;
import zwendelaar.client.ui.Statistiek;
import zwendelaar.client.ui.Statistieken;
import zwendelaar.client.ui.UserInfo;
import zwendelaar.client.ui.Inloggen;
import zwendelaar.client.ui.NotificationManager;
import zwendelaar.client.ui.Profiel;
import zwendelaar.client.ui.Registreer;
import zwendelaar.client.ui.Veilingen;
import zwendelaar.client.ui.Zoeken;
import zwendelaar.client.ui.gebruikerInfo;
import zwendelaar.client.ui.veilingToevoegen;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;

public class HistoryListener {
	public HistoryListener() {

		String initToken = History.getToken();

		if(initToken.length() == 0) {
			History.newItem("home");
		} else {
			historyChanged(initToken);
		}
		TheListener();
	}
	/*
	 * HistoryListener checkt de link (#foo) en wanneer dit een bekende pagina is, laad hij deze.
	 */
	private void TheListener() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String token = History.getToken();
				historyChanged(token);				
			}
		});

		//History.fireCurrentHistoryState();
	}

	private void historyChanged(String token) {		
		if(token.equals("home")) {
			Zwendelaar.get().showPage(new Veilingen().createPage());
		}else if(token.equals("registreer")){
			Zwendelaar.get().showPage(new Registreer().createPage());
		}else if(token.startsWith("bieden") && Zwendelaar.get().loggedIn()) {
			if(token.length() < 11) { return; }
			Zwendelaar.get().showPage(new Bieden(token.substring(10)).createpage());
		}else if(token.startsWith("userInfo") && Zwendelaar.get().loggedIn() && Zwendelaar.get().isAdmin()){
			if(token.length() < 15) { return; }
			Zwendelaar.get().showPage(new gebruikerInfo(token.substring(15)).createPage());
		}else if(token.equals("statistieken")  && Zwendelaar.get().loggedIn() && Zwendelaar.get().isAdmin()) {
			Zwendelaar.get().showPage(new Statistieken().createpage());
		}else if(token.startsWith("statistiek")  && Zwendelaar.get().loggedIn() && Zwendelaar.get().isAdmin()) {

			if(token.length() < 15) { return; }
			Zwendelaar.get().showPage(new Statistiek(token.substring(14)).createpage());
		}else if(token.equals("blokkeren") && Zwendelaar.get().loggedIn() && Zwendelaar.get().isAdmin()){
			Zwendelaar.get().showPage(new Blokkeren().createPage());
		}else if(token.equals("zoeken")) {
			Zwendelaar.get().showPage(new Zoeken().createpage());
			if(!Zwendelaar.get().loggedIn()){
				Zwendelaar.get().showElement(new Inloggen().createpage());
			}
		}else if(token.equals("hulp")){
			Zwendelaar.get().showPage(new HTML("<div>" +
					"<b>Concept:</b></br>" +
					"De website Zwendelaar is een veilingsite, gebruikers kunnen interessante aanbiedingen kopen per opbod. </br>" +
					"Elk bod dat je doet, kost je een credit, deze kun je kopen. De prijs van het product waar je op biedt, zal dan ook stijgen met een credit. <br/>" +
					"</br> <b>Verloop van de veiling</b></br> Bij elke veiling staat aangegeven wat voor soort product het is. Er staat een categorie bij en een omschrijving. Daar bij staat er ook hoeveel credits er tot nu toe geboden zijn op het product en op welke datum de veiling afloopt. Zodra u op bieden klikt, komt u op een nieuwe pagina, met alle informatie nog eens kort plus de mogelijkheid om er zelf een bod aan toe te voegen. U specificeert dan uw bod en klikt op bieden. Als u genoeg credits bezit, zal u de melding krijgen dat het bod gemaakt is. </br>" +
					"</br> <b>Aanmelden:</b></br> Je kunt je gratis bij ons <a href='#registreer' alt='registreren'>aanmelden</a>, dit blijft voor altijd gratis. Op deze manier kun je dus eerst goed rondkijken voordat je begint met bieden! </br>" +
					"</div>"));
		}else if(token.equals("veilingtoevoegen") && Zwendelaar.get().loggedIn()) {
			Zwendelaar.get().showPage(new veilingToevoegen().createpage());
		}else if(token.equals("profiel") && Zwendelaar.get().loggedIn()) {
			Zwendelaar.get().showPage(new Profiel().createpage());
		}else if(token.equals("uitloggen")) {
			NotificationManager.get().createNotification("U bent uitgelogd");
			Zwendelaar.get().setGebruiker(null);
			History.newItem("home");
		}
		
		if(!Zwendelaar.get().loggedIn()){
			NotificationManager.get().createNotification("Registreer of login voor alle functies");
			Zwendelaar.get().showElement(new Inloggen().createpage());
		}else{
			Zwendelaar.get().showElement(new UserInfo().createpage());
		}
	}
}
