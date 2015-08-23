package zwendelaar.client.ui;

import zwendelaar.client.Zwendelaar;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserInfo {
	
	Label lblNaam = new Label();
	Label lblCredits = new Label();
	Button btUitlog = new Button("Uitloggen");
	HTML adminTools;
	
	
	public UserInfo(){
		
		
		lblNaam.setStyleName("customLink");
		lblNaam.setText(Zwendelaar.get().getGebruiker().getNaam());
		lblNaam.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("profiel");
				
			}
		});
		
		lblCredits.setText("Aantal credits: "+Zwendelaar.get().getGebruiker().getCredits());
		
		// Admin links
		adminTools = new HTML("</br><p style='color:#8fb209; font-weight:bold; margin-left: -3px;'>Admin Tools:</p>" +
				"<a href='#blokkeren' style='color: #5b5b5b; display: inline;'>   - Beheren</a></br>" +
				"<a href='#statistieken' style='color: #5b5b5b; display: inline;'>   - Statistieken </a></br> " +
				"</br>");
		
		
		btUitlog.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				System.out.println("uitloggen");
				Zwendelaar.get().logout();
			}
		});
	}
	
	public Panel createpage(){
		VerticalPanel vp = new VerticalPanel();
		vp.add(lblNaam);
		vp.add(lblCredits);
		
		// Toevoegen admin links
		if(Zwendelaar.get().isAdmin()){ 
			vp.add(adminTools);
		}
		vp.add(btUitlog);
		return vp;
	}

}
