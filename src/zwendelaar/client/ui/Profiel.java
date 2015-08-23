package zwendelaar.client.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import zwendelaar.client.FieldVerifier;
import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Profiel {
	Gebruiker gebruiker;

	HorizontalPanel main = new HorizontalPanel();
	HorizontalPanel hpVeiling = new HorizontalPanel();
	HorizontalPanel hpBiedingen = new HorizontalPanel();
	HorizontalPanel hpHome;

	VerticalPanel submenu = new VerticalPanel();
	VerticalPanel content = new VerticalPanel();
	VerticalPanel vpVeilingInfo = new VerticalPanel();
	VerticalPanel vpVeiling = new VerticalPanel();
	VerticalPanel vpBiedingen = new VerticalPanel();
	VerticalPanel vpHome;
	VerticalPanel vpCredits = new VerticalPanel();

	Button showVeiling, showBiedingen;

	private Label lblPi, lblVo, lblBi, lblCr;
	private Label lblNaam, lblOmschrijving, lblMinCredits, lblEindtijd;

	private static double value = 1.25;
	
	public Profiel()
	{
		submenu.setStyleName("menu");
		content.setStyleName("content");
		gebruiker = Zwendelaar.get().getGebruiker();
		if (gebruiker == null){
			History.newItem("home");
		}

		lblPi = new Label("Persoonlijk informatie");
		lblPi.setStyleName("customLink");
		lblPi.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				content.clear();
				loadPi();
			}
		});
		lblVo = new Label("Overzicht veilingen");
		lblVo.setStyleName("customLink");
		lblVo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				content.clear();
				loadVeilingen();
			}
		});
		
		lblBi = new Label("Overzicht biedingen");
		lblBi.setStyleName("customLink");
		lblBi.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				content.clear();
				loadBiedingen();
			}
		});
		
		lblCr = new Label("Credits kopen");
		lblCr.setStyleName("customLink");
		lblCr.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Credits();
			}
		});

	}

	public HorizontalPanel createpage()
	{

		vpHome = new VerticalPanel();
		hpHome = new HorizontalPanel();

		submenu.add(lblPi);
		submenu.add(lblVo);
		submenu.add(lblBi);
		submenu.add(lblCr);
		vpHome.add(hpHome);

		loadPi();
		main.add(submenu);
		main.add(content);

		return main;
	}

	private void loadPi()
	{
		content.clear();
		Grid grid = new Grid(6, 2);
		final TextBox txtVoornaam = new TextBox();
		txtVoornaam.setText(gebruiker.getVoornaam());
		final TextBox txtAchternaam = new TextBox();
		txtAchternaam.setText(gebruiker.getAchternaam());
		HorizontalPanel pnlStraat = new HorizontalPanel();
		final TextBox txtStraat = new TextBox();
		txtStraat.setStyleName("straat");
		txtStraat.setText(gebruiker.getStraatNaam());
		final TextBox txtHuisnummer = new TextBox();
		txtHuisnummer.setText(gebruiker.getHuisNummer());
		txtHuisnummer.setStyleName("nummer");
		pnlStraat.add(txtStraat);
		pnlStraat.add(txtHuisnummer);
		pnlStraat.addStyleName("marginfix");
		final TextBox txtPostcode = new TextBox();
		txtPostcode.setText(gebruiker.getPostCode());
		final TextBox txtPlaats = new TextBox();
		txtPlaats.setText(gebruiker.getWoonPlaats());
		Button btnSave = new Button("Opslaan");
		btnSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				FieldVerifier fv = FieldVerifier.get();
				boolean check = true;
				if (txtVoornaam.getText().matches("^[A-Z-a-z]+$") &&
						FieldVerifier.get().checkField(txtVoornaam.getText(), 3, 30)) {
					txtVoornaam.setStyleName("gwt-textBoxG");
				} else {
					txtVoornaam.setStyleName("gwt-textBoxR");
					check = false;
				}

				//check achternaam
				if (txtAchternaam.getText().matches("^[A-Z-a-z-\\s]+$") &&
						FieldVerifier.get().checkField(txtAchternaam.getText(), 4, 30)) {
					txtAchternaam.setStyleName("gwt-textBoxG");
				} else {
					txtAchternaam.setStyleName("gwt-textBoxR");
					check = false;
				}

				//check Straat
				if (txtStraat.getText().matches("^[A-Z-a-z-\\s]+$") &&
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
					Zwendelaar.get().setGebruiker(new Gebruiker(txtVoornaam.getText(), txtAchternaam.getText(), txtPlaats.getText(), txtPostcode.getText(), txtStraat.getText(), txtHuisnummer.getText(), gebruiker.getCredits(), gebruiker.getEmailAdres()));
					gebruiker = Zwendelaar.get().getGebruiker();
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
					Zwendelaar.get().updateGebruiker(gebruiker, callback);
				}
			}			
		});
		Button btnCancel = new Button("Annuleren");
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				content.clear();
				loadPi();
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

	private void loadVeilingen(){
		content.clear();
		vpHome.clear();
		hpHome.clear();
		AsyncCallback<ArrayList<Veiling>> callback = new AsyncCallback<ArrayList<Veiling>>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er zijn geen veilingen gevonden");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<Veiling> result) {
				if(result.size() == 0){
					NotificationManager.get().createNotification("Er zijn geen veilingen gevonden.");
				}else{
					for(final Veiling v : result){
						vpVeilingInfo = new VerticalPanel();
						vpVeiling = new VerticalPanel();
						hpVeiling = new HorizontalPanel();

						lblNaam = new Label(v.getProduct().getNaam());

						lblOmschrijving = new Label(v.getProduct().getOmschrijving());

						lblMinCredits = new Label(""+v.getMinCredits());

						lblEindtijd = new Label(v.getSluitDatum());
						showVeiling = new Button("Info");

						showVeiling.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								History.newItem("bieden?id="+v.getID());	
							}
						});

						String naam = lblNaam.getText();
						String descr = lblOmschrijving.getText();
						String credits = lblMinCredits.getText();
						String eDatum = lblEindtijd.getText();

						if(v.getAlleBiedingen().size() == 0){
							HTMLPanel p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px;margin-left: 70px;'/></br><p>Minimum aantal credits: <b>"  + credits + "</b> credits</p><p>Einddatum: <b>" + eDatum  + "</b></p><p><button id=\"myBtn\"></button></p></div>");
							p.addAndReplaceElement(showVeiling, "myBtn");
							hpVeiling.add(p);
						}else{
							HTMLPanel p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px;margin-left: 70px;'/></br><p>Huidig bod: <b>"  + v.getLastBieding() + "</b> credits</p><p>Einddatum: <b>" + eDatum  + "</b></p><p><button id=\"myBtn\"></button></p></div>");
							p.addAndReplaceElement(showVeiling, "myBtn");
							hpVeiling.add(p);
						}
						vpVeilingInfo.add(hpVeiling);

						if(hpHome.getWidgetCount()+1 > 2){
							hpHome = new HorizontalPanel();
							hpHome.add(vpVeilingInfo);
							vpHome.add(hpHome);
						}else{
							hpHome.add(vpVeilingInfo);
						}
					}
				}
			}
		};
		new Zwendelaar().getAllVeilingenByUser(gebruiker.getEmailAdres(), callback);

		vpHome.add(hpHome);
		content.add(vpHome);
	}
	
	private void loadBiedingen(){
		content.clear();
		hpBiedingen.clear();
		vpBiedingen.clear();
		AsyncCallback<ArrayList<Veiling>> callback = new AsyncCallback<ArrayList<Veiling>>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er zijn geen veilingen gevonden");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<Veiling> result) {
				if(result.size() == 0){
					NotificationManager.get().createNotification("Er zijn geen veilingen gevonden.");
				}else{
					Grid grid = new Grid(1, 4);
					grid.setText(0, 0, "Naam:");
					grid.setText(0, 1, "Huidige bod:");
					grid.setText(0, 2, "Eigen bod:");
					grid.setText(0, 3, "Link:");
					
					for(final Veiling v : result){
						showBiedingen = new Button("Info");
						showBiedingen.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								History.newItem("bieden?id="+v.getID());	
							}
						});
						
						grid.resizeRows(grid.getRowCount()+1);
						grid.setText(grid.getRowCount()-1, 0, v.getProduct().getNaam());
						grid.setText(grid.getRowCount()-1, 1, ""+v.getLastBieding());
						grid.setText(grid.getRowCount()-1, 2, ""+v.getHighBiedingUser(gebruiker.getEmailAdres()).getCredits());
						grid.setWidget(grid.getRowCount()-1, 3, showBiedingen);
					}
					hpBiedingen.add(grid);
				}
			}
		};
		new Zwendelaar().getAllVeilingenByBod(gebruiker.getEmailAdres(), callback);

		vpBiedingen.add(hpBiedingen);
		content.add(vpBiedingen);
	}
	
	private void Credits() {
		content.clear();
		final NumberFormat nf = NumberFormat.getFormat("0.00");
		FlexTable ft = new FlexTable();
		
		Button btn10 = new Button("Kopen");
		btn10.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Er wordt een bedrag van \u20AC " + nf.format(value * 10) + " van uw rekening afgeschreven");
				gebruiker.addCredits(10);
				Zwendelaar.get().showElement(new UserInfo().createpage());
			}
		});
		Button btn50 = new Button("Kopen");
		btn50.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Er wordt een bedrag van \u20AC " + nf.format(value * 50) + " van uw rekening afgeschreven");
				gebruiker.addCredits(50);
				Zwendelaar.get().showElement(new UserInfo().createpage());
			}
		});
		Button btn100 = new Button("Kopen");
		btn100.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Er wordt een bedrag van \u20AC " + nf.format(value * 100) + " van uw rekening afgeschreven");
				gebruiker.addCredits(100);
				Zwendelaar.get().showElement(new UserInfo().createpage());
			}
		});
		Button btn250 = new Button("Kopen");
		btn250.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Er wordt een bedrag van \u20AC " + nf.format(value * 250) + " van uw rekening afgeschreven");
				gebruiker.addCredits(250);
				Zwendelaar.get().showElement(new UserInfo().createpage());
			}
		});
		ft.setText(0, 0, "Aantal credits");
		ft.setText(0, 1, "In euro's");
		ft.setText(1, 0, "10");
		ft.setText(1, 1, nf.format(value * 10));
		ft.setWidget(1, 2, btn10);
		ft.setText(2, 0, "50");
		ft.setText(2, 1, nf.format(value * 50));
		ft.setWidget(2, 2, btn50);
		ft.setText(3, 0, "100");
		ft.setText(3, 1, nf.format(value * 100));
		ft.setWidget(3, 2, btn100);
		ft.setText(4, 0, "250");
		ft.setText(4, 1, nf.format(value * 250));
		ft.setWidget(4, 2, btn250);
		
		for(int i=0; i<ft.getRowCount(); i++){
			if(i%2 == 1){
				ft.getFlexCellFormatter().setStyleName(i,0,"tableCell-odd");
				ft.getFlexCellFormatter().setStyleName(i,1,"tableCell-odd");
				ft.getFlexCellFormatter().setStyleName(i,2,"tableCell-odd");
				ft.getFlexCellFormatter().setStyleName(i,3,"tableCell-odd");
			}
		}
		ft.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
		ft.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
		ft.getFlexCellFormatter().setStyleName(0,2,"tableHeader");
		ft.getFlexCellFormatter().setStyleName(0,3,"tableHeader");
		vpCredits.add(ft);
		content.add(vpCredits);
	}
}
