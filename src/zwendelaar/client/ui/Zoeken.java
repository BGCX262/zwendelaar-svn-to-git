package zwendelaar.client.ui;

import java.util.ArrayList;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Zoeken {

	final VerticalPanel menu = new VerticalPanel();
	final VerticalPanel vp = new VerticalPanel(); 
	final FlexTable ft = new FlexTable();
	//final TextBox tb = new TextBox();
	final ListBox categorie = new ListBox();
	final MultiWordSuggestOracle wordSuggestion = new MultiWordSuggestOracle(); 
	final SuggestBox box = new SuggestBox(wordSuggestion);
	final Button btnFilter = new Button("Filter");
	public Button increaseCredits, btnAdmin;
	ArrayList<String> lijstNamen = new ArrayList<String>();
	private String category = null, title = null;

	public Zoeken(){
		box.addKeyPressHandler(veilingSearchHandler);
		categorie.addKeyPressHandler(veilingSearchHandler);

		btnFilter.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				handleFields();
			}
		});

	}
	KeyPressHandler veilingSearchHandler = new KeyPressHandler() {
		@Override
		public void onKeyPress(KeyPressEvent event) {
			if(event.getSource()== btnFilter) {
				handleFields();
			}			
		}
	};
	public VerticalPanel createMenu() {
		menu.clear();


		AsyncCallback<ArrayList<Product>> callback = new AsyncCallback<ArrayList<Product>>(){
			@Override
			public void onFailure(Throwable caught) {
				//caught.printStackTrace();
				System.out.println(caught.getCause());
			}
			public void onSuccess(ArrayList<Product> result) {
				if (result.size() == 0)
				{
					menu.add(new Label("Geen categori\u00EBn gevonden"));
				}
				if(lijstNamen.size() == 0){
					categorie.addItem("-- Alle Categori\u00EBn --");
					lijstNamen = new ArrayList<String>();
					for (Product p : result)
					{
						String categorieNaam = p.getCategorie();
						if(!lijstNamen.contains(categorieNaam)){
							if(categorieNaam != ""){
								lijstNamen.add(categorieNaam);
								categorie.addItem(categorieNaam);
							}
						}	
					}
				}

				ft.setWidget(0,0, box);
				ft.setWidget(0,1, categorie);
				ft.setWidget(0,2,btnFilter);
				menu.add(ft);
			}
		};
		Zwendelaar.get().getProducts(callback);
		return menu;
	}
	public VerticalPanel createpage() {
		AsyncCallback<ArrayList<Veiling>> callback2 = new AsyncCallback<ArrayList<Veiling>>() {

			@Override
			public void onFailure(Throwable caught) {
				//caught.printStackTrace();
				System.out.println(caught.getCause());
			}

			@Override
			public void onSuccess(ArrayList<Veiling> result) {
				createMenu();
				vp.add(menu);
				if (result.size() == 0)
				{
					vp.add(new Label("Er zijn momenteel geen veilingen"));
				}



				FlexTable flex = new FlexTable();
				int x = 0;
				int y = 0;
				for (final Veiling v : result)
				{

					String naam = v.getProduct().getNaam();
					wordSuggestion.add(naam);
					String descr = v.getProduct().getOmschrijving();
					int prijs = v.getLastBieding();
					increaseCredits = new Button("Bied!");
					btnAdmin = new Button("Statistieken");
					if(!Zwendelaar.get().loggedIn()){
						increaseCredits.setEnabled(false);
					}else if(Zwendelaar.get().loggedIn()){
						increaseCredits.setEnabled(true);
					}

					increaseCredits.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							History.newItem("bieden?id="+v.ID);	
						}
					});
					btnAdmin.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							History.newItem("statistiek?id="+v.ID);	
						}
					});
					HTMLPanel p;
					if (Zwendelaar.get().getGebruiker() != null && Zwendelaar.get().getGebruiker().getAdmin()) {
						p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button><button id=\"stat\"></button></p></div>");
						p.addAndReplaceElement(btnAdmin, "stat");
					} else
						p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button></p></div>");

					p.addAndReplaceElement(increaseCredits, "myBtn");

					flex.setWidget(y, x, p);
					x++;
					if(x == 3){
						x = 0;
						y++;
					}
				}
				vp.add(flex);
			}

		};
		Zwendelaar.get().getVeilingen(callback2);
		return vp;
	};


	private void handleFields() {
		vp.clear();
		createMenu();
		vp.add(menu);
		title = "";
		if(!box.getText().equals(""))title = "%" + box.getText().substring(0,1).toUpperCase()+box.getText().substring(1).toLowerCase() + "%";
		category = categorie.getItemText(categorie.getSelectedIndex());
		
		// Niets ingevuld.
		if(title.equals("") && categorie.getSelectedIndex() == 0);
		// Als er wel een naam, maar geen categorie is gekozen.
		if(!title.equals("") && categorie.getSelectedIndex() == 0){
			AsyncCallback<ArrayList<Veiling>> callback = new AsyncCallback<ArrayList<Veiling>>(){
				@Override
				public void onFailure(Throwable caught){
					NotificationManager.get().createNotification("Er is een fout opgetreden met de database");
				}

				@Override
				public void onSuccess(ArrayList<Veiling> result) {
					if(result.size() == 0) NotificationManager.get().createNotification("Er zijn geen producten gevonden");
					FlexTable flex = new FlexTable();
					int x = 0;
					int y = 0;
					for (final Veiling v : result)
					{

						String naam = v.getProduct().getNaam();
						wordSuggestion.add(naam);
						String descr = v.getProduct().getOmschrijving();
						int prijs = v.getMinCredits();
						increaseCredits = new Button("Bied!");
						btnAdmin = new Button("Statistieken");
						if(!Zwendelaar.get().loggedIn()){
							increaseCredits.setEnabled(false);
						}else if(Zwendelaar.get().loggedIn()){
							increaseCredits.setEnabled(true);
						}

						increaseCredits.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								History.newItem("bieden?id="+v.ID);	
							}
						});
						btnAdmin.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								History.newItem("statistiek?id="+v.ID);	
							}
						});
						HTMLPanel p;
						if (Zwendelaar.get().getGebruiker() != null && Zwendelaar.get().getGebruiker().getAdmin()) {
							p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button><button id=\"stat\"></button></p></div>");
							p.addAndReplaceElement(btnAdmin, "stat");
						} else
							p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button></p></div>");

						p.addAndReplaceElement(increaseCredits, "myBtn");
						flex.setWidget(y, x, p);
						x++;
						if(x == 3){
							x = 0;
							y++;
						}
					}
					vp.add(flex);
				}
			};
			Zwendelaar.get().zoekProduct(title, callback);
		}
		
		// Als er zowel een naam als een categorie gekozen zijn.
		if(!title.equals("") && categorie.getSelectedIndex() > 0){
			category = categorie.getItemText(categorie.getSelectedIndex());
			AsyncCallback<ArrayList<Veiling>> callback = new AsyncCallback<ArrayList<Veiling>>(){
				@Override
				public void onFailure(Throwable caught){
					NotificationManager.get().createNotification("Er is een fout opgetreden met de database");
				}

				@Override
				public void onSuccess(ArrayList<Veiling> result) {
					if(result.size() == 0) NotificationManager.get().createNotification("Er zijn geen producten gevonden");
					FlexTable flex = new FlexTable();
					int x = 0;
					int y = 0;
					for (final Veiling v : result)
					{

						String naam = v.getProduct().getNaam();
						wordSuggestion.add(naam);
						String descr = v.getProduct().getOmschrijving();
						int prijs = v.getMinCredits();
						increaseCredits = new Button("Bied!");
						btnAdmin = new Button("Statistieken");
						if(!Zwendelaar.get().loggedIn()){
							increaseCredits.setEnabled(false);
						}else if(Zwendelaar.get().loggedIn()){
							increaseCredits.setEnabled(true);
						}

						increaseCredits.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								History.newItem("bieden?id="+v.ID);	
							}
						});
						btnAdmin.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								History.newItem("statistiek?id="+v.ID);	
							}
						});
						HTMLPanel p;
						if (Zwendelaar.get().getGebruiker() != null && Zwendelaar.get().getGebruiker().getAdmin()) {
							p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button><button id=\"stat\"></button></p></div>");
							p.addAndReplaceElement(btnAdmin, "stat");
						} else
							p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button></p></div>");

						p.addAndReplaceElement(increaseCredits, "myBtn");
						flex.setWidget(y, x, p);
						x++;
						if(x == 3){
							x = 0;
							y++;
						}
					}
					vp.add(flex);
				}
			};
			Zwendelaar.get().zoekProductCat(title, category, callback);
		}

		// Als er geen naam is opgegeven, maar wel een categorie
		if(title.equals("")){
			if(categorie.getSelectedIndex() == 0){
				createpage();
			}
			if(categorie.getSelectedIndex() !=  0){
				category = categorie.getItemText(categorie.getSelectedIndex());
				AsyncCallback<ArrayList<Veiling>> callback3 = new AsyncCallback<ArrayList<Veiling>>() {

					@Override
					public void onFailure(Throwable caught) {
						//caught.printStackTrace();
						System.out.println(caught.getCause());
					}

					@Override
					public void onSuccess(ArrayList<Veiling> result) {
						if (result.size() == 0)
						{
							vp.add(new Label("Er zijn geen resultaten in uw filter."));
						}



						FlexTable flex = new FlexTable();
						int x = 0;
						int y = 0;
						for (final Veiling v : result)
						{

							String naam = v.getProduct().getNaam();
							wordSuggestion.add(naam);
							String descr = v.getProduct().getOmschrijving();
							int prijs = v.getMinCredits();
							increaseCredits = new Button("Bied!");
							btnAdmin = new Button("Statistieken");
							if(!Zwendelaar.get().loggedIn()){
								increaseCredits.setEnabled(false);
							}else if(Zwendelaar.get().loggedIn()){
								increaseCredits.setEnabled(true);
							}

							increaseCredits.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									History.newItem("bieden?id="+v.ID);	
								}
							});
							btnAdmin.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									History.newItem("statistiek?id="+v.ID);	
								}
							});
							HTMLPanel p;
							if (Zwendelaar.get().getGebruiker() != null && Zwendelaar.get().getGebruiker().getAdmin()) {
								p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button><button id=\"stat\"></button></p></div>");
								p.addAndReplaceElement(btnAdmin, "stat");
							} else
								p = new HTMLPanel("<div class='aanbieding'><p class='aanbiedingNaam'><b>"+ naam + "</b></p><p>"  + descr + "</p><img src='/img/prod/"+ naam + ".jpg' alt='"+ descr + "' style='width: 100px; height: 100px; margin-left: 70px;'/></br><p>Huidig bod: <b>" + prijs + "</b> credits</p></br><p><button id=\"myBtn\"></button></p></div>");

							p.addAndReplaceElement(increaseCredits, "myBtn");
							flex.setWidget(y, x, p);
							x++;
							if(x == 3){
								x = 0;
								y++;
							}
						}
						vp.add(flex);
					}

				};
				Zwendelaar.get().getVeilingenCategorie(category, callback3);
			}
		}

		//		Zwendelaar.get().clearMain();

	}




}
