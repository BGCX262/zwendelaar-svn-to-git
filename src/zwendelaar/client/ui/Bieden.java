package zwendelaar.client.ui;

import java.util.Date;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Bieden {
	//Variablen
	int ID = -1;
	
	//Main Panel
	HorizontalPanel main = new HorizontalPanel();
	
	//Bieding Onderdelen
	VerticalPanel bieding = new VerticalPanel();
	Grid informatie = new Grid(6,2);
	TextBox txtBod = new TextBox();
	Button bied = new Button("Bied");
	
	
	VerticalPanel recenteBiedingen = new VerticalPanel();
	Label rbieding;
	String style = "odd";
	
	
	public Bieden(String VeilingID)
	{
		Zwendelaar.get().showElement(new UserInfo().createpage());
		try {
			ID = Integer.parseInt(VeilingID);
			
		} catch (NumberFormatException nfe){
			//TODO Handle Exception
			return;
		}
		if (ID > -1) {
			AsyncCallback<Veiling> callback = new AsyncCallback<Veiling>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(final Veiling result) {
					if (result != null)
					{
						boolean first = true;
						Bod laatst = null;
						//result.getAlleBiedingen().size();
						int i = 1;
						if (result.getAlleBiedingen().size() >= 1) {
							FlexTable ft = new FlexTable();
							ft.setText(0, 0, "Bod");
							ft.setText(0, 1, "Bieder");
							for(Bod b : result.getAlleBiedingen())
							{
								if (i > 10)
									break;
								if (first) laatst = b;
								ft.setText(i, 0, String.valueOf(b.credits));
								ft.setText(i, 1, b.getEmail());
								first = false;
								i++;
							}
							recenteBiedingen.add(ft);
							for(int j=0; j<ft.getRowCount(); j++){
								if(j%2 == 1){
									ft.getFlexCellFormatter().setStyleName(j,0,"tableCell-odd");
									ft.getFlexCellFormatter().setStyleName(j,1,"tableCell-odd");
								}
							}
							ft.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
							ft.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
						} else {
							recenteBiedingen.add(new Label("Er is nog niet geboden."));
						}
						if ((laatst != null && laatst.getEmail().equals(Zwendelaar.get().getGebruiker().getEmailAdres())) || result.getEmail().equals(Zwendelaar.get().getGebruiker().getEmailAdres()))
						{
							txtBod.setEnabled(false);
							bied.setEnabled(false);
						}
						
						Product p = result.getProduct();
						informatie.setText(0, 0, "Minimum aantal credits:");
						informatie.setText(0, 1, laatst != null ? String.valueOf(laatst.getCredits() + 1) : String.valueOf(result.getMinCredits()));
						informatie.setText(1, 0, "Naam:");
						informatie.setText(1, 1, p.getNaam());
						informatie.setText(2, 0, "Omschrijving:");
						informatie.setText(2, 1, p.getOmschrijving());
						informatie.setText(3, 0, "Merk:");
						informatie.setText(3, 1, p.getMerk());
						informatie.setText(4, 0, "Bod:");
						informatie.setWidget(4, 1, txtBod);
						informatie.setWidget(5, 0, bied);
						bieding.add(informatie);
						
						bied.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								if (txtBod.getText().equals("")) return;
								try {
									int credits = Integer.parseInt(txtBod.getText());
									Gebruiker g = Zwendelaar.get().getGebruiker();
									Bod b = new Bod(result.getID() , credits, new Date().toString(), g.getEmailAdres() );
									AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

										@Override
										public void onFailure(Throwable caught) {
											caught.printStackTrace();
										}

										@Override
										public void onSuccess(Integer result) {
											if (result == 0)
											{
												NotificationManager.get().createNotification("Uw saldo is niet hoog genoeg.", 3000);
											} else if (result == 1) {
												NotificationManager.get().createNotification("Bod gedaan", 3000);
												Zwendelaar.get().showPage(new Bieden(String.valueOf(ID)).createpage());
											}
										}
									};
									int hoogstebod = 0;
									if (result.biedingen.size() == 0)
										hoogstebod = result.getMinCredits();
									else {
										for(Bod bod : result.biedingen) {
											if (bod.credits > hoogstebod) hoogstebod = bod.credits + 1;
										}
									}
									if(b.getCredits() < hoogstebod)
										NotificationManager.get().createNotification("Uw bod is niet hoog genoeg", 3000);
									else
										Zwendelaar.get().addBod(b, g, ID,callback);
								} catch (NumberFormatException nfe){
								}
							}
							
						});
						
						bieding.setStyleName("bieding");
						recenteBiedingen.setStyleName("recenteBiedingen");
						
						main.add(bieding);
						main.add(recenteBiedingen);
					}
				}
				
			};			
			Zwendelaar.get().getVeiling(ID, callback);			
		}
	}


	public Widget createpage() {
		return main;
	}
}
