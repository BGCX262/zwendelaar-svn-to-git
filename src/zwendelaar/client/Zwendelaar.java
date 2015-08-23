package zwendelaar.client;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;
import zwendelaar.client.ui.Veilingen;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Zwendelaar implements EntryPoint {
	
	public boolean loggedIn;

	
	private static Gebruiker current = null;
	private static Zwendelaar zwn = null;
	private RootPanel main; 

	private final ZwendelaarServiceAsync zwendelaar = GWT.create(ZwendelaarService.class);

	public void onModuleLoad() {
		new HistoryListener();
		createNavigation();
	}

	public static Zwendelaar get() {
		if(zwn == null) {
			zwn = new Zwendelaar();
		}
		return zwn;
	}
	
	public boolean loggedIn() {
		if(current == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void logout() {
		setGebruiker(null);
		showPage(new Veilingen().createPage());
		String token = History.getToken();
		if(token.equals("home")){
			History.newItem("zoeken");
		}
		else{
			History.newItem("home");
		}
		createNavigation();
	}
	
	public boolean isAdmin(){
		boolean b = false;
		if(getGebruiker() == null){
			b =  false;
		}else if(getGebruiker().getAdmin()){
			b =  true;
		}else{
			b = false;
		}
		return b;
	}
	
	public void setGebruiker (Gebruiker g) {
		current = g;
	}
	
	public Gebruiker getGebruiker() {
		return current;
	}
	
	public void createNavigation() {
		RootPanel menu = RootPanel.get("links");
		menu.clear();
		menu.add(new HTML("<lu>"));
		menu.add(new HTML("<li><a href=\"#home\">Home</a></li>"));
		menu.add(new HTML("<li><a href='#zoeken'> Zoeken</a></li>"));
		menu.add(new HTML("<li><a href='#hulp' id='lastLink'> Hulp</a></li>"));
		if(loggedIn() && !isAdmin()) {
			menu.add(new HTML("<li><a href=\"#veilingtoevoegen\" id=\"lastLink2\">Veiling Toevoegen</a></li>"));
		}
		if(isAdmin()) {
			menu.add(new HTML("<li><a href=\"#veilingtoevoegen\" id='lastLink2'>Veiling Toevoegen</a></li>"));
			//menu.add(new HTML("<li><a href=\"#blokkeren\" id=\"lastLink\">Blokkeren</a></li>"));
			
		}
		menu.add(new HTML("</lu>"));
	}

	public void showPage(Widget w) {
		main = RootPanel.get("main");
		main.clear();
		main.add(w);
	}
	
	public void clearMain() {
		main = RootPanel.get("main");
		main.clear();
	}
	
	public void showElement(Widget w) {
		RootPanel menu = RootPanel.get("menu");
		menu.clear();
		menu.add(w);
	}
	
	public void loginUser(String email, String password, AsyncCallback<Gebruiker> callback){
		zwendelaar.loginUser(email, password, callback);
	}

	public void registerUser(String voornaam, String achternaam, String straat, String huisnummer, String postcode, String plaats, String email, String password, AsyncCallback<Boolean> callback) {
		zwendelaar.registerUser(voornaam, achternaam, straat, huisnummer, postcode, plaats, email, password, callback);
	}
	public void getVeilingen(AsyncCallback<ArrayList<Veiling>> callback){
		zwendelaar.getAlleVeilingen(callback);
	}
	
	public void getVeiling(int ID, AsyncCallback<Veiling> callback) {
		zwendelaar.getVeiling(ID, callback);
	}
	public void getTopVeilingen(AsyncCallback<ArrayList<Veiling>> callback){
		zwendelaar.getTopVeilingen(callback);
	}
	
	public void getAllVeilingenByUser(String email,  AsyncCallback<ArrayList<Veiling>> callback){
		zwendelaar.getAllVeilingenByUser(email, callback);
	}
	
	public void getAllVeilingenByBod(String email, AsyncCallback<ArrayList<Veiling>> callback){
		zwendelaar.getAllVeilingenByBod(email, callback);
	}
	
	public void addBod(Bod b, Gebruiker g, int ID, AsyncCallback<Integer> callback) {
		zwendelaar.addBod(b, g, ID, callback);
	}
	
	public void getProducts(AsyncCallback<ArrayList<Product>> callback){
		zwendelaar.getAllProducts(callback);
	}
	public void addProduct(String nm, String omschr, String mk, String cat, AsyncCallback<Product> callback){
		zwendelaar.addProduct(nm, omschr, mk, cat, callback);
	}
	
	public void addVeiling(String email, int pID, int credits, String bDatum, String eDatum, AsyncCallback<Boolean> callback){
		zwendelaar.addVeiling(email, pID, credits, bDatum, eDatum, callback);
	}
	
	public void getAllCategories(AsyncCallback<ArrayList<String>> callback){
		zwendelaar.getAlleCategories(callback);
	}
	
	public void getAllUsers(AsyncCallback<ArrayList<Gebruiker>> callback){
		zwendelaar.getAllUsers(callback);
	}
	
	public void getVeilingenCategorie(String Categorie, AsyncCallback<ArrayList<Veiling>> callback){
		zwendelaar.getVeilingenCategorie(Categorie, callback);
	}
	
	public void updateGebruiker(Gebruiker g, AsyncCallback<Boolean> callback)
	{
		zwendelaar.updateGebruiker(g, callback);
	}

	public void blockUser(String email, AsyncCallback<Void> callback) {
		zwendelaar.blockUser(email, callback);		
	}
	public void unblockUser(String email, AsyncCallback<Void> callback) {
		zwendelaar.unblockUser(email, callback);
	}

	public void blockProd(int id, AsyncCallback<Void> callback) {
		zwendelaar.blockProd(id, callback);	
	}
	public void unblockProd(int id, AsyncCallback<Void> callback) {
		zwendelaar.unblockProd(id, callback);
	}
	
	public void getBiedingen(Date start, Date eind, AsyncCallback<ArrayList<Bod>> callback) {
		zwendelaar.getBiedingen(start, eind, callback);
	}

	public void getAlleProducts(AsyncCallback<ArrayList<Product>> callback) {
		zwendelaar.getAlleProducts(callback);
	}

	public void zoekProduct(String naam,
			AsyncCallback<ArrayList<Veiling>> callback) {
		zwendelaar.zoekProduct(naam, callback);
		
	}

	public void zoekProductCat(String naam, String category,
			AsyncCallback<ArrayList<Veiling>> callback) {
		zwendelaar.zoekProductCat(naam, category, callback);
		
	}

	public void zoekGebruiker(String email, AsyncCallback<Gebruiker> callback) {
		zwendelaar.zoekGebruiker(email, callback);	
	}


}
