package zwendelaar.client.ui;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class veilingToevoegen {

	private VerticalPanel vpHome,vpProduct, vpVeiling,vpBProduct, vpBVeiling;
	private HorizontalPanel hpHome, hpVeiling, hpVeilingCredits, hpVeilingStDt, hpVeilingEDT, hpVeilingButtons, hpBVeilingButtons;
	private HorizontalPanel hpProduct, hpProductNaam, hpProductOmschr, hpProductMerk, hpProductCategorie;
	private HorizontalPanel hpBProduct, hpBProductNaam, hpBProductOmschr, hpBProductMerk, hpBProductCategorie;
	private HorizontalPanel hpBVeiling, hpBVeilingCredits, hpBVeilingStDt, hpBVeilingEDT;
	private Button btpToevoegen, btpSelecteren, btAnnuleren, btOpslaan, btContinue;

	//Product velden en labels
	private Label lblpNaam, lblpOmschrijving, lblpMerk, lblpCategorie, lblVerplicht;
	private Label lblpBNaam, lblpBOmschrijving, lblpBMerk, lblpBCategorie;
	private TextBox pNaam, pMerk, pCategorie, pBNaam, pBMerk, pBCategorie;
	private TextArea pOmschrijving, pBOmschrijving;

	//Veiling velden en labels
	private Label lblvMinCredits, lblvSluitdatum, lblvBegindatum;
	private Label lblvBMinCredits, lblvBSluitdatum, lblvBBegindatum;
	private TextBox vMinCredits, vBMinCredits, vBEindDatum, vBStartDatum;

	//Datum listbox
	private ListBox lbDagS, lbMaandS, lbJaarS, lbDagE, lbMaandE, lbJaarE, products, categories;

	//Radio buttons
	private RadioButton productJa, productNee;
	private Label nieuwProduct, bestaandProduct;

	private TabPanel tabpanel;
	TabBar tabbar;

	private int productID = 0;
	private Product product;
	private ArrayList<Product> allProducts;
	public Date today = new Date();

	public veilingToevoegen(){
		hpProduct = new HorizontalPanel();
		hpProductMerk = new HorizontalPanel();
		hpProductNaam = new HorizontalPanel();
		hpProductOmschr = new HorizontalPanel();
		hpProductCategorie = new HorizontalPanel();
		vpProduct = new VerticalPanel();

		vpVeiling = new VerticalPanel();
		hpVeiling = new HorizontalPanel();
		hpVeilingCredits = new HorizontalPanel();
		hpVeilingEDT = new HorizontalPanel();
		hpVeilingStDt = new HorizontalPanel();
		hpVeilingButtons = new HorizontalPanel();
		hpBVeilingButtons = new HorizontalPanel();

		hpBProduct = new HorizontalPanel();
		hpBProductMerk = new HorizontalPanel();
		hpBProductNaam = new HorizontalPanel();
		hpBProductOmschr = new HorizontalPanel();
		hpBProductCategorie = new HorizontalPanel();

		hpBVeiling = new HorizontalPanel();
		hpBVeilingCredits = new HorizontalPanel();
		hpBVeilingEDT = new HorizontalPanel();
		hpBVeilingStDt = new HorizontalPanel();

		tabpanel = new TabPanel();
		tabbar = tabpanel.getTabBar();

		lblVerplicht = new Label("* is verplicht");

		lblpNaam = new Label("*Product naam:");
		lblpOmschrijving = new Label("*Product omschrijving:");
		lblpMerk = new Label("*Product merk:");
		lblpCategorie = new Label("*Product categorie:");

		lblvMinCredits = new Label("Minimum aantal credits:");
		lblvSluitdatum = new Label("Sluitings datum:");
		lblvBegindatum = new Label("Begin datum:");
		
		lblpBNaam = new Label("Product naam:");
		lblpBOmschrijving = new Label("Product omschrijving:");
		lblpBMerk = new Label("Product merk:");
		lblpBCategorie = new Label("Product categorie:");

		lblvBMinCredits = new Label("Minimum aantal credits:");
		lblvBSluitdatum = new Label("Sluitings datum:");
		lblvBBegindatum = new Label("Begin datum:");

		lbDagS = new ListBox();
		lbDagS.setWidth("75px");
		for(int d = 1; d <= 31; d++) {
			lbDagS.addItem(d + "");
		}
		lbMaandS = new ListBox();
		lbMaandS.setWidth("75px");
		for(int m = 1; m <= 12; m++) {
			lbMaandS.addItem(m + "");
		}
		lbJaarS = new ListBox();
		lbJaarS.setWidth("75px");
		for(int j = 2011; j <= Integer.parseInt(formatDate(today)); j++) {
			lbJaarS.addItem(j + "");
		}

		lbDagE = new ListBox();
		lbDagE.setWidth("75px");
		for(int d = 1; d <= 31; d++) {
			lbDagE.addItem(d + "");
		}
		lbMaandE = new ListBox();
		lbMaandE.setWidth("75px");
		for(int m = 1; m <= 12; m++) {
			lbMaandE.addItem(m + "");
		}
		lbJaarE = new ListBox();
		lbJaarE.setWidth("75px");
		for(int j = Integer.parseInt(formatDate(today)); j <= 2018; j++) {
			lbJaarE.addItem(j + "");
		}


		products = new ListBox();
		getProducts();		
		categories = new ListBox();
		getCategories();


		btAnnuleren = new Button("Annuleren");
		btContinue = new Button("Volgende");
		btOpslaan = new Button("Opslaan");
		btpSelecteren = new Button("Selecteer");
		btpToevoegen = new Button("Voeg toe");

		btOpslaan.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(productJa.getValue()){
					addVeiling();
				}else if(productNee.getValue()){
					addVeiling(productID);
				}
			}
		});

		btContinue.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(vMinCredits.getText().equals("")){
					NotificationManager.get().createNotification("Niet alle velden\n zijn ingevuld");
				}else{
					tabpanel.remove(2);
					tabpanel.add(bevestig(), "Bevestiging");
					tabpanel.selectTab(2);
				}
			}
		});

		btAnnuleren.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem("home");
			}
		});

		btpToevoegen.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(pMerk.getText().equals("") ||
						pNaam.getText().equals("")){
					NotificationManager.get().createNotification("Niet alle velden\n zijn ingevuld");
				}else{
					addProduct();
				}

			}
		});

		btpSelecteren.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				tabbar.setTabEnabled(1, true);
				tabpanel.selectTab(1);
			}
		});

		productJa = new RadioButton("product");
		productNee = new RadioButton("product");

		pNaam = new TextBox();
		pOmschrijving = new TextArea();
		pMerk = new TextBox();
		pCategorie = new TextBox();

		vMinCredits = new TextBox();

		nieuwProduct = new Label("Een nieuw product aanmaken");
		bestaandProduct = new Label("Een bestaand product selecteren");
	}

	public Panel createpage(){
		vpHome = new VerticalPanel();

		tabpanel.add(productOpties(), "Product");
		tabpanel.add(veilingOpties(), "Veiling");
		tabpanel.add(new VerticalPanel(), "Bevestiging");
		tabpanel.selectTab(0);
		tabbar.setTabEnabled(2, false);
		tabbar.setTabEnabled(1, false);

		vpHome.add(tabpanel);

		return vpHome;
	}

	public VerticalPanel bevestig() {
		VerticalPanel vp = new VerticalPanel();
		vpBProduct = new VerticalPanel();
		vpBVeiling = new VerticalPanel();

		vBMinCredits = new TextBox();
		vBEindDatum = new TextBox();
		vBStartDatum = new TextBox();

		pBNaam = new TextBox();
		pBNaam.setEnabled(false);
		pBOmschrijving = new TextArea();
		pBOmschrijving.setEnabled(false);
		pBMerk = new TextBox();
		pBMerk.setEnabled(false);
		pBCategorie = new TextBox();
		pBCategorie.setEnabled(false);

		vBMinCredits.setText(vMinCredits.getText());
		vBMinCredits.setEnabled(false);

		vBStartDatum.setText(lbDagS.getItemText(lbDagS.getSelectedIndex())+"-"+lbMaandS.getItemText(lbMaandS.getSelectedIndex())+"-"+lbJaarS.getItemText(lbJaarS.getSelectedIndex()));
		vBStartDatum.setEnabled(false);

		vBEindDatum.setText(lbDagE.getItemText(lbDagE.getSelectedIndex())+"-"+lbMaandE.getItemText(lbMaandE.getSelectedIndex())+"-"+lbJaarE.getItemText(lbJaarE.getSelectedIndex()));
		vBEindDatum.setEnabled(false);

		hpBVeilingCredits.add(lblvBMinCredits);
		hpBVeilingCredits.add(vBMinCredits);

		hpBVeilingStDt.add(lblvBBegindatum);
		hpBVeilingStDt.add(vBStartDatum);

		hpBVeilingEDT.add(lblvBSluitdatum);
		hpBVeilingEDT.add(vBEindDatum);

		if(productJa.getValue()){
			pBNaam.setText(pNaam.getText());
			pBOmschrijving.setText(pOmschrijving.getText());
			pBMerk.setText(pMerk.getText());
			pBCategorie.setText(categories.getItemText(categories.getSelectedIndex()));

			hpBProductNaam.add(lblpBNaam);
			hpBProductNaam.add(pBNaam);

			hpBProductOmschr.add(lblpBOmschrijving);
			hpBProductOmschr.add(pBOmschrijving);

			hpBProductMerk.add(lblpBMerk);
			hpBProductMerk.add(pBMerk);

			hpBProductCategorie.add(lblpBCategorie);
			hpBProductCategorie.add(pBCategorie);

			vpBProduct.add(hpBProductNaam);
			vpBProduct.add(hpBProductMerk);
			vpBProduct.add(hpBProductOmschr);
			vpBProduct.add(hpBProductCategorie);
		}else if(productNee.getValue()){
			productID = allProducts.get(products.getSelectedIndex()).getID();
			pBNaam.setText(allProducts.get(products.getSelectedIndex()).getNaam());
			pBOmschrijving.setText(allProducts.get(products.getSelectedIndex()).getOmschrijving());
			pBMerk.setText(allProducts.get(products.getSelectedIndex()).getMerk());
			pBCategorie.setText(allProducts.get(products.getSelectedIndex()).getCategorie());
			
			hpBProductNaam.add(lblpBNaam);
			hpBProductNaam.add(pBNaam);

			hpBProductOmschr.add(lblpBOmschrijving);
			hpBProductOmschr.add(pBOmschrijving);

			hpBProductMerk.add(lblpBMerk);
			hpBProductMerk.add(pBMerk);

			hpBProductCategorie.add(lblpBCategorie);
			hpBProductCategorie.add(pBCategorie);
			
			vpBProduct.add(hpBProductNaam);
			vpBProduct.add(hpBProductMerk);
			vpBProduct.add(hpBProductOmschr);
			vpBProduct.add(hpBProductCategorie);
		}
		hpBVeilingButtons.add(btAnnuleren);
		hpBVeilingButtons.add(btOpslaan);

		vpBVeiling.add(hpBVeilingCredits);
		vpBVeiling.add(hpBVeilingStDt);
		vpBVeiling.add(hpBVeilingEDT);
		vpBVeiling.add(hpBVeilingButtons);

		vp.add(vpBProduct);
		vp.add(vpBVeiling);
		return vp;
	}

	private Widget veilingOpties() {
		VerticalPanel vp = new VerticalPanel();

		vpVeiling.clear();

		hpVeilingCredits.add(lblvMinCredits);
		hpVeilingCredits.add(vMinCredits);

		hpVeilingStDt.add(lblvBegindatum);
		hpVeilingStDt.add(lbDagS);
		hpVeilingStDt.add(lbMaandS);
		hpVeilingStDt.add(lbJaarS);

		hpVeilingEDT.add(lblvSluitdatum);
		hpVeilingEDT.add(lbDagE);
		hpVeilingEDT.add(lbMaandE);
		hpVeilingEDT.add(lbJaarE);

		hpVeilingButtons.add(btAnnuleren);
		hpVeilingButtons.add(btContinue);

		vpVeiling.add(hpVeilingCredits);
		vpVeiling.add(hpVeilingStDt);
		vpVeiling.add(hpVeilingEDT);
		vpVeiling.add(hpVeilingButtons);

		vp.add(vpVeiling);
		return vp;
	}

	private Widget productOpties() {
		HorizontalPanel hp = new HorizontalPanel();
		VerticalPanel vp = new VerticalPanel();
		productJa.setFocus(false);
		productNee.setFocus(false);

		hp.add(nieuwProduct);
		hp.add(productJa);
		hp.add(bestaandProduct);
		hp.add(productNee);
		vp.add(hp);

		productJa.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				vpProduct.clear();
				RadioButton rb = (RadioButton) event.getSource();
				VerticalPanel hp = (VerticalPanel) rb.getParent().getParent();

				vpProduct.add(lblVerplicht);
				hpProductNaam.add(lblpNaam);
				hpProductNaam.add(pNaam);

				hpProductOmschr.add(lblpOmschrijving);
				hpProductOmschr.add(pOmschrijving);

				hpProductMerk.add(lblpMerk);
				hpProductMerk.add(pMerk);

				hpProductCategorie.add(lblpCategorie);
				hpProductCategorie.add(categories);

				vpProduct.add(hpProductNaam);
				vpProduct.add(hpProductMerk);
				vpProduct.add(hpProductOmschr);
				vpProduct.add(hpProductCategorie);
				vpProduct.add(btpToevoegen);
				hp.add(vpProduct);
			}
		});

		productNee.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				vpProduct.clear();
				RadioButton rb = (RadioButton) event.getSource();
				VerticalPanel hp = (VerticalPanel) rb.getParent().getParent();

				vpProduct.add(products);
				vpProduct.add(btpSelecteren);
				hp.add(vpProduct);
			}
		});
		return vp;
	}

	private Widget getCategories(){ 
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er is een server fout opgetreden.");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<String> result) {
				for(final String s : result){
					categories.addItem(s);
				}
			}
		};
		new Zwendelaar().getAllCategories(callback);
		return categories;
	}

	private Widget getProducts(){
		allProducts = new ArrayList<Product>();
		AsyncCallback<ArrayList<Product>> callback = new AsyncCallback<ArrayList<Product>>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er is een server fout opgetreden.");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<Product> result) {
				for(final Product p : result){
					allProducts.add(p);
					products.addItem(p.getNaam()+", "+p.getOmschrijving());
				}
			}
		};
		new Zwendelaar().getProducts(callback);
		return products;
	}

	private void addProduct(){
		AsyncCallback<Product> callback = new AsyncCallback<Product>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er is een server fout opgetreden.");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(Product result) {

				product = result;
				//result == null is fout
				//Product resultaat is met ID
				tabbar.setTabEnabled(1, true);
				tabpanel.selectTab(1);
			}
		};
		new Zwendelaar().addProduct(pNaam.getText(), pOmschrijving.getText(), pMerk.getText(), categories.getItemText(categories.getSelectedIndex()), callback);
	}

	private void addVeiling(int productID){
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Veiling is niet toegevoegd");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(Boolean result) {
				NotificationManager.get().createNotification("Veiling is toegevoegd");
				History.newItem("home");
			}
		};
		new Zwendelaar().addVeiling(Zwendelaar.get().getGebruiker().getEmailAdres(), productID, Integer.parseInt(vBMinCredits.getText()), vBStartDatum.getText(), vBEindDatum.getText(), callback);
	}
	
	private void addVeiling(){
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Veiling is niet toegevoegd");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(Boolean result) {
				NotificationManager.get().createNotification("Veiling is toegevoegd");
				History.newItem("home");
			}
		};
		new Zwendelaar().addVeiling(Zwendelaar.get().getGebruiker().getEmailAdres(), product.getID(), Integer.parseInt(vBMinCredits.getText()), vBStartDatum.getText(), vBEindDatum.getText(), callback);
	}

	public String formatDate(Date d){
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
		return dtf.format(d);
	}
}
