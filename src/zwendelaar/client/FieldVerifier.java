package zwendelaar.client;

import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.ui.NotificationManager;
import zwendelaar.client.ui.UserInfo;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FieldVerifier {

	private static FieldVerifier fv;
	
	private FieldVerifier(){ }
	
	public static FieldVerifier get() {
		if(fv == null) {
			fv = new FieldVerifier();
		}
		return fv;
	}
	
	
	public boolean checkField(String text, int minLength, int maxLength) {
		if(text.length() >= minLength && text.length() <= maxLength) {
			return true;
		} else {
			return false;
		}	
	}
	
	public void checkField(Widget w) {
		if(w instanceof TextBox) {
			if(FieldVerifier.get().checkField(((TextBox) w).getText(), 3, 30)) { 
				w.setStyleName("gwt-textBoxG");
			} else {
				w.setStyleName("gwt-textBoxR");
			}
		} else if(w instanceof SuggestBox) { 
			if(FieldVerifier.get().checkField(((SuggestBox) w).getText(), 3, 30)) { 
				w.setStyleName("gwt-textBoxG");
			} else {
				w.setStyleName("gwt-textBoxR");
			}
		} else if(w instanceof TextArea) {
			if(FieldVerifier.get().checkField(((TextArea) w).getText(), 3, 500)) { 
				w.setStyleName("gwt-textBoxG");
			} else {
				w.setStyleName("gwt-textBoxR");
			}
		}
	}
	
	public void checkRegister(final String vN, final String aN, final String str, final String hN,
							final String pC, final String wP, final String eM, final String wW){
		AsyncCallback<java.lang.Boolean> callback = new AsyncCallback<java.lang.Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.getStackTrace();
				NotificationManager.get().createNotification("Er is een server fout opgetreden. Excuses voor het ongemak.");
			}
			@Override
			public void onSuccess(java.lang.Boolean result) {
				if(result) {
					NotificationManager.get().createNotification("U bent succesvol geregistreerd met gebruikersnaam: " + eM + ".");
					History.newItem("home");
				} else {
					NotificationManager.get().createNotification("De door u ingestuurde gebruiker bestaat al.");
				}
			}
		};
		new Zwendelaar().registerUser(vN, aN, str, hN, pC, wP, eM, wW, callback);
	}
	
	public void checkLogin(final String gN, final String ww){
		AsyncCallback<Gebruiker> callback = new AsyncCallback<Gebruiker>() {
			
			
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Uw gebruikersnaam/wachtwoord combinatie is niet bij ons bekend.");
				NotificationManager.get().createNotification("Er is een server fout opgetreden. Excuses voor het ongemak.");
			}
			@Override
			public void onSuccess(Gebruiker result) {
				if(result == null) {
					NotificationManager.get().createNotification("Uw gebruikersnaam/wachtwoord combinatie is niet bij ons bekend.");					
				}
				if(result.getBlock() == true){
					NotificationManager.get().createNotification("Uw account is geblokkeerd, neem contact op met de beheerder van de site.");
				}
				else {
					NotificationManager.get().createNotification("Welkom " + result.getVoornaam() + " " + result.getAchternaam() + "! U bent succesvol ingelogd.");
					Zwendelaar.get().setGebruiker(result);
					if(History.getToken() == "profiel"){
						History.newItem("profiel");
					}else{
						History.newItem("profiel");
					}
					
					Zwendelaar.get().showElement(new UserInfo().createpage());
					Zwendelaar.get().createNavigation();
				}
			}
		};
		Zwendelaar.get().loginUser(gN, ww, callback);
	}
}
