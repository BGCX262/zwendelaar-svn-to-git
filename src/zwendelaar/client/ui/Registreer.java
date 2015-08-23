package zwendelaar.client.ui;

import zwendelaar.client.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Registreer {
	private Label lblVoornaam, lblAchternaam, lblStraat, lblHuisNr, lblPostCode, lblWoonplaats,
	lblEmail, lblWachtwoord, lblWachtwoordCheck, lblInfo;
	private TextBox txtVoornaam, txtAchternaam, txtStraat, txtHuisNr, txtPostCode, txtWoonplaats, txtEmail;
	private PasswordTextBox txtWachtwoord, txtWachtwoordCheck;
	private Button btnSubmit, btnCancel;
	private VerticalPanel vp; 
	private HorizontalPanel buttonPanel;

	public Registreer() {	

		lblInfo = new Label("De velden met een * zijn verplicht!");

		lblVoornaam = new Label("*Voornaam:");
		txtVoornaam = new TextBox();
		txtVoornaam.setStyleName("gwt-textBox");

		lblAchternaam = new Label("*Achternaam:");
		txtAchternaam = new TextBox();
		txtAchternaam.setStyleName("gwt-textBox");

		lblStraat = new Label("*Straat:");
		txtStraat = new TextBox();
		txtStraat.setStyleName("gwt-textBox");

		lblHuisNr = new Label("*Huisnummer en evt. toevoeging:");
		txtHuisNr = new TextBox();
		txtHuisNr.setStyleName("gwt-textBox");

		lblPostCode = new Label("*Postcode:");
		txtPostCode = new TextBox();
		txtPostCode.setStyleName("gwt-textBox");

		lblWoonplaats = new Label("*Woonplaats:");
		txtWoonplaats = new TextBox();
		txtWoonplaats.setStyleName("gwt-textBox");

		lblEmail = new Label("*Email adres:");
		txtEmail= new TextBox();
		txtEmail.setStyleName("gwt-textBox");

		lblWachtwoord = new Label("*Wachtwoord:");
		txtWachtwoord = new PasswordTextBox();
		txtWachtwoord.setStyleName("gwt-textBox");

		lblWachtwoordCheck = new Label("*Wachtwoord check:");
		txtWachtwoordCheck = new PasswordTextBox();
		txtWachtwoordCheck.setStyleName("gwt-textBox");

		btnSubmit = new Button("Registreer");
		btnCancel = new Button("Reset");

		vp = new VerticalPanel();
		buttonPanel = new HorizontalPanel();

		KeyPressHandler loginKeyHandler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					handleFields();
				}			
			}
		};

		txtVoornaam.addKeyPressHandler(loginKeyHandler);
		txtAchternaam.addKeyPressHandler(loginKeyHandler);
		txtStraat.addKeyPressHandler(loginKeyHandler);
		txtHuisNr.addKeyPressHandler(loginKeyHandler);
		txtPostCode.addKeyPressHandler(loginKeyHandler);
		txtWoonplaats.addKeyPressHandler(loginKeyHandler);
		txtEmail.addKeyPressHandler(loginKeyHandler);
		txtWachtwoord.addKeyPressHandler(loginKeyHandler);
		txtWachtwoordCheck.addKeyPressHandler(loginKeyHandler);

		btnSubmit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				handleFields();
			}
		});

		btnCancel.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				resetFields();
			}
		});

	}

	public Panel createPage() {
		vp.add(lblInfo);
		vp.add(lblVoornaam); 	vp.add(txtVoornaam);
		vp.add(lblAchternaam);  vp.add(txtAchternaam);
		vp.add(lblStraat); 		vp.add(txtStraat);
		vp.add(lblHuisNr); 		vp.add(txtHuisNr);
		vp.add(lblPostCode);	vp.add(txtPostCode);
		vp.add(lblWoonplaats);	vp.add(txtWoonplaats);
		vp.add(lblEmail);		vp.add(txtEmail);		
		vp.add(lblWachtwoord); vp.add(txtWachtwoord);
		vp.add(lblWachtwoordCheck); vp.add(txtWachtwoordCheck);

		buttonPanel.add(btnSubmit); buttonPanel.add(btnCancel);
		vp.add(buttonPanel); 
		return vp;
	}

	private void handleFields() {
		int b = 0;

		//check voornaam
		if (txtVoornaam.getText().matches("^[A-Z-a-z]+$") &&
				FieldVerifier.get().checkField(txtVoornaam.getText(), 4, 30)) {
			txtVoornaam.setStyleName("gwt-textBoxG");
			b += 1;
		} else {
			txtVoornaam.setStyleName("gwt-textBoxR");
		}

		//check achternaam
		if (txtAchternaam.getText().matches("^[A-Z-a-z-\\s]+$") &&
				FieldVerifier.get().checkField(txtAchternaam.getText(), 4, 30)) {
			txtAchternaam.setStyleName("gwt-textBoxG");
			b += 1;
		} else {
			txtAchternaam.setStyleName("gwt-textBoxR");
		}

		//check Straat
		if (txtStraat.getText().matches("^[A-Z-a-z0-9-\\s]+$") &&
				FieldVerifier.get().checkField(txtStraat.getText(), 4, 30)) {
			txtStraat.setStyleName("gwt-textBoxG");
			b += 1;
		} else {
			txtStraat.setStyleName("gwt-textBoxR");
		}

		//check huisnummer
		if(txtHuisNr.getText().matches("^[0-9]{1,6}[a-zA-Z]{0,3}$")){
			txtHuisNr.setStyleName("gwt-textBoxG");
			b +=1;
		}
		else{
			txtHuisNr.setStyleName("gwt-textBoxR");
		}

		//check postcode
		if(txtPostCode.getText().matches("^[1-9][0-9][0-9][0-9][A-Z][A-Z]$")){
			txtPostCode.setStyleName("gwt-textBoxG");
			b +=1;
		}
		else{
			txtPostCode.setStyleName("gwt-textBoxR");
		}

		//check woonplaats
		if(txtWoonplaats.getText().matches("^[A-Za-z-\\s]+$") && 
				FieldVerifier.get().checkField(txtWoonplaats.getText(), 4, 30)) {
			txtWoonplaats.setStyleName("gwt-textBoxG");
			b +=1;
		}
		else{
			txtWoonplaats.setStyleName("gwt-textBoxR");
		}

		//check email
		if(txtEmail.getText().matches("^[A-Za-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,3})$") 
				&& FieldVerifier.get().checkField(txtEmail.getText(), 4, 30)){
			txtEmail.setStyleName("gwt-textBoxG");
			b +=1;
		}else {
			txtEmail.setStyleName("gwt-textBoxR");
		}

		//check wachtwoord
		if (FieldVerifier.get().checkField(txtWachtwoord.getText(), 4, 30) &&
				txtWachtwoord.getText().equals(txtWachtwoordCheck.getText())) {
				txtWachtwoord.removeStyleName("gwt-textBoxR");
				txtWachtwoordCheck.removeStyleName("gwt-textBoxR");
			b += 1;
		} else {
			txtWachtwoord.setStyleName("gwt-textBoxR");
			txtWachtwoordCheck.setStyleName("gwt-textBoxR");
		}

		if(b == 8) {
			FieldVerifier.get().checkRegister(txtVoornaam.getText(), txtAchternaam.getText(), 
					txtStraat.getText(), txtHuisNr.getText(), txtPostCode.getText(), 
					txtWoonplaats.getText(), txtEmail.getText(), txtWachtwoord.getText());
		}
	}

	private void resetFields() {
		txtVoornaam.setStyleName("gwt-textBox"); 
		txtAchternaam.setStyleName("gwt-textBox");
		txtStraat.setStyleName("gwt-textBox");
		txtHuisNr.setStyleName("gwt-textBox");
		txtPostCode.setStyleName("gwt-textBox");
		txtEmail.setStyleName("gwt-textBox");
		txtWoonplaats.setStyleName("gwt-textBox");
		txtWachtwoord.setStyleName("gwt-textBox"); 
		txtWachtwoordCheck.setStyleName("gwt-textBox");
	}

}