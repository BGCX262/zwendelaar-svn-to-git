package zwendelaar.client.ui;

import zwendelaar.client.FieldVerifier;
import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class gebruikerInfo {
	VerticalPanel content = new VerticalPanel();
	HorizontalPanel main = new HorizontalPanel();
	Grid grid = new Grid(6, 2);
	Gebruiker gebruiker;
	
	

	public gebruikerInfo(String email){
		AsyncCallback<Gebruiker> callback = new AsyncCallback<Gebruiker>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();				
			}

			@Override
			public void onSuccess(Gebruiker result) {
				if(result == null){ }
				if(result != null){
					gebruiker = result;
					final TextBox txtVoornaam = new TextBox();
					txtVoornaam.setStyleName("gwt-textBox");
					txtVoornaam.setText(result.getVoornaam());
					final TextBox txtAchternaam = new TextBox();
					txtAchternaam.setText(result.getAchternaam());
					txtAchternaam.setStyleName("gwt-textBox");
					HorizontalPanel pnlStraat = new HorizontalPanel();
					final TextBox txtStraat = new TextBox();
					txtStraat.setStyleName("gwt-textBox");
					txtStraat.setText(result.getStraatNaam());
					final TextBox txtHuisnummer = new TextBox();
					txtHuisnummer.setText(result.getHuisNummer());
					txtHuisnummer.setStyleName("gwt-textBox");
					pnlStraat.add(txtStraat);
					pnlStraat.add(txtHuisnummer);
					pnlStraat.addStyleName("marginfix");
					final TextBox txtPostcode = new TextBox();
					txtPostcode.setText(result.getPostCode());
					txtPostcode.setStyleName("gwt-textBox");
					final TextBox txtPlaats = new TextBox();
					txtPlaats.setText(result.getWoonPlaats());
					txtPlaats.setStyleName("gwt-textBox");
					Button btnSave = new Button("Opslaan");
					btnSave.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							FieldVerifier fv = FieldVerifier.get();
							boolean check = true;
							if (txtVoornaam.getText().matches("^[A-Z-a-z0-9]+$") &&
									FieldVerifier.get().checkField(txtVoornaam.getText(), 3, 30)) {
								txtVoornaam.setStyleName("gwt-textBoxG");
							} else {
								txtVoornaam.setStyleName("gwt-textBoxR");
								check = false;
							}

							//check achternaam
							if (txtAchternaam.getText().matches("^[A-Z-a-z0-9]+$") &&
									FieldVerifier.get().checkField(txtAchternaam.getText(), 4, 30)) {
								txtAchternaam.setStyleName("gwt-textBoxG");
							} else {
								txtAchternaam.setStyleName("gwt-textBoxR");
								check = false;
							}

							//check Straat
							if (txtStraat.getText().matches("^[A-Z-a-z0-9]+$") &&
									FieldVerifier.get().checkField(txtStraat.getText(), 4, 30)) {
								txtStraat.setStyleName("gwt-textBoxG");
							} else {
								txtStraat.setStyleName("gwt-textBoxR");
								check = false;
							}

							//check huisnummer
							if(txtHuisnummer.getText().matches("^[0-9]{1,6}[a-zA-Z]{0,3}$")){
								txtHuisnummer.setStyleName("gwt-textBoxG");
							}
							else{
								txtHuisnummer.setStyleName("gwt-textBoxR");
								check = false;
							}

							//check postcode
							if(txtPostcode.getText().matches("^[1-9][0-9][0-9][0-9][A-Z][A-Z]$")){
								txtPostcode.setStyleName("gwt-textBoxG");
							}
							else{
								txtPostcode.setStyleName("gwt-textBoxR");
								check = false;
							}

							//check woonplaats
							if(txtPlaats.getText().matches("^[A-Za-z]+$") && 
									FieldVerifier.get().checkField(txtPlaats.getText(), 4, 30)) {
								txtPlaats.setStyleName("gwt-textBoxG");
							}
							else{
								txtPlaats.setStyleName("gwt-textBoxR");
								check = false;
							}
							if (check)
							{
								//rs.getString("voornaam"), rs.getString("achternaam"), rs.getString("woonplaats"), rs.getString("postcode"), rs.getString("straat"), rs.getString("huisnummer"), rs.getInt("credits"), rs.getString("email")
								Gebruiker modifyUser = new Gebruiker(txtVoornaam.getText(), txtAchternaam.getText(), txtPlaats.getText(), txtPostcode.getText(), txtStraat.getText(), txtHuisnummer.getText(), gebruiker.getCredits(), gebruiker.getEmailAdres());
								AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
									@Override
									public void onFailure(Throwable caught) {
										NotificationManager.get().createNotification("Er is een fout opgetreden, probeer het later nog eens", 3000);
									}
									@Override
									public void onSuccess(Boolean result) {
										NotificationManager.get().createNotification("Uw gegevens zijn aangepast", 3000);
									}
								};
								Zwendelaar.get().updateGebruiker(modifyUser, callback);
							}
						}			
					});
					Button btnCancel = new Button("Annuleren");
					btnCancel.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							content.clear();
							Zwendelaar.get().showPage(new Blokkeren().createPage());;
						}	
					});

					grid.setText(0, 0, "Voornaam:");
					grid.setWidget(0, 1, txtVoornaam);
					grid.setText(1, 0, "Achternaam:");
					grid.setWidget(1, 1, txtAchternaam);
					grid.setText(2, 0, "Straat:");
					grid.setWidget(2, 1, pnlStraat);
					grid.setText(3, 0, "Postcode:");
					grid.setWidget(3, 1, txtPostcode);
					grid.setText(4, 0, "Plaats:");
					grid.setWidget(4, 1, txtPlaats);
					grid.setWidget(5, 0, btnSave);
					grid.setWidget(5, 1, btnCancel);
					content.add(grid);
				}
			}
		};
		Zwendelaar.get().zoekGebruiker(email, callback);
	}
	
	
	public Widget createPage() {
		main.add(content);
		return main;
	}
}