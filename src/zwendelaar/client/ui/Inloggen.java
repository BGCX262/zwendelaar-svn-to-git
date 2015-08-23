package zwendelaar.client.ui;

import zwendelaar.client.FieldVerifier;
import zwendelaar.client.Zwendelaar;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Inloggen {

	Label lblNaam = new Label("Gebruikersnaam:");
	TextBox txtNaam = new TextBox();
	Label lblWachtwoord = new Label("Wachtwoord:");
	PasswordTextBox pwdWachtwoord = new PasswordTextBox();
	HTML html = new HTML("</br>");
	Button btnOk = new Button("Inloggen");
	Button btnReg = new Button("Registreren");
	
	public Inloggen(){
		
		txtNaam.addKeyPressHandler(loginKeyHandler);
		pwdWachtwoord.addKeyPressHandler(loginKeyHandler);
		
		btnOk.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				handleFields();
			}
		});
		
		btnReg.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("registreer");
			}
		});
		
	}
	
	KeyPressHandler loginKeyHandler = new KeyPressHandler() {
		@Override
		public void onKeyPress(KeyPressEvent event) {
			if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				handleFields();
			}			
		}
	};
	

	private void handleFields() {
		boolean b = false;
		if(txtNaam.getText().matches("^[A-Za-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,3})$") 
				&& FieldVerifier.get().checkField(txtNaam.getText(), 4, 30)){
			if(FieldVerifier.get().checkField(pwdWachtwoord.getText(), 4, 30)){
				b = true;
			}
		}
		
		if(b){
			FieldVerifier.get().checkLogin(txtNaam.getText(), pwdWachtwoord.getText());
		}
		
	}
	
	public Panel createpage(){
		VerticalPanel vp = new VerticalPanel();
		if(!Zwendelaar.get().loggedIn){
			vp.add(lblNaam); 
			vp.add(txtNaam); txtNaam.setStyleName("gwt-textBox");
			vp.add(lblWachtwoord);
			vp.add(pwdWachtwoord); pwdWachtwoord.setStyleName("gwt-textBox");
			HorizontalPanel buttonPanel = new HorizontalPanel();
			vp.add(html);
			buttonPanel.add(btnOk);
			Label someRoom = new Label("..");
			someRoom.setStyleName("someRoom");
			buttonPanel.add(someRoom);
			buttonPanel.add(btnReg);
			vp.add(buttonPanel); 
			
		}
		return vp;
	}

}
