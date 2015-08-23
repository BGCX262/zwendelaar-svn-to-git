package zwendelaar.client.ui;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Veilingen {

	public VerticalPanel vpHome, vpVeiling, vpVeilingInfo;
	public HorizontalPanel hpHome, hpVeiling;

	public Label lblNaam, lblOmschrijving, lblMinCredits, lblEindtijd;

	public Date today = new Date();

	public Button increaseCredits;
	public Veilingen(){

	}

	public Panel createPage(){
		vpHome = new VerticalPanel();
		hpHome = new HorizontalPanel();
		getVeilingen();

		vpHome.add(hpHome);
		return vpHome;
	}

	public void getVeilingen(){	
		AsyncCallback<ArrayList<Veiling>> callback = new AsyncCallback<ArrayList<Veiling>>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er is een server fout opgetreden. ");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<Veiling> result) {
				for(final Veiling v : result){
					vpVeilingInfo = new VerticalPanel();
					vpVeiling = new VerticalPanel();
					hpVeiling = new HorizontalPanel();

					lblNaam = new Label(v.getProduct().getNaam());

					lblOmschrijving = new Label(v.getProduct().getOmschrijving());

					lblMinCredits = new Label(""+v.getMinCredits());

					lblEindtijd = new Label(v.getSluitDatum());

					increaseCredits = new Button("Bied!");

					if(!Zwendelaar.get().loggedIn()){
						increaseCredits.setEnabled(false);
						increaseCredits.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								NotificationManager.get().createNotification("Log in om te bieden!");
							}
						});
					}else if(Zwendelaar.get().loggedIn()){
						increaseCredits.setEnabled(true);
					}

					increaseCredits.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							History.newItem("bieden?id="+v.ID);	
						}
					});


					String naam = lblNaam.getText();
					String descr = lblOmschrijving.getText();
					String credits = lblMinCredits.getText();
					String eDatum = lblEindtijd.getText();

					if(v.getAlleBiedingen().size() == 0){
						HTMLPanel p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px;margin-left: 70px;'/></br><p>Minimum aantal credits: <b>"  + credits + "</b> credits</p><p>Einddatum: <b>" + eDatum  + "</b></p><p><button id=\"myBtn\"></button></p></div>");
						p.addAndReplaceElement(increaseCredits, "myBtn");
						hpVeiling.add(p);
					}else{
						HTMLPanel p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px;margin-left: 70px;'/></br><p>Huidig bod: <b>"  + v.getLastBieding() + "</b> credits</p><p>Einddatum: <b>" + eDatum  + "</b></p><p><button id=\"myBtn\"></button></p></div>");
						p.addAndReplaceElement(increaseCredits, "myBtn");
						hpVeiling.add(p);
					}

					vpVeilingInfo.add(hpVeiling);

					if(hpHome.getWidgetCount()+1 > 3){
						hpHome = new HorizontalPanel();
						hpHome.add(vpVeilingInfo);
						vpHome.add(hpHome);
					}else{
						hpHome.add(vpVeilingInfo);
					}
				}
			}
		};
		new Zwendelaar().getVeilingen(callback);
	}

	public String formatDate(Date d){
		DateTimeFormat dtf = DateTimeFormat.getFormat("dd-MM-yyyy");
		return dtf.format(d);
	}

}
